package org.example.userservice.Controller;

import jakarta.validation.Valid;
import org.example.userservice.Request.SellerRequest;
import org.example.userservice.Request.UpdateUserRequest;
import org.example.userservice.Response.AuthResponse;
import org.example.userservice.Response.SellerResponse;
import org.example.userservice.Response.UserResponse;
import org.example.userservice.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final   UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<UserResponse> getProfile(@RequestHeader("X-USER-ID") Long userId){
        return ResponseEntity.ok(userService.getProfile(userId));
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateProfile(@RequestHeader("X-USER-ID") Long userId, @Valid @RequestBody UpdateUserRequest userRequest){
        return ResponseEntity.ok(userService.updateBasicProfile(userRequest,userId));
    }

    @PostMapping
    public ResponseEntity<AuthResponse> becomeSeller(@RequestHeader("X-USER-ID") Long userId, @Valid @RequestBody SellerRequest request){
        System.out.println("userId = " + userId);
        return ResponseEntity.ok(userService.becomeSeller(request,userId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProfile(@RequestHeader("X-USER-ID") Long userId){
        userService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }
}
