package ru.skillbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.likePost.LikePostDto;
import ru.skillbox.mapper.LikePostMapper;
import ru.skillbox.mapper.PostMapper;
import ru.skillbox.model.Account;
import ru.skillbox.model.LikePost;
import ru.skillbox.model.Post;
import ru.skillbox.repository.LikePostRepository;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.repository.AccountRepository;
import ru.skillbox.util.CurrentUsers;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class LikePostService {

    private final LikePostRepository likePostRepository;
    private final AccountRepository accountRepository;
    private final LikePostMapper likePostMapper;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CurrentUsers currentUsers;

    @Autowired
    public LikePostService(LikePostRepository likeRepository, AccountRepository accountRepository, LikePostMapper likeMapper, PostRepository postRepository, PostMapper postMapper, CurrentUsers currentUsers) {
        this.likePostRepository = likeRepository;
        this.accountRepository = accountRepository;
        this.likePostMapper = likeMapper;
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.currentUsers = currentUsers;
    }

    public LikePostDto createLikePost(Long postId) {
        UUID userId = currentUsers.getCurrentUserId();
        Account account = accountRepository.findById(userId).get();


        Optional<LikePost> existingLike = likePostRepository.findByPostIdAndAuthorId(postId, account.getId());
        if (existingLike.isPresent()) {
            return null;
        }

        LikePost likePost = new LikePost();
        Post post = postRepository.findById(postId).get();
        post.setLikeAmount(post.getLikeAmount() + 1);
        likePost.setPost(post);
        likePost.setAuthor(account);
        log.info("Пользователь: {}, добавил like к посту: {}", account.getEmail(), post.getId());
        return likePostMapper.convertToDTO(likePostRepository.save(likePost));
    }

    public void deleteLikePost(Long postId) {
        UUID userId = currentUsers.getCurrentUserId();
        Account account = accountRepository.findById(userId).get();

        Optional<LikePost> existingLike = likePostRepository.findByPostIdAndAuthorId(postId, account.getId());
        if (existingLike.isPresent()) {
            Post post = postRepository.findById(postId).get();
            post.setLikeAmount(post.getLikeAmount() - 1);
            likePostRepository.deleteById(existingLike.get().getId());
        }
    }

}
