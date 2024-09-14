package ru.skillbox.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class JwtTokenUtil {

    private String secret = "123123456456789789123123456456789789123123456456789789";
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

    public UUID getUserIdFromToken() {
        String id = (String) getAllClaimsFromToken().get("id");
        return UUID.fromString(id);
    }

    public String getRoleFromToken() {
        return (String) getAllClaimsFromToken().get("role");
    }

    public String getUsernameFromToken() {
        return getAllClaimsFromToken().getSubject();
    }
}

