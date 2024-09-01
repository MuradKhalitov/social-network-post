package ru.skillbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.LikePostDto;
import ru.skillbox.mapper.LikePostMapper;
import ru.skillbox.mapper.NewsMapper;
import ru.skillbox.model.LikePost;
import ru.skillbox.model.Post;
import ru.skillbox.model.User;
import ru.skillbox.repository.LikePostRepository;
import ru.skillbox.repository.UserRepository;
import ru.skillbox.util.CurrentUsers;

import java.util.Optional;

@Service
@Slf4j
public class LikePostService {

    private final LikePostRepository likePostRepository;
    private final UserRepository userRepository;
    private final LikePostMapper likePostMapper;
    private final NewsService newsService;
    private final NewsMapper newsMapper;

    @Autowired
    public LikePostService(LikePostRepository likeRepository, UserRepository userRepository, LikePostMapper likeMapper, NewsService newsService, NewsMapper newsMapper) {
        this.likePostRepository = likeRepository;
        this.userRepository = userRepository;
        this.likePostMapper = likeMapper;
        this.newsService = newsService;
        this.newsMapper = newsMapper;
    }

    public LikePostDto createLikePost(Long postId) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername).get();


        Optional<LikePost> existingLike = likePostRepository.findByPostIdAndAuthorId(postId, user.getId());
        if (existingLike.isPresent()) {
            return null;
        }

        LikePost likePost = new LikePost();
        Post post = newsMapper.convertToEntity(newsService.getNewsById(postId));
        likePost.setPost(post);
        likePost.setAuthor(user);
        log.info("Пользователь: {}, добавил like к посту: {}", currentUsername, post.getId());
        return likePostMapper.convertToDTO(likePostRepository.save(likePost));
    }

    public ResponseEntity<Void> deleteLike(Long postId) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername).get();

        Optional<LikePost> existingLike = likePostRepository.findByPostIdAndAuthorId(postId, user.getId());
        if (existingLike.isPresent()) {
            likePostRepository.deleteById(existingLike.get().getId());
        }

        return null;
    }

}