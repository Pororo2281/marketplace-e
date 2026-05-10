package order_service.order.Repository;

import order_service.order.Entity.MainOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainOrderRepo extends JpaRepository<MainOrderEntity, Long> {

}
