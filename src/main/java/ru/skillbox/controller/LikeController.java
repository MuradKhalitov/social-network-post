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
    public List<LikeDto> getLikesByPostId(@PathVariable Long postId) {
        return likeService.getLikesByPostId(postId);
    }

    @GetMapping("/{id}")
    public LikeDto getLikeById(@PathVariable Long id) {
        return likeService.getLikeById(id);
    }

    @PostMapping
    public LikeDto createLike(@RequestBody LikeDto likeDto) {
        return likeService.createLike(likeDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long id) {
        likeService.deleteLike(id);
        return ResponseEntity.ok().build();
    }
}
