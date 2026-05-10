package org.example.userservice.Security;

import org.example.userservice.Entity.UserEntity;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public record UserDetailsImpl(UserEntity entity) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + entity.getRole().name()));
    }

    public Long getId(){
        return entity.getId();
    }

    public String getRole(){
        return entity.getRole().name();
    }

    @Override
    public @Nullable String getPassword() {
        return entity.getPassword();
    }

    @Override
    public String getUsername() {
        return entity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
