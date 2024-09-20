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

    @Mapping(source = "comments", target = "comments")
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "likes", target = "likeAmount")  // Маппинг от likes к likeAmount
    PostDto convertToDTO(Post post);

    @Mapping(source = "tags", target = "tags")
    PagePostDto.PostContent convertToPostContent(Post post);

    @Mapping(source = "comments", target = "comments")
    @Mapping(source = "tags", target = "tags")
    @Mapping(source = "likeAmount", target = "likes")
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

    default Integer mapComments(List<Comment> comments) {
        return comments.size();
    }


    default int mapLikesToLikeAmount(List<LikePost> likes) {
        return likes != null ? likes.size() : 0;
    }

    default List<LikePost> mapLikeAmountToLikes(int likeAmount) {
        List<LikePost> likes = new ArrayList<>();
        for (int i = 0; i < likeAmount; i++) {
            likes.add(new LikePost());
        }
        return likes;
    }
}


