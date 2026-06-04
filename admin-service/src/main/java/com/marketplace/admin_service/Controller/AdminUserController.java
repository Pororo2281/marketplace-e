package com.marketplace.admin_service.Controller;

import com.marketplace.admin_service.Request.BlockUserRequest;
import com.marketplace.admin_service.Request.UnblockUserRequest;
import com.marketplace.admin_service.Response.UserResponseDto;
import com.marketplace.admin_service.Service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins/users")
public class AdminUserController {

    private final AdminUserService adminUserService;
    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(@PageableDefault(size = 20) Pageable pageable,
                                                             @RequestParam(required = false) String search,
                                                             @RequestParam(required = false) String role,
                                                             @RequestParam(required = false) Boolean blocked){
        return ResponseEntity.ok(adminUserService.getUsers(pageable,search,role,blocked));
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id){
        return ResponseEntity.ok(adminUserService.getUserById(id));
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Void> blockUser(@PathVariable Long id, @Valid @RequestBody BlockUserRequest blockUserRequest){
        adminUserService.blockUser(id, blockUserRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/unBlock")
    public ResponseEntity<Void> unBlockUser(@PathVariable Long id, @Valid @RequestBody UnblockUserRequest unblockUserRequest){
        adminUserService.unBlockUser(id, unblockUserRequest);
        return ResponseEntity.noContent().build();
    }

}
