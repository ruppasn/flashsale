package com.turvo.flashsale.builder;

import com.turvo.flashsale.dto.ProductDTO;
import com.turvo.flashsale.model.Product;

public class ProductBuilder {

    public static ProductDTO buildProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription(product.getDescription());
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setSku(product.getSku());
        productDTO.setQuantity(product.getQuantity());
        return productDTO;
    }
}
