package ru.skillbox.controller;

import org.springframework.data.domain.Pageable;
import ru.skillbox.dto.PostDto;
import ru.skillbox.dto.SearchDto;
import ru.skillbox.dto.response.PostResponse;
import ru.skillbox.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public PostDto getNewsById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    public PostDto updateNews(@PathVariable Long id, @RequestBody PostDto postDto) {
        return postService.updateNews(id, postDto);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNews(@PathVariable Long id) {
        postService.deleteNews(id);
    }



    @GetMapping()
    public PostResponse searchPosts(SearchDto searchDto, Pageable pageable) {
        return postService.searchPosts(searchDto, pageable);
    }

    @PostMapping
    public PostDto createNews(@RequestBody PostDto postDto) {
        return postService.createNews(postDto);
    }
}






