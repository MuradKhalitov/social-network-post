package ru.skillbox.util;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Component
public class CurrentUsers {
    private String token =
            "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwiaWQiOiI2MGIxZjQ3OC1lYzVhLTRjZmEtYTAyMi1lZTk3MTMyMjhhODYiLCJzdWIiOiJ0YWdpckBnbWFpbC5jb20iLCJpYXQiOjE3MjU2OTQ1NjQsImV4cCI6OTcyNTgzODU2NH0.0aqpFz7xIureyrK2b_dGKW0axImrlszyX3u7PINqVn0";


//        public static String getCurrentUsername() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            Object principal = authentication.getPrincipal();
//            if (principal instanceof UserDetails) {
//                return ((UserDetails) principal).getUsername();
//            } else {
//                // Вернуть имя пользователя напрямую, если UserDetails не используется
//                return principal.toString();
//            }
//        }
//        return null;
//    }
//        public static boolean hasRole(String role) {
//        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
//                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role));
//    }

    public UUID getCurrentUserId() {
        return getUserIdFromToken();
    }

    public boolean hasRole(String role) {
        return (getRoleFromToken().equals(role));
    }

    public UUID getUserIdFromToken() {
        Map<String, Object> claims = getAllClaimsFromToken(token);
        return UUID.fromString((String) claims.get("id"));
    }

    public String getRoleFromToken() {
        Map<String, Object> claims = getAllClaimsFromToken(token);
        return (String) claims.get("role");
    }

    public Map<String, Object> getAllClaimsFromToken(String token) {
        String payload = getPayloadFromToken(token);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> claims = null;
        try {
            claims = objectMapper.readValue(payload, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка при парсинге токена", e);
        }
        return claims;
    }

    private static String getPayloadFromToken(String token) {
        String[] parts = token.split("\\.");
        if (parts.length == 3) {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(parts[1]);
            return new String(decodedBytes);
        } else {
            throw new IllegalArgumentException("Invalid JWT format");
        }
    }
}
