package product_service.demo.Specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import product_service.demo.Entity.ProductEntity;
import product_service.demo.Enum.ProductStatus;

import java.math.BigDecimal;

public class ProductSpecifications {

    public static Specification<ProductEntity> hasSeller(Long sellerId){
        return (root, query, criteriaBuilder) -> {
            if (sellerId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("sellerId"), sellerId);
        };
    }

    public static Specification<ProductEntity> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<ProductEntity> hasMinPrice(BigDecimal minPrice) {
        return (root, query, cb) -> {
            if (minPrice == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
        };
    }

    public static Specification<ProductEntity> hasMaxPrice(BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (maxPrice == null) {
                return cb.conjunction();
            }
            return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
        };
    }

    public static Specification<ProductEntity> hasSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + search.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
            );
        };
    }

    public static Specification<ProductEntity> hasStatus(ProductStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<ProductEntity> hasStatusActive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), ProductStatus.ACTIVE);
    }

}
