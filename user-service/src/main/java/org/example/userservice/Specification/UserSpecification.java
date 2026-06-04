package org.example.userservice.Specification;

import org.example.userservice.Entity.UserEntity;
import org.example.userservice.Enums.Role;
import org.example.userservice.Enums.UserStatus;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<UserEntity> hasBlocked(Boolean blocked) {

        return (root, query, criteriaBuilder) -> {

            if (blocked == null|| blocked==false) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("userStatus"),
                    UserStatus.BLOCKED
            );
        };
    }

    public static Specification<UserEntity> hasRole(Role userRole) {

        return (root, query, criteriaBuilder) -> {

            if (userRole==null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("role"),
                    userRole
            );
        };
    }

    public static Specification<UserEntity> hasSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String likePattern = "%" + search.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), likePattern)
            );
        };
    }
}
