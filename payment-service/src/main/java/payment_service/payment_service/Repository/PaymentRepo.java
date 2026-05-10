package payment_service.payment_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import payment_service.payment_service.Entity.PaymentEntity;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentEntity,Long> {

    Optional<PaymentEntity> findByYookassaPaymentId(String paymentId);
}
