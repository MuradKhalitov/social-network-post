package ru.skillbox.controller;

import ru.skillbox.dto.CommentDTO;
import ru.skillbox.mapper.CommentMapper;
import ru.skillbox.model.Comment;
import ru.skillbox.model.User;
import ru.skillbox.service.CommentService;
import ru.skillbox.service.UserService;
import ru.skillbox.util.CurrentUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public CommentDTO createComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO,
                                    @RequestParam(required = false) Long parentCommentId) {
        return commentService.createComment(postId, commentDTO, parentCommentId);
    }

    @PostMapping("/{commentId}/subcomment")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public CommentDTO createReplyComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO,
                                         @PathVariable Long commentId) {
        return commentService.createComment(postId, commentDTO, commentId);
    }



    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public List<CommentDTO> getCommentsByNewsId(@PathVariable Long postId,
                                                @RequestParam(required = false, defaultValue = "0") int page,
                                                @RequestParam(required = false, defaultValue = "10") int size) {
        List<CommentDTO> commentDTOList = commentService.getCommentsByNewsId(postId, PageRequest.of(page, size));

        return commentDTOList;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public CommentDTO getCommentById(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @PutMapping("/{id}")
    public CommentDTO updateComment(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        return commentService.updateComment(id, commentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

