package org.example.userservice.Repository;

import org.example.userservice.Entity.SellerProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerProfileRepo extends JpaRepository<SellerProfileEntity, Long> {
}
