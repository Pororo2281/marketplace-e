package product_service.demo.Controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product_service.demo.Enum.ProductStatus;
import product_service.demo.Request.CreateCategoryRequest;
import product_service.demo.Request.UpdateCategoryRequest;
import product_service.demo.Response.CategoryResponse;
import product_service.demo.Service.CategoryService;
import product_service.demo.Service.ProductService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final CategoryService service;
    private final ProductService productService;

    public AdminController(CategoryService service, ProductService productService) {
        this.service = service;
        this.productService = productService;
    }

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
       return ResponseEntity.ok(service.createCategory(request));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@Valid @PathVariable Long id, @RequestBody UpdateCategoryRequest request){
        return ResponseEntity.ok(service.updateCategory(id, request));
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        service.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/categories/{id}/toggle-active")
    public ResponseEntity<CategoryResponse> toggleCategoryActiveStatus(@PathVariable Long id) {
        return ResponseEntity.ok(service.toggleCategoryActiveStatus(id));
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProductsForAdmin(@RequestParam(required = false) ProductStatus status,
                                                    @RequestParam(required = false) Long sellerId,
                                                    @PageableDefault(size = 20) Pageable pageable){
        return ResponseEntity.ok(productService.getAllProductsForAdmin(
                status, sellerId, pageable
        ));
    }

    @DeleteMapping("/products/{id}/force")
    public ResponseEntity<Void> forceDeleteProduct(@PathVariable Long id){
        productService.forceDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
