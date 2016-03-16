package com.mindfire.bicyclesharing;

import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    public boolean canAccessUser(CurrentUser currentUser, Long userId) {
        return currentUser != null
                && (currentUser.getRole().getUserRole().equals("ADMIN") || currentUser.getUserId().equals(userId));
    }
}