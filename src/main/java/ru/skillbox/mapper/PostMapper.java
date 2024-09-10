package ru.skillbox.mapper;

import ru.skillbox.dto.post.request.PostDto;
import ru.skillbox.dto.post.response.PagePostDto;
import ru.skillbox.model.Comment;
import ru.skillbox.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.model.Tag;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface PostMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "comments", target = "comments")
    @Mapping(source = "tags", target = "tags")
    PostDto convertToDTO(Post post);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "tags", target = "tags")
    PagePostDto.PostContent convertToPostContent(Post post);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "comments", target = "comments")
    @Mapping(source = "tags", target = "tags")
    Post convertToEntity(PostDto postDto);

    // Маппинг List<Tag> -> List<String>
    default List<String> mapTagsToStrings(List<Tag> tags) {
        return tags.stream()
                .map(Tag::getName)
                .toList();
    }

    // Маппинг List<String> -> List<Tag>
    default List<Tag> mapStringsToTags(List<String> tagNames) {
        return tagNames.stream()
                .map(tagName -> {
                    Tag tag = new Tag();
                    tag.setName(tagName);
                    return tag;
                })
                .toList();
    }

    default Integer mapComments(List<Comment> comments) {
        return comments.size();
    }
}
