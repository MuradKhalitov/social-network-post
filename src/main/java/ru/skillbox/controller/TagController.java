package ru.skillbox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.dto.TagDto;
import ru.skillbox.service.TagService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<TagDto> getTagsByName(@RequestParam String name) {
        return tagService.searchTagsByName(name);
    }
}

