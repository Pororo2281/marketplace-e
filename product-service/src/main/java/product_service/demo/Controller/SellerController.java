package product_service.demo.Controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import product_service.demo.Enum.ProductStatus;
import product_service.demo.Request.UpdatePriceRequest;
import product_service.demo.Request.UpdateProductRequest;
import product_service.demo.Request.UpdateStockRequest;
import product_service.demo.Response.ProductImageResponse;
import product_service.demo.Response.ProductResponse;
import product_service.demo.Response.SellerStatsResponse;
import product_service.demo.Service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/sellers/products")
public class SellerController {

    private final ProductService service;

    public SellerController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getSellerProducts(
            @RequestHeader("X-USER-ID") Long sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "ACTIVE") ProductStatus status,
            @RequestParam(required = false) Long categoryId
            ){
        return ResponseEntity.ok(service.getSellerProducts(page, size, status, categoryId,sellerId));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> createProduct(
            @RequestHeader("X-USER-ID") Long sellerId,
            @RequestParam("json") String json,
            @RequestParam(value = "images", required = false) List<MultipartFile> images
            ){
        return ResponseEntity.ok(service.createProduct(json,images,sellerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @RequestHeader("X-USER-ID") Long sellerId,
            @PathVariable Long id){
        return ResponseEntity.ok(service.getProductByIdForSeller(id,sellerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @RequestHeader("X-USER-ID") Long sellerId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request){
        return ResponseEntity.ok(service.updateProduct(id,request,sellerId));
    }

    @PutMapping("/{id}/publish")
    public ResponseEntity<ProductResponse> publishProduct(
            @RequestHeader("X-USER-ID") Long sellerId,
            @PathVariable Long id){
        return ResponseEntity.ok(service.publishProduct(id,sellerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @RequestHeader("X-USER-ID") Long sellerId,
            @PathVariable Long id
    ) {
        service.deleteProduct(id, sellerId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/archive")
    public ResponseEntity<ProductResponse> archiveProduct(
            @RequestHeader("X-USER-ID") Long sellerId,
            @PathVariable Long id){
        return ResponseEntity.ok(service.archiveProduct(id,sellerId));
    }

    @PostMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(
            @RequestHeader("X-USER-ID") Long sellerId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateStockRequest request){
        return ResponseEntity.ok(service.updateStock(id,request,sellerId));
    }

    @PostMapping("/{id}/price")
    public ResponseEntity<ProductResponse> updatePrice(
            @RequestHeader("X-USER-ID") Long sellerId,
            @PathVariable Long id,
            @Valid @RequestBody UpdatePriceRequest request){
        return ResponseEntity.ok(service.updatePrice(id,request,sellerId));
    }

    @PostMapping(value = "/{id}/images",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> addProductImages(
            @RequestHeader("X-USER-ID") Long sellerId,
            @PathVariable Long id,
            @RequestParam("images") List<MultipartFile> images){
        return ResponseEntity.ok(service.addProductImages(id,images,sellerId));
    }

    @DeleteMapping("/{id}/images/{imageId}")
    public ResponseEntity<Void> deleteProductImage(
            @RequestHeader("X-USER-ID") Long sellerId,
            @PathVariable Long id,
            @PathVariable Long imageId){
        service.deleteProductImage(id,imageId,sellerId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/images/{imageId}/main")
    public ResponseEntity<ProductImageResponse> setMainProductImage(
            @RequestHeader("X-USER-ID") Long sellerId,
            @PathVariable Long id,
            @PathVariable Long imageId){
        return ResponseEntity.ok(service.setMainProductImage(id,imageId,sellerId));
    }

}
