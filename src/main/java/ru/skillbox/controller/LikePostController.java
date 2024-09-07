package ru.skillbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.dto.LikePostDto;
import ru.skillbox.service.LikePostService;

@RestController
@RequestMapping("/api/v1/post/{id}/like")
public class LikePostController {

    private final LikePostService likePostService;

    @Autowired
    public LikePostController(LikePostService likePostService) {
        this.likePostService = likePostService;
    }

    @PostMapping
    public LikePostDto createLike(@PathVariable Long id) {
        return likePostService.createLikePost(id);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLike(@PathVariable Long id) {
        likePostService.deleteLike(id);
        return ResponseEntity.ok().build();
    }
}
