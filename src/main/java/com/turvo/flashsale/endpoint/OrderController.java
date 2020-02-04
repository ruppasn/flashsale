package com.turvo.flashsale.endpoint;


import com.turvo.flashsale.dto.OrderRequest;
import com.turvo.flashsale.dto.OrderResponse;
import com.turvo.flashsale.service.flashsale.IFlashSaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/orders")
public class OrderController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IFlashSaleService flashSaleService;

    @PostMapping
    public OrderResponse order(OrderRequest orderRequest) {
        log.info("Product ordered");
        return flashSaleService.order(orderRequest);
    }
}
