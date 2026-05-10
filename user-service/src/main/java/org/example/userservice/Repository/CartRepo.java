package org.example.userservice.Repository;

import org.example.userservice.Entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<CartEntity, Long> {
}
