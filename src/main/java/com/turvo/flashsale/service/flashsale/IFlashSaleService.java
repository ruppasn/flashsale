package com.turvo.flashsale.service.flashsale;

import com.turvo.flashsale.dto.OrderRequest;
import com.turvo.flashsale.dto.OrderResponse;

public interface IFlashSaleService {

    void startFlashSale();

    OrderResponse order(OrderRequest orderRequest);

    void stopFlashSale();
}
