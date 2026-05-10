package product_service.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import product_service.demo.Entity.ProductImageEntity;

@Repository
public interface ProductImageRepo extends JpaRepository<ProductImageEntity, Long> {
}
