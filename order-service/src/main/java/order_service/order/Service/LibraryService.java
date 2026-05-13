package order_service.order.Service;

import order_service.order.Entity.OrderEntity;
import order_service.order.Entity.OrderItemEntity;
import order_service.order.Entity.PurchasedProductEntity;
import order_service.order.Exception.NotFoundById;
import order_service.order.Mapper.EntityToPurchased;
import order_service.order.Repository.PurchasedProductRepo;
import order_service.order.Response.PurchasedProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class LibraryService {

    private final PurchasedProductRepo purchasedProductRepo;

    public LibraryService(PurchasedProductRepo purchasedProductRepo) {
        this.purchasedProductRepo = purchasedProductRepo;
    }

    public void addToLibrary(OrderEntity order) {

        List<PurchasedProductEntity> purchasedProducts = order.getItems().stream()
                .map(item -> EntityToPurchased.toEntity(item, order))
                .toList();
        purchasedProductRepo.saveAll(purchasedProducts);
    }

    public Page<PurchasedProductResponse> getMyLibrary(int size, int page, Long userId) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("purchasedAt").descending());
        var purchasedProduct =   purchasedProductRepo.findAllByUserId(userId,pageable);
        return purchasedProduct.map(EntityToPurchased::entityToPurchased);
    }

    public PurchasedProductResponse getProduct(Long productId,Long userId) {
        var product = purchasedProductRepo.findByProductId(productId)
                .filter(pr->pr.getUserId().equals(userId))
                .orElseThrow(()-> new NotFoundById("purchasedProduct with id: " + productId + " not found"));
        return EntityToPurchased.entityToPurchased(product);

    }

    public Boolean purchased(Long productId, Long userId) {
        return purchasedProductRepo.existsByUserIdAndProductId(productId,userId);
    }
}
