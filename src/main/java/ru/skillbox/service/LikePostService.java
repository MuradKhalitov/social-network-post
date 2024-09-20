package ru.skillbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.likePost.LikePostDto;
import ru.skillbox.exception.NewsNotFoundException;
import ru.skillbox.mapper.LikePostMapper;
import ru.skillbox.mapper.PostMapper;
import ru.skillbox.model.LikePost;
import ru.skillbox.model.Post;
import ru.skillbox.repository.LikePostRepository;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.util.CurrentUsers;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class LikePostService {

    private final LikePostRepository likePostRepository;
    private final LikePostMapper likePostMapper;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CurrentUsers currentUsers;

    @Autowired
    public LikePostService(LikePostRepository likeRepository, LikePostMapper likeMapper, PostRepository postRepository, PostMapper postMapper, CurrentUsers currentUsers) {
        this.likePostRepository = likeRepository;
        this.likePostMapper = likeMapper;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.currentUsers = currentUsers;
    }

    public LikePostDto createLikePost(Long postId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Optional<LikePost> existingLike = likePostRepository.findByPostIdAndAuthorId(postId, currentUserId);
        if (existingLike.isPresent()) {
            return null;
        }
        LikePost likePost = new LikePost();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NewsNotFoundException("Post with id " + postId + " not found"));;
        post.setLikeAmount(post.getLikeAmount() + 1);
        likePost.setPost(post);
        likePost.setAuthorId(currentUserId);
        log.info("Пользователь: {}, добавил like к посту: {}", currentUserId, post.getId());
        return likePostMapper.convertToDTO(likePostRepository.save(likePost));
    }

    public void deleteLikePost(Long postId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Optional<LikePost> existingLike = likePostRepository.findByPostIdAndAuthorId(postId, currentUserId);
        if (existingLike.isPresent()) {
            Post post = postRepository.findById(postId).get();
            post.setLikeAmount(post.getLikeAmount() - 1);
            likePostRepository.deleteById(existingLike.get().getId());
        }
    }

}
