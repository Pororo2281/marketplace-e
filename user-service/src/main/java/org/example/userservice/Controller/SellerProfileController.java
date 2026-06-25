package org.example.userservice.Controller;

import jakarta.validation.Valid;
import org.example.userservice.Request.SellerRequest;
import org.example.userservice.Response.SellerResponse;
import org.example.userservice.Service.SellerProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sellers")
public class SellerProfileController {

    private final SellerProfileService  service;

    public SellerProfileController(SellerProfileService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SellerResponse> getSellerProfile(@PathVariable Long id){
        return ResponseEntity.ok(service.getSellerProfile(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SellerResponse> getSellerProfileByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(service.getSellerProfileByUserId(userId));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<SellerResponse> getSellerStats(@PathVariable Long id){
        return ResponseEntity.ok(service.getSellerStats(id));
    }

    @PutMapping
    public ResponseEntity<SellerResponse> updateSellerProfile(
            @RequestHeader("X-USER-ID") Long userId,
            @Valid @RequestBody SellerRequest sellerRequest)
    {
        return ResponseEntity.ok(service.updateSellerProfile(userId,sellerRequest));
    }
}
