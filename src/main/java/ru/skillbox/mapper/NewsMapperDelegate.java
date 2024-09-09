package ru.skillbox.mapper;


import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.dto.response.PostResponse;
import ru.skillbox.model.Post;

public abstract class NewsMapperDelegate implements PostMapper {

    @Autowired
    private PostMapper delegate;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public PostResponse.PostContent convertToPostContent(Post post) {
        PostResponse.PostContent postContent = delegate.convertToPostContent(post);
        //postContent.setUsername(news.getAuthor().getUsername());
        postContent.setCommentsCount(post.getComments().size());

        return postContent;
    }
}

