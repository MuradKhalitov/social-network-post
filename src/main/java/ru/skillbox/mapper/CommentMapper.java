package ru.skillbox.mapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.dto.comment.request.CommentDto;
import ru.skillbox.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.repository.CommentRepository;

@Mapper(componentModel = "spring", uses = {PostMapper.class, LikeCommentMapper.class})
public abstract class CommentMapper {

    @Autowired
    private CommentRepository commentRepository;

    @Mapping(source = "post.id", target = "postId")
    @Mapping(source = "subComments", target = "subComments")
    @Mapping(target = "parentId", source = "parent.id")  // Маппинг parent.id в parentId
    @Mapping(source = "likes", target = "likes")         // Маппинг списка лайков
    public abstract CommentDto convertToDTO(Comment comment);

    @Mapping(source = "postId", target = "post.id")
    @Mapping(source = "subComments", target = "subComments")
    @Mapping(target = "parent", source = "parentId")      // Маппинг parentId в parent
    @Mapping(source = "likes", target = "likes")          // Обратный маппинг для лайков
    public abstract Comment convertToEntity(CommentDto commentDTO);

    // Метод для маппинга parentId в Comment
    Comment map(Long parentId) {
        return parentId == null ? null : commentRepository.findById(parentId).orElse(null);
    }
}


