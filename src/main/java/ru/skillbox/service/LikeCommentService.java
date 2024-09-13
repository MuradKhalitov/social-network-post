package ru.skillbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.likeComment.LikeCommentDto;
import ru.skillbox.exception.CommentNotFoundException;
import ru.skillbox.mapper.CommentMapper;
import ru.skillbox.mapper.LikeCommentMapper;
import ru.skillbox.model.*;
import ru.skillbox.repository.CommentRepository;
import ru.skillbox.repository.LikeCommentRepository;
import ru.skillbox.repository.UserRepository;
import ru.skillbox.util.CurrentUsers;

import java.util.Optional;

@Service
@Slf4j
public class LikeCommentService {

    private final LikeCommentRepository likeCommentRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeCommentMapper likeCommentMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final CurrentUsers currentUsers;

    @Autowired
    public LikeCommentService(LikeCommentRepository likeRepository, UserRepository userRepository, CommentRepository commentRepository, LikeCommentMapper likeMapper, CommentService commentService, CommentMapper commentMapper, CurrentUsers currentUsers) {
        this.likeCommentRepository = likeRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.likeCommentMapper = likeMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.currentUsers = currentUsers;
    }

    public LikeCommentDto createLikeComment(Long Postid, Long commentId) {
        Long userId = currentUsers.getCurrentUserId();
        Account account = userRepository.findById(userId).get();

        Optional<LikeComment> existingLike = likeCommentRepository.findByCommentIdAndAuthorId(commentId, account.getId());
        if (existingLike.isPresent()) {
            return null;
        }

        LikeComment likeComment = new LikeComment();
        //Comment comment = commentMapper.convertToEntity(commentService.getCommentById(commentId));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + Postid + " not found"));
        comment.setLikeAmount(comment.getLikeAmount() + 1);
        likeComment.setComment(comment);
        likeComment.setAuthor(account);
        log.info("Пользователь: {}, добавил like к комментарию: {}", account.getEmail(), comment.getId());
        return likeCommentMapper.convertToDTO(likeCommentRepository.save(likeComment));
    }

    public void deleteLikeComment(Long commentId) {
        Long userId = currentUsers.getCurrentUserId();
        Account account = userRepository.findById(userId).get();

        Optional<LikeComment> existingLike = likeCommentRepository.findByCommentIdAndAuthorId(commentId, account.getId());
        if (existingLike.isPresent()) {
            Comment comment = commentRepository.findById(commentId).get();
            comment.setLikeAmount(comment.getLikeAmount() - 1);
            likeCommentRepository.deleteById(existingLike.get().getId());
        }
    }
}
