package org.example.userservice.Repository;

import org.example.userservice.Entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItemEntity, Long> {
}
