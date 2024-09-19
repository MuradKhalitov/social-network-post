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
import ru.skillbox.util.CurrentUsers;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class LikeCommentService {

    private final LikeCommentRepository likeCommentRepository;
    private final CommentRepository commentRepository;
    private final LikeCommentMapper likeCommentMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    private final CurrentUsers currentUsers;

    @Autowired
    public LikeCommentService(LikeCommentRepository likeRepository, CommentRepository commentRepository, LikeCommentMapper likeMapper, CommentService commentService, CommentMapper commentMapper, CurrentUsers currentUsers) {
        this.likeCommentRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.likeCommentMapper = likeMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
        this.currentUsers = currentUsers;
    }

    public LikeCommentDto createLikeComment(Long Postid, Long commentId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Optional<LikeComment> existingLike = likeCommentRepository.findByCommentIdAndAuthorId(commentId, currentUserId);
        if (existingLike.isPresent()) {
            return null;
        }

        LikeComment likeComment = new LikeComment();
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + Postid + " not found"));
        comment.setLikeAmount(comment.getLikeAmount() + 1);
        likeComment.setComment(comment);
        likeComment.setAuthorId(currentUserId);
        log.info("Пользователь: {}, добавил like к комментарию: {}", currentUserId, comment.getId());
        return likeCommentMapper.convertToDTO(likeCommentRepository.save(likeComment));
    }

    public void deleteLikeComment(Long commentId) {
        UUID currentUserId = currentUsers.getCurrentUserId();

        Optional<LikeComment> existingLike = likeCommentRepository.findByCommentIdAndAuthorId(commentId, currentUserId);
        if (existingLike.isPresent()) {
            Comment comment = commentRepository.findById(commentId).get();
            comment.setLikeAmount(comment.getLikeAmount() - 1);
            likeCommentRepository.deleteById(existingLike.get().getId());
        }
    }
}
