package ru.skillbox.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.dto.AccountDto;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/account/me")
@Profile("dev")

//Класс-заглушка для доступа в фронт
public class AccountController {


    @GetMapping
    public ResponseEntity<AccountDto> getAccount() {
        AccountDto response = new AccountDto();
        response.setId(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        response.setFirstName("Alex");
        response.setLastName("Black");

        return ResponseEntity.ok(response);
    }

}
