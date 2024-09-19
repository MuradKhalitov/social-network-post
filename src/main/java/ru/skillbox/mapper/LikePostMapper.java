package ru.skillbox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.dto.likePost.LikePostDto;
import ru.skillbox.model.LikePost;

@Mapper(componentModel = "spring", uses = {PostMapper.class})
public interface LikePostMapper {

    @Mapping(source = "post.id", target = "postId")
    LikePostDto convertToDTO(LikePost likePost);

    @Mapping(source = "postId", target = "post.id")
    LikePost convertToEntity(LikePostDto likeDto);
}
