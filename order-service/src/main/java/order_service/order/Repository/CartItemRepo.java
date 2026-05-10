package order_service.order.Repository;

import order_service.order.Entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItemEntity,Long> {

    List<CartItemEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<CartItemEntity> findByUserIdAndProductId(Long userId,Long productId);

    @Modifying
    @Query("DELETE FROM CartItemEntity c WHERE c.userId = :userId")
    void deleteAllCartByUserId(Long userId);
}
