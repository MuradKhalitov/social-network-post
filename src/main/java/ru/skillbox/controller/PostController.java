package ru.skillbox.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.skillbox.dto.post.request.PostDto;
import ru.skillbox.dto.post.request.PostSearchDto;
import ru.skillbox.dto.post.response.PagePostDto;
import ru.skillbox.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @CrossOrigin(origins = "http://localhost:8080")
    public PagePostDto searchPosts(
            PostSearchDto postSearchDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) Boolean withFriends,
            @RequestParam(defaultValue = "false") Boolean isDeleted
    ) {
        postSearchDto.setWithFriends(withFriends);
        postSearchDto.setIsDeleted(isDeleted);

        String[] sortParams = sort.split("\\s*,\\s*");
        String field = sortParams[0];
        String direction = (sortParams.length > 1) ? sortParams[1] : "asc";

        Sort sorting = Sort.by(new Sort.Order(Sort.Direction.fromString(direction), field));

        Pageable pageable = PageRequest.of(page, size, sorting);

        return postService.searchPosts(postSearchDto, pageable);
    }

    @PostMapping
    public PostDto createNews(@RequestBody PostDto postDto) {
        return postService.createNews(postDto);
    }
}






