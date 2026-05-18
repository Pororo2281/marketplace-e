package org.example.userservice.Mapper;

import org.example.userservice.Entity.UserEntity;
import org.example.userservice.Enums.UserEventType;
import org.example.userservice.Event.UserEvent;

import java.time.Instant;

public class UserEventMapper {

    public static UserEvent createUserEvent(UserEntity user, UserEventType type){
        UserEvent userEvent = new UserEvent();
        userEvent.setEventType(type);
        userEvent.setName(user.getFirstName()+ " " + user.getLastName());
        userEvent.setEmail(user.getEmail());
        userEvent.setActionTime(Instant.now());
        userEvent.setUserId(user.getId());
        return userEvent;
    }
}
