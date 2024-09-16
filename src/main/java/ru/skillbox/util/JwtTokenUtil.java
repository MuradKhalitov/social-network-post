package ru.skillbox.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtTokenUtil {

    // private String secret = "123123456456789789123123456456789789123123456456789789";
//    {
//        "role": "ROLE_USER",
//            "id": 60b1f478-ec5a-4cfa-a022-ee9713228a86,
//            "email": "tagir@gmail.com",
//            "sub": "tagir",
//            "iat": 1725694564,
//            "exp": 9725838564
//    }
    private String token =
            "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwiaWQiOiI2MGIxZjQ3OC1lYzVhLTRjZmEtYTAyMi1lZTk3MTMyMjhhODYiLCJlbWFpbCI6InRhZ2lyQGdtYWlsLmNvbSIsInN1YiI6InRhZ2lyIiwiaWF0IjoxNzI1Njk0NTY0LCJleHAiOjk3MjU4Mzg1NjR9.kF6b-qUCR3Rq9GU0pA6sZAlBnlr4ewrkjsQjFh21_v4";

    public Claims getAllClaimsFromToken() {
        return Jwts.parserBuilder()
                .build() // Создаем парсер без проверки подписи
                .parseClaimsJwt(token)
                .getBody(); // Получаем тело токена (Payload)
    }

    public UUID getUserIdFromToken() {
        // Извлекаем полезную нагрузку в виде JSON
        String payload = getPayloadFromToken(token);

        // Парсим JSON с помощью Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> claims = null;
        try {
            claims = objectMapper.readValue(payload, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Извлекаем "id" из полезной нагрузки
        String idString = (String) claims.get("id");

        // Преобразуем строку в UUID
        return UUID.fromString(idString);

//        String id = (String) getAllClaimsFromToken().get("id");
//        return UUID.fromString(id);
    }

    public String getRoleFromToken() {
        return (String) getAllClaimsFromToken().get("role");
    }

//    public String getUsernameFromToken() {
//        return getAllClaimsFromToken().getSubject();
//    }


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

