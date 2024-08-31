package ru.skillbox.mapper;

import ru.skillbox.dto.response.BriefNewsDTO;
import ru.skillbox.model.Post;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class NewsMapperDelegate implements NewsMapper {

    @Autowired
    private NewsMapper delegate;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public BriefNewsDTO convertToBriefDTO(Post post) {
        BriefNewsDTO briefNewsDTO = delegate.convertToBriefDTO(post);
        briefNewsDTO.setUsername(post.getAuthor().getUsername());
        briefNewsDTO.setComments(post.getComments().size());

        return briefNewsDTO;
    }
}

