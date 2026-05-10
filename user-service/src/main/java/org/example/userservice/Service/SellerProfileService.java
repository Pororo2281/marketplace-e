package org.example.userservice.Service;

import jakarta.transaction.Transactional;
import org.example.userservice.Entity.SellerProfileEntity;
import org.example.userservice.Entity.UserEntity;
import org.example.userservice.Exception.NotFoundById;
import org.example.userservice.Mapper.SellerMapper;
import org.example.userservice.Repository.SellerProfileRepo;
import org.example.userservice.Repository.UserRepo;
import org.example.userservice.Request.SellerRequest;
import org.example.userservice.Response.SellerResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SellerProfileService {

    private final SellerProfileRepo repo;
    private final UserRepo userRepo;

    public SellerProfileService(SellerProfileRepo repo,UserRepo userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    @Transactional
    public SellerResponse createSellerProfile(SellerRequest request, UserEntity user){
        SellerProfileEntity entity = new SellerProfileEntity();
        entity.setCreatedAt(Instant.now());
        entity.setDescription(request.getDescription());
        entity.setCompanyName(request.getCompanyName());
        entity.setUser(user);
        repo.save(entity);
        return SellerMapper.entityToSeller(entity);
    }

    public SellerResponse getSellerProfile(Long id) {
        var profile = repo.findById(id)
                .orElseThrow(()-> new NotFoundById("sellerProfile with id: " + id + " not found"));
        return SellerMapper.entityToSeller(profile);
    }

    public SellerResponse getSellerStats(Long id) {
        var sellerProfile = repo.findById(id)
                .orElseThrow(()-> new NotFoundById("sellerProfile with id: " + id + " not found"));
        return SellerMapper.entityToSeller(sellerProfile);
    }

    @Transactional
    public SellerResponse updateSellerProfile(Long userId, SellerRequest sellerRequest) {
        var seller = userRepo.findById(userId)
                .orElseThrow(()-> new NotFoundById("user with id: " + userId + " not found"));
        var sellerProfile = seller.getSellerProfile();
        if (sellerProfile==null){
            throw new RuntimeException("пользователь не является продавцом");
        }
        if (sellerRequest.getCompanyName()!=null){
            sellerProfile.setCompanyName(sellerRequest.getCompanyName());
        }
        if (sellerRequest.getDescription()!=null){
            sellerProfile.setDescription(sellerRequest.getDescription());
        }
        repo.save(sellerProfile);
        return SellerMapper.entityToSeller(sellerProfile);
    }
}
