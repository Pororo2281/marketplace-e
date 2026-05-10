package payment_service.payment_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import payment_service.payment_service.Entity.RefundEntity;
@Repository
public interface RefundRepo extends JpaRepository<RefundEntity,Long> {

}
