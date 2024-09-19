package ru.skillbox.util;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Component
public class CurrentUsers {
    private String token =
            "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwiaWQiOiI2MGIxZjQ3OC1lYzVhLTRjZmEtYTAyMi1lZTk3MTMyMjhhODYiLCJpYXQiOjE3MjU2OTQ1NjQsImV4cCI6OTcyNTgzODU2NH0.nbu7NN_ZMj_SbtDAAJLKxoi4vLZwSJXzqyCmw4RJG5k";


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
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    // Проверяем, что пользователь аутентифицирован
    if (authentication != null && authentication.isAuthenticated()) {
        // Получаем пользователя из контекста
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            // Получаем id, который сохранён в поле username
            String userId = ((User) principal).getUsername();
            try {
                return UUID.fromString(userId);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid UUID format for user ID: " + userId, e);
            }
        }
    }

    // Если пользователь не аутентифицирован или id отсутствует, можно бросить исключение
    throw new RuntimeException("User is not authenticated");
}
public boolean hasRole(String role) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.equals(new SimpleGrantedAuthority(role)));
    }

    // Если роль не найдена, возвращаем false
    return false;
}


    public UUID getUserIdFromToken() {
        Map<String, Object> claims = getAllClaimsFromToken(token);
        return UUID.fromString((String) claims.get("id"));
    }

    public String getRoleFromToken() {
        Map<String, Object> claims = getAllClaimsFromToken(token);
        return (String) claims.get("roles");
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
