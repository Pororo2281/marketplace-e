package org.example.userservice.Entity;

import org.example.userservice.Enums.Role;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private SellerProfileEntity sellerProfile;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderEntity> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshTokenEntity> refreshTokens = new ArrayList<>();

    public UserEntity() {
    }

    public UserEntity(String email, String password, String firstName, String lastName, Role role, Instant createdAt) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.createdAt = createdAt;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    public List<RefreshTokenEntity> getRefreshTokens() {
        return refreshTokens;
    }

    public void setRefreshTokens(List<RefreshTokenEntity> refreshTokens) {
        this.refreshTokens = refreshTokens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public SellerProfileEntity getSellerProfile() {
        return sellerProfile;
    }

    public void setSellerProfile(SellerProfileEntity sellerProfile) {
        this.sellerProfile = sellerProfile;
    }
}
