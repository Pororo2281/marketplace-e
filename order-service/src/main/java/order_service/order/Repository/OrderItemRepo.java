package order_service.order.Repository;

import order_service.order.Entity.OrderItemEntity;
import order_service.order.Enum.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepo extends JpaRepository<order_service.order.Entity.OrderItemEntity,Long> {
}
