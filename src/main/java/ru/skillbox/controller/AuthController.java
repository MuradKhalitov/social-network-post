package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.dto.AuthenticateRq;
import ru.skillbox.dto.LoginResponse;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth/login")

//Класс-заглушка для доступа в фронт
public class AuthController {

    @CrossOrigin(origins = "http://localhost:8080")
    @PostMapping
    public ResponseEntity<LoginResponse> getAllDialogs(@RequestBody AuthenticateRq request) {
        LoginResponse response = new LoginResponse();
        response.setData("asdasd");
        response.setTimestamp(234234L);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<LoginResponse> getkek() {
        LoginResponse response = new LoginResponse();
        response.setData("asdasd");
        response.setTimestamp(234234L);

        return ResponseEntity.ok(response);
    }

}
