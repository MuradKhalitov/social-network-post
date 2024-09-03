package ru.skillbox.mapper;
import ru.skillbox.dto.CommentDto;
import ru.skillbox.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, PostMapper.class})
public interface CommentMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "subComments", target = "subComments")
    CommentDto convertToDTO(Comment comment);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "postId", target = "post.id")
    @Mapping(source = "subComments", target = "subComments")
    Comment convertToEntity(CommentDto commentDTO);
}
