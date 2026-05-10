package product_service.demo.Controller;

import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product_service.demo.Response.CategoryResponse;
import product_service.demo.Response.ProductResponse;
import product_service.demo.Service.CategoryService;
import product_service.demo.Service.ProductService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;
    private final ProductService productService;

    public CategoryController(CategoryService service, ProductService productService) {
        this.service = service;
        this.productService = productService;
    }


    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getCategories(Pageable pageable){
        return ResponseEntity.ok(service.getAllCategories(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok(service.getCategoryById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryResponse> getCategoryBySlug(@PathVariable String slug){
        return ResponseEntity.ok(service.getCategoryBySlug(slug));
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategoryId(@PathVariable Long id,
                                                                         @PageableDefault(page = 0, size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        return ResponseEntity.ok(productService.getProductsByCategory(id,pageable ));
    }

}
