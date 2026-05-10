package org.example.userservice.Repository;

import org.example.userservice.Entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity, Long> {

}
