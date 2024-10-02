package ru.skillbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.likePost.AddReactionDto;
import ru.skillbox.exception.PostNotFoundException;
import ru.skillbox.mapper.LikePostMapper;
import ru.skillbox.mapper.PostMapper;
import ru.skillbox.model.LikePost;
import ru.skillbox.model.Post;
import ru.skillbox.repository.LikePostRepository;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.util.CurrentUsers;

import java.time.LocalDateTime;
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

public void createLikePost(Long postId, AddReactionDto addReactionDto) {
    UUID currentUserId = currentUsers.getCurrentUserId();

    Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));

    Optional<LikePost> existingLike = likePostRepository.findByPostIdAndAuthorId(postId, currentUserId);

    if (existingLike.isPresent()) {
        LikePost oldLike = existingLike.get();

        if (oldLike.getReactionType().equals(addReactionDto.getReactionType())) {
            likePostRepository.delete(oldLike); // Удаляем лайк
            log.info("Пользователь: {}, удалил like с поста: {}", currentUserId, postId);
        } else {
            oldLike.setReactionType(addReactionDto.getReactionType());
            likePostRepository.save(oldLike);
            log.info("Пользователь: {}, изменил реакцию на пост: {}", currentUserId, postId);
        }
    } else {
        LikePost likePost = new LikePost();
        likePost.setPost(post);
        likePost.setAuthorId(currentUserId);
        likePost.setReactionType(addReactionDto.getReactionType());
        likePost.setCreatedAt(LocalDateTime.now());
        likePostRepository.save(likePost);
        log.info("Пользователь: {}, добавил новую реакцию к посту: {}", currentUserId, postId);
    }
}

    public void deleteLikePost(Long postId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Optional<LikePost> existingLike = likePostRepository.findByPostIdAndAuthorId(postId, currentUserId);
        if (existingLike.isPresent()) {
            Post post = postRepository.findById(postId).get();
            likePostRepository.deleteById(existingLike.get().getId());
        }

    }

}
