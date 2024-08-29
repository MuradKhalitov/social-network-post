package ru.skillbox.controller;


import ru.skillbox.dto.CommentDto;
import ru.skillbox.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post/{postId}/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public List<CommentDto> getCommentsByPostId(@PathVariable Long postId) {
        return commentService.getCommentsByPostId(postId);
    }

    @GetMapping("/{id}")
    public CommentDto getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @PostMapping
    public CommentDto createComment(@RequestBody CommentDto commentDto) {
        return commentService.createComment(commentDto);
    }

    @PutMapping("/{id}")
    public CommentDto updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        return commentService.updateComment(id, commentDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok().build();
    }
}

