package ru.skillbox.controller;

import ru.skillbox.dto.PostDto;
import ru.skillbox.dto.response.BriefPostDTO;
import ru.skillbox.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public List<BriefPostDTO> getFilteredNewsByAuthor(@RequestParam(required = false) Long authorIds) {
        return postService.getFilteredNewsByAuthor(authorIds);
    }

    @PostMapping
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public PostDto createNews(@RequestBody PostDto postDto) {
        return postService.createNews(postDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public List<BriefPostDTO> getAllNews(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        //Sort sort = Utils.sort(sortBy, orderBy);
        return postService.getAllNews(PageRequest.of(page, size));//, sort);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
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
}


