package ru.skillbox.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.TagDto;
import ru.skillbox.model.Tag;
import ru.skillbox.repository.TagRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {


    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // Метод для поиска тегов по имени
    public List<TagDto> searchTagsByName(String name) {
        List<Tag> tags = tagRepository.findByNameContainingIgnoreCase(name);
        return tags.stream()
                .map(tag -> new TagDto(tag.getName()))
                .collect(Collectors.toList());
    }
}
