package ru.skillbox.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    private String secret = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327855";
//    {
//        "role": "ROLE_USER",
//            "id": 1,
//            "email": "tagir@gmail.com",
//            "sub": "tagir",
//            "iat": 1725694564,
//            "exp": 1725838564
//    }
    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwiaWQiOjEsImVtYWlsIjoidGFnaXJAZ21haWwuY29tIiwic3ViIjoidGFnaXIiLCJpYXQiOjE3MjU2OTQ1NjQsImV4cCI6OTcyNTgzODU2NH0.mn6iTtPxENZMioPUSDYfiIMqtzXPONxI9VKfxsLUTbU";

    public Claims getAllClaimsFromToken() {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public Long getUserIdFromToken() {
        Object id = getAllClaimsFromToken().get("id");

        if (id instanceof Integer) {
            return ((Integer) id).longValue(); // Преобразование Integer в Long
        } else if (id instanceof Long) {
            return (Long) id; // Если уже Long
        } else {
            throw new IllegalArgumentException("ID is not of type Integer or Long");
        }
        //return (Long) getAllClaimsFromToken().get("id");
    }
    public String getRoleFromToken() {
        return (String) getAllClaimsFromToken().get("role");
    }

    public String getUsernameFromToken() {
        return getAllClaimsFromToken().getSubject();
    }
}

