package ru.skillbox.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.dto.LikePostDto;
import ru.skillbox.model.LikePost;

@Mapper(componentModel = "spring", uses = {UserMapper.class, NewsMapper.class})
public interface LikePostMapper {

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "post.id", target = "postId")
    LikePostDto convertToDTO(LikePost likePost);

    @Mapping(source = "authorId", target = "author.id")
    @Mapping(source = "postId", target = "post.id")
    LikePost convertToEntity(LikePostDto likeDto);
}
