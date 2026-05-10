package org.example.userservice.Repository;

import org.example.userservice.Entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepo extends JpaRepository<ReviewEntity,Long> {
}
