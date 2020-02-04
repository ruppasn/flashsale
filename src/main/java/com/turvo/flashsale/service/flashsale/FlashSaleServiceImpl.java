package com.turvo.flashsale.service.flashsale;

import com.turvo.flashsale.dto.OrderRequest;
import com.turvo.flashsale.dto.OrderResponse;
import com.turvo.flashsale.exception.IlligallFlashSaleAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlashSaleServiceImpl implements IFlashSaleService {

    @Autowired
    private FlashSaleManager flashSaleManager;

    @Override
    public void startFlashSale() {
        flashSaleManager.loadCustomers();
        flashSaleManager.loadInventory();
        flashSaleManager.startSale();
    }

    @Override
    public OrderResponse order(OrderRequest orderRequest) {
        if (!flashSaleManager.isFlashSaleActive()) {
            throw new IlligallFlashSaleAccess("Order cannot be placed.");
        }

        return flashSaleManager.order(orderRequest);
    }

    @Override
    public void stopFlashSale() {
        flashSaleManager.stopSale();
    }

}
