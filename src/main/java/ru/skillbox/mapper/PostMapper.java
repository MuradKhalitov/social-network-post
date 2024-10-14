package ru.skillbox.mapper;

import ru.skillbox.dto.TagDto;
import ru.skillbox.dto.post.request.PostDto;
import ru.skillbox.dto.post.response.PagePostDto;
import ru.skillbox.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.model.Tag;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface PostMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "tags", target = "tags")
    PostDto convertToDTO(Post post);

    @Mapping(source = "tags", target = "tags")
    PagePostDto.PostContent convertToPostContent(Post post);

    @Mapping(source = "tags", target = "tags")
    Post convertToEntity(PostDto postDto);

    default List<TagDto> mapTagsToTagDtos(List<Tag> tags) {
        return tags.stream()
                .map(tag -> {
                    TagDto tagDto = new TagDto();
                    tagDto.setName(tag.getName());
                    return tagDto;
                })
                .collect(Collectors.toList());
    }

    default List<Tag> mapTagDtosToTags(List<TagDto> tagDtos) {
        return tagDtos.stream()
                .map(tagDto -> {
                    Tag tag = new Tag();
                    tag.setName(tagDto.getName());
                    return tag;
                })
                .collect(Collectors.toList());
    }

    default List<String> mapTagsToStrings(List<Tag> tags) {
        return tags.stream()
                .map(Tag::getName)
                .toList();
    }
    default List<Tag> mapStringsToTags(List<String> tagNames) {
        return tagNames.stream()
                .map(tagName -> {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    return tag;
                })
                .toList();
    }
}


