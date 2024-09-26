package ru.skillbox.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.skillbox.dto.comment.request.CommentDto;
import ru.skillbox.dto.comment.response.PageCommentDto;
import ru.skillbox.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post/{id}/comment")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createPostComment(@PathVariable Long id, @RequestBody CommentDto commentDTO) {
        return commentService.createPostComment(id, commentDTO);
    }

    @PostMapping("/{commentId}/subcomment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createSubComment(@PathVariable Long id, @PathVariable Long parentCommentId,
                                       @RequestBody CommentDto commentDTO) {
        return commentService.createSubComment(id, commentDTO, parentCommentId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageCommentDto getCommentsByPostId(@PathVariable Long id,
                                              @RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id,asc") String sort) {

        String[] sortParams = sort.split("\\s*,\\s*");
        String field = sortParams[0];
        String direction = (sortParams.length > 1) ? sortParams[1] : "asc";

        Sort sorting = Sort.by(new Sort.Order(Sort.Direction.fromString(direction), field));

        Pageable pageable = PageRequest.of(page, size, sorting);

        return commentService.getPostComments(id, pageable);
    }

    @GetMapping("/{commentId}/subcomment")
    @ResponseStatus(HttpStatus.OK)
    public PageCommentDto getSubCommentById(@PathVariable Long id,
                                        @PathVariable Long commentId,
                                        @RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "10") int size,
                                        @RequestParam(defaultValue = "id,asc") String sort) {
        String[] sortParams = sort.split("\\s*,\\s*");
        String field = sortParams[0];
        String direction = (sortParams.length > 1) ? sortParams[1] : "asc";

        Sort sorting = Sort.by(new Sort.Order(Sort.Direction.fromString(direction), field));

        Pageable pageable = PageRequest.of(page, size, sorting);
        return commentService.getSubComments(id, pageable);
    }

    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto updateComment(@PathVariable Long id, @PathVariable Long commentId, @RequestBody CommentDto commentDTO) {
        return commentService.updateComment(id, commentId, commentDTO);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@PathVariable Long id, @PathVariable Long commentId) {
        commentService.deleteComment(id, commentId);
    }
}

