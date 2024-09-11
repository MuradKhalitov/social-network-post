package ru.skillbox.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.skillbox.dto.comment.request.CommentDto;
import ru.skillbox.dto.comment.response.PageCommentDto;
import ru.skillbox.dto.post.request.PostSearchDto;
import ru.skillbox.dto.post.response.PagePostDto;
import ru.skillbox.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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


    @PostMapping
    public CommentDto createComment(@PathVariable Long postId, @RequestBody CommentDto commentDTO,
                                    @RequestParam(required = false) Long parentCommentId) {
        return commentService.createComment(postId, commentDTO, parentCommentId);
    }

    @PostMapping("/{commentId}/subcomment")
    public CommentDto createReplyComment(@PathVariable Long postId, @RequestBody CommentDto commentDTO,
                                         @PathVariable Long commentId) {
        return commentService.createComment(postId, commentDTO, commentId);
    }


    @GetMapping
    public PageCommentDto getCommentsByPostId(@PathVariable Long postId,
                                              @RequestParam(required = false, defaultValue = "0") int page,
                                              @RequestParam(required = false, defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id,asc") String sort) {

        String[] sortParams = sort.split("\\s*,\\s*");
        String field = sortParams[0];
        String direction = (sortParams.length > 1) ? sortParams[1] : "asc";

        Sort sorting = Sort.by(new Sort.Order(Sort.Direction.fromString(direction), field));

        Pageable pageable = PageRequest.of(page, size, sorting);

        return commentService.getComments(postId, pageable);


        //return commentService.getCommentsByNewsId(postId, PageRequest.of(page, size));
    }

//    @GetMapping()
//    public PagePostDto searchPosts(
//            PostSearchDto postSearchDto,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id,asc") String sort
//    ) {
//        String[] sortParams = sort.split("\\s*,\\s*");
//        String field = sortParams[0];
//        String direction = (sortParams.length > 1) ? sortParams[1] : "asc";
//
//        Sort sorting = Sort.by(new Sort.Order(Sort.Direction.fromString(direction), field));
//
//        Pageable pageable = PageRequest.of(page, size, sorting);
//
//        return postService.searchPosts(postSearchDto, pageable);
//    }

    @GetMapping("/{id}")
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

