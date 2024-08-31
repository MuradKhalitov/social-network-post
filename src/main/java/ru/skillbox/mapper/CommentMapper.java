package ru.skillbox.mapper;
import ru.skillbox.dto.CommentDTO;
import ru.skillbox.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, NewsMapper.class})
public interface CommentMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "post.id", target = "postId")
    CommentDTO convertToDTO(Comment comment);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "postId", target = "post.id")
    Comment convertToEntity(CommentDTO commentDTO);
}
