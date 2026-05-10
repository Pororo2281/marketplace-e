package org.example.userservice.Security;

import org.example.userservice.Entity.UserEntity;
import org.example.userservice.Repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

    private UserRepo userRepo;

    public UserDetailServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepo.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User with email " + username + " not found"));
        return new UserDetailsImpl(userEntity);
    }
}
