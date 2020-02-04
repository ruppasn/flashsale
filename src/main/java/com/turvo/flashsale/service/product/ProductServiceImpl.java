package com.turvo.flashsale.service.product;

import com.turvo.flashsale.model.Product;
import com.turvo.flashsale.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Integer productId) {
        return productRepository.getOne(productId);
    }
}
