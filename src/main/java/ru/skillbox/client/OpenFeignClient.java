package ru.skillbox.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "social-network-auth")
public interface OpenFeignClient {

    @GetMapping("/api/v1/auth/tokenValidation")
    boolean validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token);

}
