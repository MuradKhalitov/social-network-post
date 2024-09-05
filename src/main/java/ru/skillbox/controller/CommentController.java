package ru.skillbox.controller;

import ru.skillbox.dto.CommentDto;
import ru.skillbox.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

//    @PostMapping
//    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
//    public CommentDTO createComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) {
//        return commentService.createComment(postId, commentDTO);
//    }


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public CommentDto createComment(@PathVariable Long postId, @RequestBody CommentDto commentDTO,
                                    @RequestParam(required = false) Long parentCommentId) {
        return commentService.createComment(postId, commentDTO, parentCommentId);
    }

    @PostMapping("/{commentId}/subcomment")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public CommentDto createReplyComment(@PathVariable Long postId, @RequestBody CommentDto commentDTO,
                                         @PathVariable Long commentId) {
        return commentService.createComment(postId, commentDTO, commentId);
    }



    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public List<CommentDto> getCommentsByNewsId(@PathVariable Long postId,
                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                @RequestParam(required = false, defaultValue = "10") int size) {
        List<CommentDto> commentDtoList = commentService.getCommentsByNewsId(postId, PageRequest.of(page, size));

        return commentDtoList;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public CommentDto getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @PutMapping("/{id}")
    public CommentDto updateComment(@PathVariable Long id, @RequestBody CommentDto commentDTO) {
        return commentService.updateComment(id, commentDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }
}

