package ru.skillbox.mapper;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.dto.comment.request.CommentDto;
import ru.skillbox.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.repository.CommentRepository;

@Mapper(componentModel = "spring", uses = {PostMapper.class})
public abstract class CommentMapper {
    @Autowired
    private CommentRepository commentRepository;

    @Mapping(source = "post.id", target = "postId")
    @Mapping(target = "parentId", source = "parent.id")
    public abstract CommentDto convertToDTO(Comment comment);


    @Mapping(source = "postId", target = "post.id")
    @Mapping(target = "parent", source = "parentId")
    public abstract Comment convertToEntity(CommentDto commentDTO);

    Comment map(Long parentId) {
        return parentId == null ? null : commentRepository.findById(parentId).orElse(null);
    }
}


