package ru.skillbox.mapper;

import ru.skillbox.dto.response.BriefPostDTO;
import ru.skillbox.model.Post;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PostMapperDelegate implements PostMapper {

    @Autowired
    private PostMapper delegate;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public BriefPostDTO convertToBriefDTO(Post post) {
        BriefPostDTO briefPostDTO = delegate.convertToBriefDTO(post);
        briefPostDTO.setUsername(post.getAuthor().getUsername());
        briefPostDTO.setComments(post.getComments().size());

        return briefPostDTO;
    }
}
