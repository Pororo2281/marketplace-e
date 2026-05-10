package product_service.demo.Controller;

import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import product_service.demo.Enum.ProductStatus;
import product_service.demo.Response.ProductResponse;
import product_service.demo.Service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProducts(@PageableDefault(page = 0, size = 20) Pageable pageable,
                                                             @RequestParam(required = false) Long categoryId,
                                                             @RequestParam(required = false) BigDecimal minPrice,
                                                             @RequestParam(required = false) BigDecimal maxPrice,
                                                             @RequestParam(required = false) String search,
                                                             @RequestParam(required = false) Long sellerId,
                                                             @RequestParam(required = false) @Pattern(regexp = "^(price|rating|createdAt|salesCount),(asc|desc)$") String sort) {
        return ResponseEntity.ok(service.getAllProducts(pageable,categoryId,minPrice,maxPrice,search,sellerId,sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(service.getProductById(id));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ProductResponse>> getFeaturedProducts(@RequestParam(defaultValue = "10") int limit){
        return ResponseEntity.ok(service.getFeaturedProducts(limit));
    }

    @GetMapping("/new")
    public ResponseEntity<List<ProductResponse>> getNewProducts(@RequestParam(defaultValue = "20") int limit){
        return ResponseEntity.ok(service.getNewProducts(limit));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<ProductResponse>> getPopularProducts(@RequestParam(defaultValue = "20") int limit){
        return ResponseEntity.ok(service.getPopularProducts(limit));
    }

    @PostMapping("/reindex")
    public ResponseEntity<Void> reindexProducts(){
        service.reindexProducts();
        return ResponseEntity.ok().build();
    }

}
