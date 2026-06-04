package com.marketplace.review_service.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "order-service",url = "${services.order-service}")
public interface OrderClient {

    @GetMapping("api/internal/orders/check-purchase")
    boolean checkUserPurchase(
            @RequestParam Long userId,
            @RequestParam Long productId
    );

}
