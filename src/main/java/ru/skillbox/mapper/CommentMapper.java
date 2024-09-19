package ru.skillbox.mapper;
import ru.skillbox.dto.comment.request.CommentDto;
import ru.skillbox.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PostMapper.class})
public interface CommentMapper {

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "subComments", target = "subComments")
    CommentDto convertToDTO(Comment comment);

    @Mapping(source = "postId", target = "post.id")
    @Mapping(source = "subComments", target = "subComments")
    Comment convertToEntity(CommentDto commentDTO);
}
