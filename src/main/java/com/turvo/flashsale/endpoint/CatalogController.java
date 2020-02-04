package com.turvo.flashsale.endpoint;

import com.turvo.flashsale.dto.ProductDTO;
import com.turvo.flashsale.model.Product;
import com.turvo.flashsale.service.product.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.turvo.flashsale.builder.ProductBuilder.buildProductDTO;
import static java.lang.Integer.parseInt;


@RestController
@RequestMapping("v1/products")
public class CatalogController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IProductService productService;

    @GetMapping
    public List<Product> getProducts() {
        log.info("get products for flash sale");
        return productService.getProducts();
    }

    @GetMapping("{productId}")
    public ProductDTO getProduct(@PathVariable("productId") String productId) {
        log.info("get product details");
        return buildProductDTO(productService.getProduct(parseInt(productId)));
    }

}
