package com.marketplace.review_service.Client;

import com.marketplace.review_service.Response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service",url = "${services.user-service}")
public interface UserClient {

    @GetMapping("/api/users/internal/{userId}")
    UserResponse getUserById(@PathVariable Long userId);

}
