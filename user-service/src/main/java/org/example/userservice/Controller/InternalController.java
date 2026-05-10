package org.example.userservice.Controller;

import org.example.userservice.Response.UserResponse;
import org.example.userservice.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/internal")
public class InternalController {
    private final UserService service;

    public InternalController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(service.getUserById(userId));
    }
}
