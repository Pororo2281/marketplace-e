package com.marketplace.review_service.Client;

import com.marketplace.review_service.Response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service",url = "http://localhost:8082")
public interface ProductClient {

    @GetMapping("/api/internal/products")
    ProductResponse getSellerIdByProductId(@RequestParam Long productId);
}