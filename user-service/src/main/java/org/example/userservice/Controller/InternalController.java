package org.example.userservice.Controller;

import jakarta.validation.Valid;
import org.example.userservice.Request.OAuth2UserRequest;
import org.example.userservice.Response.OAuth2UserResponse;
import org.example.userservice.Response.UserResponse;
import org.example.userservice.Service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/internal")
public class InternalController {
    private final UserService service;

    public InternalController(UserService service) {
        this.service = service;
    }




    @PostMapping("/oauth2")
    public ResponseEntity<OAuth2UserResponse> findOrCreate(@Valid @RequestBody OAuth2UserRequest request) {
        return ResponseEntity.ok(service.findOrCreate(request));
    }

    @GetMapping()
    public ResponseEntity<Page<UserResponse>> getUsers(@PageableDefault(size = 20) Pageable pageable,
                                                      @RequestParam(required = false) String search,
                                                      @RequestParam(required = false) String role,
                                                      @RequestParam(required = false) Boolean blocked){
        return ResponseEntity.ok(service.getUsers(pageable,search,role,blocked));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(service.getUserByIdForAdmin(userId));
    }

    @PutMapping("/{userId}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Long userId){
        service.blockUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{userId}/unblock")
    public ResponseEntity<Void> unBlockUser(@PathVariable Long userId){
        service.unBlockUser(userId);
        return ResponseEntity.noContent().build();
    }
}
