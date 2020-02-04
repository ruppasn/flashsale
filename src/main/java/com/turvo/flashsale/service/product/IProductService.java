package com.turvo.flashsale.service.product;

import com.turvo.flashsale.model.Product;

import java.util.List;

public interface IProductService {

    List<Product> getProducts();
    Product getProduct(Integer productId);
}
