package org.example.userservice.Repository;

import org.example.userservice.Entity.PasswordResetTokenEntity;
import org.example.userservice.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepo extends JpaRepository<PasswordResetTokenEntity,Long> {

    Optional<PasswordResetTokenEntity> findByToken(String token);

}
