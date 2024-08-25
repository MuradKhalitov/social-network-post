package ru.skillbox.controller;

import org.springframework.http.HttpStatus;
import ru.skillbox.dto.PostDto;
import ru.skillbox.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getPostsByAuthorId(@RequestParam Long authorId) {
        List<PostDto> posts = postService.getPostsByAuthorId(authorId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        PostDto post = postService.getPostById(id);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        PostDto createdPost = postService.createPost(postDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
        PostDto updatedPost = postService.updatePost(id, postDto);
        return updatedPost != null ? ResponseEntity.ok(updatedPost) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }
}

