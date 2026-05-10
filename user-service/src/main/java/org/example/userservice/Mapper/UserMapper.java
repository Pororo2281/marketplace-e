package org.example.userservice.Mapper;

import org.example.userservice.Entity.UserEntity;
import org.example.userservice.Response.UserResponse;

public class UserMapper {
    public static UserResponse entityToUser(UserEntity entity){
        UserResponse response = new UserResponse();
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setEmail(entity.getEmail());
        response.setRole(entity.getRole());
        return response;
    }
}
