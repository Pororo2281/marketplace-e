package product_service.demo.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import product_service.demo.Entity.ProductEntity;
import product_service.demo.Enum.ProductStatus;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity,Long>, JpaSpecificationExecutor<ProductEntity> {
    Page<ProductEntity> findByCategoryId(Long categoryId, Pageable pageable);
    boolean existsByCategoryId(Long id);

    Page<ProductEntity> findAll(Specification<ProductEntity> spec, Pageable sortedPageable);

    Page<ProductEntity> findByStatus(ProductStatus productStatus,Pageable pageable);

    @Query(value = """
       SELECT p.title
       FROM products p
       WHERE p.title ILIKE %:q%
       AND p.status = 'ACTIVE'
       GROUP BY p.title
       ORDER BY MAX(p.sales_count) DESC
       LIMIT 10
       """, nativeQuery = true)
    List<String> findSuggestions(@Param("q") String q);

    List<ProductEntity> findAllByStatus(ProductStatus status);
}
