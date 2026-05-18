package org.example.userservice.Entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiry;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public PasswordResetTokenEntity() {
    }

    public PasswordResetTokenEntity(String token, Instant expiry, UserEntity user) {
        this.token = token;
        this.expiry = expiry;
        this.user = user;
    }

    public boolean isExpired() {
        return Instant.now().isAfter(expiry);
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Instant getExpiry() {
        return expiry;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpiry(Instant expiry) {
        this.expiry = expiry;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
