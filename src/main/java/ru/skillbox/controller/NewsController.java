package ru.skillbox.controller;

import ru.skillbox.aop.Autorizator;
import ru.skillbox.dto.PostDto;
import ru.skillbox.dto.response.BriefPostDTO;
import ru.skillbox.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/post")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping
    public List<BriefPostDTO> getFilteredNewsByAuthor(@RequestParam(required = false) Long authorIds) {
        return newsService.getFilteredNewsByAuthor(authorIds);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public PostDto createNews(@RequestBody PostDto postDto) {
        return newsService.createNews(postDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public List<BriefPostDTO> getAllNews(@RequestParam(required = false, defaultValue = "0") int page,
                                         @RequestParam(required = false, defaultValue = "10") int size) {
        //Sort sort = Utils.sort(sortBy, orderBy);
        return newsService.getAllNews(PageRequest.of(page, size));//, sort);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER') || hasAuthority('MODERATOR')")
    public PostDto getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id);
    }

    @PutMapping("/{id}")
    public PostDto updateNews(@PathVariable Long id, @RequestBody PostDto postDto) {
        return newsService.updateNews(id, postDto);
    }

    @DeleteMapping("/{id}")
    @Autorizator
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
    }
}


