package ru.skillbox.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    private String secret = "secret";
//    {
//        "role": "ROLE_USER",
//            "id": 1,
//            "email": "tagir@gmail.com",
//            "sub": "tagir",
//            "iat": 1725694564,
//            "exp": 9725838564
//    }
    private String token =
        "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwiaWQiOjEsImVtYWlsIjoidGFnaXJAZ21haWwuY29tIiwic3ViIjoidGFnaXIiLCJpYXQiOjE3MjU2OTQ1NjQsImV4cCI6OTcyNTgzODU2NH0.pSgFanQtSYisJjRKDH9uEXrtObW_9nUyoS1TcbGemso";
    //    {
//        "role": "ROLE_USER",
//            "id": 1,
//            "email": "tagir@gmail.com",
//            "sub": "tagir",
//            "iat": 1725694564,
//            "exp": 9725838564
//    }
    private String token2 =
    "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiUk9MRV9VU0VSIiwiaWQiOjIsImVtYWlsIjoiYWRtaW5AZ21haWwuY29tIiwic3ViIjoiYWRtaW4iLCJpYXQiOjE3MjU2OTQ1NjQsImV4cCI6OTcyNTgzODU2NH0.OoR7DDKgQKDaJ8jAMByMhL91_qBB4NPWtj1qX_vLpB4";
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

