package ru.skillbox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.dto.likeComment.LikeCommentDto;
import ru.skillbox.model.LikeComment;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface LikeCommentMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "comment.id", target = "commentId")
    LikeCommentDto convertToDTO(LikeComment likeComment);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "commentId", target = "comment.id")
    LikeComment convertToEntity(LikeCommentDto likeCommentDto);
}
