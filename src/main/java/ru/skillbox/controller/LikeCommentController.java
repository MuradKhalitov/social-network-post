package ru.skillbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.dto.likeComment.LikeCommentDto;
import ru.skillbox.service.LikeCommentService;

@RestController
@RequestMapping("/api/v1/post/{id}/comment/{commentId}/like")
public class LikeCommentController {

    @Autowired
    private LikeCommentService likeCommentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createLike(@PathVariable Long id, @PathVariable Long commentId) {
        likeCommentService.createLikeComment(commentId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void deleteLike(@PathVariable Long id, @PathVariable Long commentId) {
        likeCommentService.deleteLikeComment(commentId);
    }
}
