package org.example.userservice.Controller;

import org.example.userservice.Request.OAuth2UserRequest;
import org.example.userservice.Response.OAuth2UserResponse;
import org.example.userservice.Response.UserResponse;
import org.example.userservice.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/oauth2")
    public ResponseEntity<OAuth2UserResponse> findOrCreate(@RequestBody OAuth2UserRequest request) {
        return ResponseEntity.ok(service.findOrCreate(request));
    }
}
