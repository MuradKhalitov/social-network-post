package ru.skillbox.mapper;
import ru.skillbox.dto.PostDto;
import ru.skillbox.dto.response.BriefNewsDTO;
import ru.skillbox.model.Comment;
import ru.skillbox.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface NewsMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "comments", target = "comments")
    PostDto convertToDTO(Post post);
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "comments", target = "comments")
    BriefNewsDTO convertToBriefDTO(Post post);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "comments", target = "comments")
    Post convertToEntity(PostDto postDto);
    default Integer mapComments(List<Comment> comments) {
        return comments.size();
    }
}
