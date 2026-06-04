package com.marketplace.admin_service.Client;

import com.marketplace.admin_service.Response.ProductResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "product-service",url = "${services.product-service:http://localhost:8082}")
public interface ProductClient {

    @GetMapping("/api/products")
    Page<ProductResponseDto> getProducts( @RequestParam("page") int page,
                                          @RequestParam("size") int size,
                                          @RequestParam(value = "sort", required = false,defaultValue = "createdAt,desc") String sort);

    @PutMapping("/api/internal/products/{id}/approve")
    void approveProduct(@PathVariable Long id);

    @GetMapping("api/products/{id}")
    ProductResponseDto getProduct(@PathVariable Long id);

    @PutMapping("/api/internal/products/{id}/reject")
    void rejectProduct(@PathVariable Long id);
}
