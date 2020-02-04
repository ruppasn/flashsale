package com.turvo.flashsale.service.flashsale;

import com.google.common.collect.Lists;
import com.turvo.flashsale.config.Constants;
import com.turvo.flashsale.dto.OrderRequest;
import com.turvo.flashsale.dto.OrderResponse;
import com.turvo.flashsale.exception.OrderException;
import com.turvo.flashsale.model.Customer;
import com.turvo.flashsale.model.Order;
import com.turvo.flashsale.model.OrderStatus;
import com.turvo.flashsale.model.Product;
import com.turvo.flashsale.repository.CustomerRepository;
import com.turvo.flashsale.repository.OrderRepository;
import com.turvo.flashsale.repository.ProductRepository;
import com.turvo.flashsale.service.cache.ICacheService;
import com.turvo.flashsale.service.cache.ILockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

import static com.turvo.flashsale.config.Constants.*;
import static java.lang.Integer.parseInt;

@Service
public class FlashSaleManager {

    private boolean isStarted = false;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ExecutorService EXECUTOR_SERVICE;

    public FlashSaleManager() {
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);
    }

    @Autowired
    private ILockService lockService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository buyerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ICacheService<String, Object> cacheService;

    public void loadCustomers() {
        List<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
            cacheService.set(buildCustomerKey(customer), BUYER_CACHE_PREFIX, Boolean.TRUE, FLASHSALE_DURATION);
        }
    }

    public void loadInventory() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            cacheService.set(buildProductKey(product), PRODUCT_CACHE_PREFIX, product.getQuantity(), FLASHSALE_DURATION);
        }
    }

    public void startSale() {
        isStarted = true;
    }

    public void stopSale() {
        isStarted = false;
    }

    public boolean isFlashSaleActive() {
        return isStarted;
    }

    public OrderResponse order(OrderRequest orderRequest) {

        final String productKey = PRODUCT_CACHE_PREFIX + ":" + orderRequest.getProductId();
        final String buyerKey = BUYER_CACHE_PREFIX + ":" + orderRequest.getCustomerId();
        final List<String> watchKeys = Lists.newArrayList(productKey, buyerKey);
        final Long end = System.currentTimeMillis() + Constants.BUY_TIMEOUT.longValue() * 1000 * 1000 + 1000;

        final OrderResponse orderResponse = new OrderResponse();

        Future<OrderResponse> future = EXECUTOR_SERVICE.submit(new Callable<OrderResponse>() {
            @Override
            public OrderResponse call() throws Exception {

                // We will exit if the max retry interval has passed
                while (System.currentTimeMillis() < end) {
                    final String readLock = lockService.acquireLockWithTimeout(Constants.ELIGIBILITY_LOCKNAME,
                            Constants.LOCK_ACQUIRE_TIMEOUT, Constants.ELIGIBILITY_LOCK_TIMEOUT);
                    if (readLock == null) {
//                        logger.info("Could not obtain readLock, buyer: " + buyerId);
                        Thread.sleep(Constants.PURCHASE_CACHE_CYCLE_SLEEP);
                        continue;
                    }
                    final String writeLock = lockService.acquireLockWithTimeout(Constants.BUY_LOCKNAME,
                            Constants.LOCK_ACQUIRE_TIMEOUT, Constants.BUY_LOCK_TIMEOUT);

                    if (writeLock == null) {
//                        logger.info("Could not obtain writeLock, buyer: " + buyerId);
                        lockService.releaseLock(Constants.ELIGIBILITY_LOCKNAME, readLock);
                        Thread.sleep(Constants.PURCHASE_CACHE_CYCLE_SLEEP);
                        continue;
                    }

                    // Make the transaction only if we have both locks
                    return redisTemplate.execute(new SessionCallback<OrderResponse>() {
                        @Override
                        public OrderResponse execute(RedisOperations operations) throws DataAccessException {
                            try {
                                operations.watch(watchKeys);

                                final Integer remainingSKU = Float.valueOf(operations.opsForValue().get(productKey) + "").intValue();
                                final Boolean buyerStatus = (Boolean) operations.opsForValue().get(buyerKey);

                                if (buyerStatus != null && buyerStatus && remainingSKU != null && remainingSKU > 0) {
                                    logger.info("SKU: " + remainingSKU + ", buyer: " + orderRequest.getCustomerId());
                                    operations.multi();
                                    final Integer changedSKU = remainingSKU - 1;
                                    operations.opsForValue().set(productKey, changedSKU);
                                    // use it  to restrict customer to order only once
                                    operations.delete(buyerKey);
                                    operations.exec();
                                    operations.unwatch();
                                    logger.info("Purchase successful, buyer: " + orderRequest.getCustomerId());

                                    persistOrder(changedSKU, orderRequest);
                                    return orderResponse;
                                } else {
                                    operations.unwatch();
//                                    logger.info("Can't buy for this buyer: " + buyerId);
                                    throw new OrderException("Cannot buy for this customer");
                                }
                            } finally {
                                lockService.releaseLock(Constants.ELIGIBILITY_LOCKNAME, readLock);
                                lockService.releaseLock(Constants.BUY_LOCKNAME, writeLock);
                            }
                        }
                    });
                }
                return orderResponse;
            }

        });

        try {
            return future.get(Constants.BUY_TIMEOUT.longValue(), TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error("Purchase interrupted", e);
        } catch (ExecutionException e) {
            logger.error("Purchase interrupted", e);
        } catch (TimeoutException e) {
            logger.error("Purchase interrupted", e);
        }
        return orderResponse;
    }

    @Transactional
    private void persistOrder(Integer skuQuantity, OrderRequest orderRequest) {
        Order order = new Order();
        Optional<Customer> customerOptional = customerRepository.findById(parseInt(orderRequest.getCustomerId()));
        Customer customer = customerOptional.get();
        Optional<Product> productOptional = productRepository.findById(parseInt(orderRequest.getProductId()));
        Product product = productOptional.get();
        order.setCustomer(customer);
        order.setProduct(product);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderedDate(new Timestamp(System.currentTimeMillis()));

        product.setQuantity(skuQuantity);
        productRepository.save(product);

        customer.setEligibility(false);
        customerRepository.save(customer);

        orderRepository.saveAndFlush(order);
    }

    private String buildProductKey(Product product) {
        return product.getId().toString();
    }

    private String buildCustomerKey(Customer customer) {
        return customer.getId().toString();
    }
}
