package com.marketplace.admin_service.Client;

import com.marketplace.admin_service.Response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service",url = "${services.user-service:http://localhost:8081}")
public interface UserClient {

    @GetMapping("/api/users/internal/{userId}")
    UserResponseDto getUserById(@PathVariable Long userId);

    @PutMapping("/api/users/internal/{userId}/block")
    void blockUser(@PathVariable Long userId);

    @PutMapping("/api/users/internal/{userId}/unblock")
    void unBlockUser(@PathVariable Long userId);

    @GetMapping("/api/users/internal")
    Page<UserResponseDto> getUsers(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   @RequestParam(value = "sort", required = false,defaultValue = "createdAt,desc") String sort,
                                   @RequestParam(required = false) String search,
                                   @RequestParam(required = false) String role,
                                   @RequestParam(required = false) Boolean blocked);
}
