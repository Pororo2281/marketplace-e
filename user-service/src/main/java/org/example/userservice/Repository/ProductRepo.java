package org.example.userservice.Repository;

import org.example.userservice.Entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<ProductEntity,Long> {

}
