package ru.skillbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.dto.likePost.LikePostDto;
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
    @ResponseStatus(HttpStatus.CREATED)
    public LikePostDto createLike(@PathVariable Long id) {
        return likePostService.createLikePost(id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void deleteLike(@PathVariable Long id) {
        likePostService.deleteLikePost(id);
    }
}
