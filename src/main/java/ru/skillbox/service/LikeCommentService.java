package ru.skillbox.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.dto.LikeCommentDto;
import ru.skillbox.mapper.CommentMapper;
import ru.skillbox.mapper.LikeCommentMapper;
import ru.skillbox.model.*;
import ru.skillbox.repository.LikeCommentRepository;
import ru.skillbox.repository.UserRepository;
import ru.skillbox.util.CurrentUsers;

import java.util.Optional;

@Service
@Slf4j
public class LikeCommentService {

    private final LikeCommentRepository likeCommentRepository;
    private final UserRepository userRepository;
    private final LikeCommentMapper likeCommentMapper;
    private final CommentService commentService;
    private final CommentMapper commentMapper;

    @Autowired
    public LikeCommentService(LikeCommentRepository likeRepository, UserRepository userRepository, LikeCommentMapper likeMapper, CommentService commentService, CommentMapper commentMapper) {
        this.likeCommentRepository = likeRepository;
        this.userRepository = userRepository;
        this.likeCommentMapper = likeMapper;
        this.commentService = commentService;
        this.commentMapper = commentMapper;
    }

    public LikeCommentDto createLikeComment(Long id, Long commentId) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername).get();

        Optional<LikeComment> existingLike = likeCommentRepository.findByCommentIdAndAuthorId(commentId, user.getId());
        if (existingLike.isPresent()) {
            return null;
        }

        LikeComment likeComment = new LikeComment();
        Comment comment = commentMapper.convertToEntity(commentService.getCommentById(commentId));
        likeComment.setComment(comment);
        likeComment.setAuthor(user);
        log.info("Пользователь: {}, добавил like к комментарию: {}", currentUsername, comment.getId());
        return likeCommentMapper.convertToDTO(likeCommentRepository.save(likeComment));
    }

    public ResponseEntity<Void> deleteLike(Long commentId) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername).get();

        Optional<LikeComment> existingLike = likeCommentRepository.findByCommentIdAndAuthorId(commentId, user.getId());
        if (existingLike.isPresent()) {
            likeCommentRepository.deleteById(existingLike.get().getId());
        }

        return null;
    }

}
