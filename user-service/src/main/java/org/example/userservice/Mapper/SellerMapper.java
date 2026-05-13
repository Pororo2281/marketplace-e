package org.example.userservice.Mapper;

import org.example.userservice.Entity.SellerProfileEntity;
import org.example.userservice.Entity.UserEntity;
import org.example.userservice.Request.SellerRequest;
import org.example.userservice.Response.SellerResponse;

import java.time.Instant;

public class SellerMapper {

    public static SellerResponse entityToSeller(SellerProfileEntity entity){
        SellerResponse sellerResponse = new SellerResponse();
        sellerResponse.setCompanyName(entity.getCompanyName());
        sellerResponse.setCreatedAt(entity.getCreatedAt());
        sellerResponse.setDescription(entity.getDescription());
        sellerResponse.setRating(entity.getRating());
        sellerResponse.setTotalSales(entity.getTotalSales());
        return sellerResponse;
    }

    public static SellerProfileEntity requestToEntity(SellerRequest request, UserEntity user) {
        SellerProfileEntity entity = new SellerProfileEntity();
        entity.setCreatedAt(Instant.now());
        entity.setDescription(request.getDescription());
        entity.setCompanyName(request.getCompanyName());
        entity.setUser(user);
        return entity;
    }

}
