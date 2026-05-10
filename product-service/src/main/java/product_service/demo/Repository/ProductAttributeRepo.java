package product_service.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import product_service.demo.Entity.ProductAttributeEntity;

import java.beans.JavaBean;

@Repository
public interface ProductAttributeRepo extends JpaRepository<ProductAttributeEntity, Long> {
}
