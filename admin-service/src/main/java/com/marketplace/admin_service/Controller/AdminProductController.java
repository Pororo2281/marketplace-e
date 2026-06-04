package com.marketplace.admin_service.Controller;

import com.marketplace.admin_service.Request.ApproveProductRequest;
import com.marketplace.admin_service.Request.RejectProductRequest;
import com.marketplace.admin_service.Response.ProductResponseDto;
import com.marketplace.admin_service.Service.AdminProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins/product")
public class AdminProductController {

    private final AdminProductService adminProductService;

    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<ProductResponseDto>> getPendingProducts(@PageableDefault(size = 20) Pageable pageable){
        return ResponseEntity.ok(adminProductService.getPendingProducts(pageable));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveProduct(@PathVariable Long id, @Valid @RequestBody ApproveProductRequest approveProductRequest){
        adminProductService.approveProduct(id, approveProductRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectProduct(@PathVariable Long id, @Valid @RequestBody RejectProductRequest rejectProductRequest){
        adminProductService.rejectProduct(id, rejectProductRequest);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id){
        return ResponseEntity.ok(adminProductService.getProductById(id));
    }

}
