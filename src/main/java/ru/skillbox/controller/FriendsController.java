package ru.skillbox.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://89.111.174.153:8080")
@RestController
@RequestMapping("/api/v1")
public class FriendsController {

    @GetMapping("/friends/count")
    public ResponseEntity<Integer> getFriendsCount() {
        return ResponseEntity.ok(2);
    }
}
