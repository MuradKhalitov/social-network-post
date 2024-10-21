package ru.skillbox.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dto.comment.request.CommentDto;
import ru.skillbox.dto.comment.response.PageCommentDto;
import ru.skillbox.exception.*;
import ru.skillbox.mapper.CommentMapper;
import ru.skillbox.model.Comment;
import ru.skillbox.model.Post;
import ru.skillbox.repository.CommentRepository;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.util.CurrentUsers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final CurrentUsers currentUsers;


    @Autowired
    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository, CommentMapper commentMapper, CurrentUsers currentUsers) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
        this.currentUsers = currentUsers;
    }

    public CommentDto createPostComment(Long postId, CommentDto commentDTO) {
        if (commentDTO.getCommentText() == null || commentDTO.getCommentText().trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.EMPTY_COMMENT_TEXT.getMessage());
        }
        UUID currentUserId = currentUsers.getCurrentUserId();
        Comment comment = commentMapper.convertToEntity(commentDTO);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessage.POST_NOT_FOUND.format(postId)));
        comment.setAuthorId(currentUserId);
        comment.setPost(post);
        log.info("Пользователь: {}, добавил комментарий", currentUserId);
        return commentMapper.convertToDTO(commentRepository.save(comment));
    }

    public CommentDto createSubComment(Long postId, CommentDto commentDTO, Long parentCommentId) {
        if (commentDTO.getCommentText() == null || commentDTO.getCommentText().trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.EMPTY_COMMENT_TEXT.getMessage());
        }
        UUID currentUserId = currentUsers.getCurrentUserId();
        Comment comment = commentMapper.convertToEntity(commentDTO);
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.format(parentCommentId)));
        comment.setParent(parentComment);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessage.POST_NOT_FOUND.format(postId)));
        comment.setAuthorId(currentUserId);
        comment.setPost(post);
        log.info("Пользователь: {}, добавил комментарий", currentUserId);
        return commentMapper.convertToDTO(commentRepository.save(comment));
    }

    public PageCommentDto getPostComments(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessage.POST_NOT_FOUND.format(postId)));
        Page<Comment> commentPage = commentRepository.findByPostId(post.getId(), pageable);
        return buildPageCommentDto(commentPage, pageable, true);
    }

    public PageCommentDto getSubComments(Long postId, Pageable pageable) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(ErrorMessage.POST_NOT_FOUND.format(postId)));
        Page<Comment> commentPage = commentRepository.findByPostId(post.getId(), pageable);
        return buildPageCommentDto(commentPage, pageable, false);
    }

    private PageCommentDto buildPageCommentDto(Page<Comment> commentPage, Pageable pageable, boolean isParent) {
        PageCommentDto pageCommentDto = new PageCommentDto();
        pageCommentDto.setTotalElements(commentPage.getTotalElements());
        pageCommentDto.setTotalPages(commentPage.getTotalPages());
        pageCommentDto.setNumber(commentPage.getNumber());
        pageCommentDto.setSize(commentPage.getSize());
        pageCommentDto.setFirst(commentPage.isFirst());
        pageCommentDto.setLast(commentPage.isLast());
        pageCommentDto.setNumberOfElements(commentPage.getNumberOfElements());
        pageCommentDto.setPageable(pageable);
        pageCommentDto.setEmpty(commentPage.isEmpty());

        List<PageCommentDto.CommentContent> comments = commentPage.getContent().stream()
                .filter(comment -> isParent ? comment.getParent() == null : comment.getParent() != null)
                .map(this::convertToCommentContent)
                .toList();

        pageCommentDto.setContent(new ArrayList<>(comments));
        return pageCommentDto;
    }

    private PageCommentDto.CommentContent convertToCommentContent(Comment comment) {
        UUID currentUserId = currentUsers.getCurrentUserId();

        comment.updateLikeAmount();
        boolean isMyLike = comment.getLikes().stream()
                .anyMatch(likeComment -> likeComment.getAuthorId().equals(currentUserId));

        return new PageCommentDto.CommentContent(
                comment.getId(),
                comment.getCommentType(),
                comment.getTime(),
                comment.getTimeChanged(),
                comment.getAuthorId(),
                comment.getParent() != null ? comment.getParent().getId() : 0L,
                comment.getCommentText(),
                comment.getPost().getId(),
                comment.isBlocked(),
                comment.isDeleted(),
                comment.getLikeAmount(),
                isMyLike,
                comment.getSubComments().size(),
                comment.getImagePath()
        );
    }

    @Transactional
    public CommentDto updateComment(Long commentId, CommentDto updatedCommentDto) {
        if (updatedCommentDto.getCommentText() == null || updatedCommentDto.getCommentText().trim().isEmpty()) {
            throw new IllegalArgumentException("Текст комментария не может быть пустым");
        }
        UUID currentUserId = currentUsers.getCurrentUserId();

        Comment updatedComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.format(commentId)));
        UUID updatedCommentAuthor = updatedComment.getAuthorId();

        if (currentUserId.equals(updatedCommentAuthor) || currentUsers.hasRole("ADMIN") || currentUsers.hasRole("MODERATOR")) {
            updatedComment.setCommentText(updatedCommentDto.getCommentText());
            return commentMapper.convertToDTO(commentRepository.save(updatedComment));
        } else {
            throw new AccessDeniedException("У вас нет разрешения на обновление этого комментария");
        }
    }

    public void deleteComment(Long commentId) {
        UUID currentUserId = currentUsers.getCurrentUserId();

        Comment deletedComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(ErrorMessage.COMMENT_NOT_FOUND.format(commentId)));
        UUID deletedCommentAuthor = deletedComment.getAuthorId();
        if (currentUserId.equals(deletedCommentAuthor) || currentUsers.hasRole("ADMIN") || currentUsers.hasRole("MODERATOR")) {
            commentRepository.delete(deletedComment);
        } else {
            throw new AccessDeniedException("У вас нет разрешения на удаление этого комментария");
        }
    }
}

