package ru.skillbox.mapper;

import ru.skillbox.dto.post.request.PostDto;
import ru.skillbox.dto.post.response.PagePostDto;
import ru.skillbox.model.Comment;
import ru.skillbox.model.LikePost;
import ru.skillbox.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.model.Tag;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface PostMapper {

    @Mapping(source = "tags", target = "tags")
    PostDto convertToDTO(Post post);

    @Mapping(source = "tags", target = "tags")
    PagePostDto.PostContent convertToPostContent(Post post);

    @Mapping(source = "tags", target = "tags")
    Post convertToEntity(PostDto postDto);

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


