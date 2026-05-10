package product_service.demo.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product_service.demo.Response.ProductResponse;
import product_service.demo.Service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final ProductService productService;

    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getProductsBySearch(@RequestParam(required = false) String q,
                                                                     @RequestParam(required = false) Long categoryId,
                                                                     @RequestParam(required = false) BigDecimal minPrice,
                                                                     @RequestParam(required = false) BigDecimal maxPrice,
                                                                     @PageableDefault(size = 20) Pageable pageable){
        return ResponseEntity.ok(productService.search(q, categoryId, minPrice, maxPrice, pageable));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getProductSuggestions(@RequestParam String q){
        return ResponseEntity.ok(productService.getSearchSuggestions(q));
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<Page<ProductResponse>> getProductsBySellerId(@PathVariable Long sellerId, @PageableDefault Pageable pageable){
        return ResponseEntity.ok(productService.getProductsBySellerId(sellerId,pageable));
    }
}
