package org.example.userservice.Repository;

import org.example.userservice.Entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, Long> {

}
