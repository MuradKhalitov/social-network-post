package ru.skillbox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.dto.likeComment.LikeCommentDto;
import ru.skillbox.model.LikeComment;

@Mapper(componentModel = "spring", uses = {CommentMapper.class})
public interface LikeCommentMapper {

    @Mapping(source = "comment.id", target = "commentId")
    LikeCommentDto convertToDTO(LikeComment likeComment);

    @Mapping(source = "commentId", target = "comment.id")
    LikeComment convertToEntity(LikeCommentDto likeCommentDto);
}
