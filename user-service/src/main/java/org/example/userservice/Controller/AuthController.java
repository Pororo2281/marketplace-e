package org.example.userservice.Controller;

import jakarta.validation.Valid;
import org.example.userservice.Request.*;
import org.example.userservice.Response.AuthResponse;
import org.example.userservice.Response.UserResponse;
import org.example.userservice.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody CreateUserRequest createUserRequest){
        return ResponseEntity.ok(userService.register(createUserRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.login(userRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody RefreshTokenRequest request){
        return ResponseEntity.ok(userService.refreshAccessToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request){
        userService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@Valid @RequestBody ChangePasswordRequest request){
        userService.forgotPassword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        userService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }
}
