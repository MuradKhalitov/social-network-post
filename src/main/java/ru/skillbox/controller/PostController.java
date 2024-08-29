package ru.skillbox.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import ru.skillbox.dto.PostDto;
import ru.skillbox.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public List<PostDto> getPostsByAuthorId(@RequestParam Long authorId) {
        return postService.getPostsByAuthorId(authorId);
    }

    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/all")
    public List<PostDto> getAllPost() {
        return postService.getAllPost();
    }

    @PostMapping
    public PostDto createPost(@RequestBody PostDto postDto) {
        return postService.createPost(postDto);
    }

    @PutMapping("/{id}")
    public PostDto updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
        return postService.updatePost(id, postDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

