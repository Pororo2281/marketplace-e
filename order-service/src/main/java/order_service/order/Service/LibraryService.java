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

@Service
public class LibraryService {

    private final PurchasedProductRepo purchasedProductRepo;

    public LibraryService(PurchasedProductRepo purchasedProductRepo) {
        this.purchasedProductRepo = purchasedProductRepo;
    }

    public void addToLibrary(OrderEntity order) {

        for (OrderItemEntity item : order.getItems()) {

            PurchasedProductEntity purchased = new PurchasedProductEntity();
            purchased.setUserId(order.getUserId());
            purchased.setDownloadUrl(item.getDownloadUrl());
            purchased.setLicenseKey(item.getLicenseKey());
            purchased.setOrderId(item.getOrder().getId());
            purchased.setOrderItemId(item.getId());
            purchased.setPurchasedAt(Instant.now());
            purchased.setAccessCount(item.getDownloadCount());
            purchased.setLastAccessedAt(Instant.now());
            purchased.setProductId(item.getProductId());
            purchased.setProductImageUrl(item.getProductImageUrl());
            purchased.setProductTitle(item.getProductTitle());
            purchased.setProductType(item.getProductType());
            purchasedProductRepo.save(purchased);

        }
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
