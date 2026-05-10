package order_service.order.Repository;

import order_service.order.Entity.PurchasedProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Locale;
import java.util.Optional;

@Repository
public interface PurchasedProductRepo extends JpaRepository<PurchasedProductEntity,Long> {

    Page<PurchasedProductEntity> findAllByUserId(Long userId, Pageable pageable);

    Boolean existsByUserIdAndProductId(Long purchasedId, Long userId);

    Optional<PurchasedProductEntity> findByProductId(Long productId);

    Optional<PurchasedProductEntity> findByOrderItemIdAndUserId(Long orderItemId, Long userId);

    Optional<PurchasedProductEntity> findByUserIdAndProductId(Long userId, Long productId);
}
