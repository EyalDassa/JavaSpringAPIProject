package aii.Utility;

import org.springframework.stereotype.Component;

import aii.Boundary.UserBoundary;
import aii.data.UserEntity;

@Component
public class UserConverter {
    public UserEntity toEntity(UserBoundary boundary) {
        UserEntity entity = new UserEntity();
        entity.setUserId(boundary.getUserId());
        entity.setUsername(boundary.getUsername());
        entity.setAvatar(boundary.getAvatar());
        entity.setRole(boundary.getRole());
        return entity;
    }

    public UserBoundary toBoundary(UserEntity entity) {
        UserBoundary boundary = new UserBoundary();
        boundary.setUserId(entity.generateUserId());
        boundary.setUsername(entity.getUsername());
        boundary.setAvatar(entity.getAvatar());
        boundary.setRole(entity.getRole());
        return boundary;
    }
}
