package ru.skillbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.LikePostDto;
import ru.skillbox.mapper.LikePostMapper;
import ru.skillbox.mapper.PostMapper;
import ru.skillbox.model.LikePost;
import ru.skillbox.model.Post;
import ru.skillbox.model.User;
import ru.skillbox.repository.LikePostRepository;
import ru.skillbox.repository.NewsRepository;
import ru.skillbox.repository.UserRepository;
import ru.skillbox.util.CurrentUsers;

import java.util.Optional;

@Service
@Slf4j
public class LikePostService {

    private final LikePostRepository likePostRepository;
    private final UserRepository userRepository;
    private final LikePostMapper likePostMapper;
    private final NewsRepository newsRepository;
    private final PostMapper postMapper;

    @Autowired
    public LikePostService(LikePostRepository likeRepository, UserRepository userRepository, LikePostMapper likeMapper, NewsRepository newsRepository, PostMapper postMapper) {
        this.likePostRepository = likeRepository;
        this.userRepository = userRepository;
        this.likePostMapper = likeMapper;
        this.newsRepository = newsRepository;
        this.postMapper = postMapper;
    }

    public LikePostDto createLikePost(Long postId) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Optional<LikePost> existingLike = likePostRepository.findByPostIdAndAuthorId(postId, user.getId());
        if (existingLike.isPresent()) {
            return null;
        }

        LikePost likePost = new LikePost();
        Optional<Post> post = newsRepository.findById(postId);
        likePost.setPost(post.get());
        likePost.setAuthor(user);
        log.info("Пользователь: {}, добавил like к посту: {}", currentUsername, post.get().getId());
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
