package product_service.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import product_service.demo.Entity.CategoryEntity;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity, Long> {

    Optional<CategoryEntity> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
