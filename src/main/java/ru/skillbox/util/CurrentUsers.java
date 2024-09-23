package ru.skillbox.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CurrentUsers {

public UUID getCurrentUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            String userId = ((User) principal).getUsername();
            try {
                return UUID.fromString(userId);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid UUID format for user ID: " + userId, e);
            }
        }
    }
    throw new RuntimeException("User is not authenticated");
}
public boolean hasRole(String role) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.equals(new SimpleGrantedAuthority(role)));
    }
    return false;
}
}
