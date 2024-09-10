package ru.skillbox.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.dto.post.response.PagePostDto;
import ru.skillbox.model.Post;

public abstract class NewsMapperDelegate implements PostMapper {

    @Autowired
    private PostMapper delegate;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PagePostDto.PostContent convertToPostContent(Post post) {
        PagePostDto.PostContent postContent = delegate.convertToPostContent(post);
        //postContent.setUsername(news.getAuthor().getUsername());
        postContent.setCommentsCount(post.getComments().size());

        return postContent;
    }
}

