package order_service.order.Repository;

import order_service.order.Entity.OrderEntity;
import order_service.order.Enum.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, Long> {

    Page<OrderEntity> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable);

    Page<OrderEntity> findBySellerIdAndStatus(Long userId, OrderStatus status, Pageable pageable);

    Optional<OrderEntity> findByUserIdAndOrderNumber(Long userId,String orderNumber);

    Page<OrderEntity> findBySellerId(Long userId, Pageable pageable);

    boolean existsBySellerId(Long sellerId);

    long countBySellerId(Long sellerId);

    long countBySellerIdAndStatus(Long sellerId, OrderStatus status);

    @Query("""
       SELECT COALESCE(SUM(o.total), 0)
       FROM OrderEntity o
       WHERE o.sellerId = :sellerId
       AND o.paymentStatus = 'COMPLETED'
       """)
    BigDecimal getTotalRevenue(Long sellerId);

    @Query("""
       SELECT COALESCE(SUM(o.total), 0)
       FROM OrderEntity o
       WHERE o.sellerId = :sellerId
       AND o.paymentStatus = 'PENDING'
       """)
    BigDecimal getPendingRevenue(Long sellerId);

    @Query("""
       SELECT COALESCE(SUM(o.total), 0)
       FROM OrderEntity o
       WHERE o.sellerId = :sellerId
       AND o.paymentStatus = 'REFUNDED'
       """)
    BigDecimal getRefundedAmount(Long sellerId);

    @Query("""
       SELECT COALESCE(SUM(i.quantity), 0)
       FROM OrderItemEntity i
       WHERE i.order.sellerId = :sellerId
       AND i.order.paymentStatus = 'COMPLETED'
       """)
    Long getTotalProductsSold(Long sellerId);

    @Query("""
       SELECT COUNT(DISTINCT o.userId)
       FROM OrderEntity o
       WHERE o.sellerId = :sellerId
       """)
    Long getUniqueCustomers(Long sellerId);

    @Query("""
       SELECT MIN(o.createdAt)
       FROM OrderEntity o
       WHERE o.sellerId = :sellerId
       """)
    Instant getFirstOrderDate(Long sellerId);

    @Query("""
       SELECT MAX(o.createdAt)
       FROM OrderEntity o
       WHERE o.sellerId = :sellerId
       """)
    Instant getLastOrderDate(Long sellerId);

    @Query("""
       SELECT COUNT(o)
       FROM OrderEntity o
       WHERE o.sellerId = :sellerId
       AND o.createdAt >= :fromDate
       """)
    Long countOrdersLast30Days(Long sellerId, Instant fromDate);

    @Query("""
       SELECT COALESCE(SUM(o.total), 0)
       FROM OrderEntity o
       WHERE o.sellerId = :sellerId
       AND o.paymentStatus = 'COMPLETED'
       AND o.createdAt >= :fromDate
       """)
    BigDecimal getRevenueLast30Days(Long sellerId, Instant fromDate);

    @Query("""
       SELECT COALESCE(SUM(i.quantity), 0)
       FROM OrderItemEntity i
       WHERE i.order.sellerId = :sellerId
       AND i.order.createdAt >= :fromDate
       """)
    Long getProductsSoldLast30Days(Long sellerId, Instant fromDate);

    @Query("""
       SELECT COUNT(DISTINCT o.userId)
       FROM OrderEntity o
       WHERE o.sellerId = :sellerId
       AND o.createdAt >= :fromDate
       """)
    Long getNewCustomersLast30Days(Long sellerId, Instant fromDate);

    @Query("SELECT o FROM OrderEntity o JOIN o.items i " +
            "WHERE i.productId = :productId ")
    Optional<OrderEntity> findFirstByProductId(@Param("productId") Long productId);

    @Query("SELECT o FROM OrderEntity o JOIN o.items i " +
       "WHERE i.productId = :productId " +
       "AND o.userId = :userId")
Optional<OrderEntity> findFirstByProductIdAndUserId(@Param("productId") Long productId,@Param("userId") Long userId);
}
