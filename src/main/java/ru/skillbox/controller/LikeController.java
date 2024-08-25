package ru.skillbox.controller;

import ru.skillbox.dto.LikeDto;
import ru.skillbox.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post/{postId}/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping
    public ResponseEntity<List<LikeDto>> getLikesByPostId(@PathVariable Long postId) {
        List<LikeDto> likes = likeService.getLikesByPostId(postId);
        return ResponseEntity.ok(likes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LikeDto> getLikeById(@PathVariable Long id) {
        LikeDto like = likeService.getLikeById(id);
        return like != null ? ResponseEntity.ok(like) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<LikeDto> createLike(@RequestBody LikeDto likeDto) {
        LikeDto createdLike = likeService.createLike(likeDto);
        return new ResponseEntity<>(createdLike, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long id) {
        likeService.deleteLike(id);
        return ResponseEntity.ok().build();
    }
}
