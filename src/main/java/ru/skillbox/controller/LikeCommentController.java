package ru.skillbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.dto.LikeCommentDto;
import ru.skillbox.dto.LikePostDto;
import ru.skillbox.service.LikeCommentService;
import ru.skillbox.service.LikePostService;

@RestController
@RequestMapping("/api/v1/post/{id}/comment/{commentId}/like")
public class LikeCommentController {

    @Autowired
    private LikeCommentService likeCommentService;

    @PostMapping
    public LikeCommentDto createLike(@PathVariable Long id, @PathVariable Long commentId) {
        return likeCommentService.createLikeComment(id, commentId);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteLike(@PathVariable Long commentId) {
        likeCommentService.deleteLike(commentId);
        return ResponseEntity.ok().build();
    }
}
