package ru.skillbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.exception.CommentNotFoundException;
import ru.skillbox.mapper.CommentMapper;
import ru.skillbox.model.*;
import ru.skillbox.repository.CommentRepository;
import ru.skillbox.repository.LikeCommentRepository;
import ru.skillbox.util.CurrentUsers;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class LikeCommentService {

    private final LikeCommentRepository likeCommentRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final CurrentUsers currentUsers;

    @Autowired
    public LikeCommentService(LikeCommentRepository likeRepository, CommentRepository commentRepository, CommentService commentService, CommentMapper commentMapper, CurrentUsers currentUsers) {
        this.likeCommentRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.currentUsers = currentUsers;
    }

    public void createLikeComment(Long commentId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " not found"));
        Optional<LikeComment> existingLike = likeCommentRepository.findByCommentIdAndAuthorId(commentId, currentUserId);
        if (!existingLike.isPresent()) {
            LikeComment likeComment = new LikeComment();
            likeComment.setComment(comment);
            likeComment.setAuthorId(currentUserId);
            log.info("Пользователь: {}, добавил like к комментарию: {}", currentUserId, comment.getId());
            likeCommentRepository.save(likeComment);
        }
    }

    public void deleteLikeComment(Long commentId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " not found"));
        Optional<LikeComment> existingLike = likeCommentRepository.findByCommentIdAndAuthorId(commentId, currentUserId);
        if (existingLike.isPresent()) {
            likeCommentRepository.deleteById(existingLike.get().getId())
                    ;
        }
    }
}
