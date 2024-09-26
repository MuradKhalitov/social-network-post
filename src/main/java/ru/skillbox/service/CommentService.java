package ru.skillbox.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dto.comment.request.CommentDto;
import ru.skillbox.dto.comment.response.PageCommentDto;
import ru.skillbox.exception.AccessDeniedException;
import ru.skillbox.exception.CommentNotFoundException;
import ru.skillbox.exception.PostNotFoundException;
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
import java.util.stream.Collectors;

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
        UUID currentUserId = currentUsers.getCurrentUserId();
        Comment comment = commentMapper.convertToEntity(commentDTO);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));
        comment.setAuthorId(currentUserId);
        comment.setPost(post);
        log.info("Пользователь: {}, добавил комментарий", currentUserId);
        return commentMapper.convertToDTO(commentRepository.save(comment));
    }

    public CommentDto createSubComment(Long postId, CommentDto commentDTO, Long parentCommentId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Comment comment = commentMapper.convertToEntity(commentDTO);
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new RuntimeException("Parent comment not found"));
        comment.setParent(parentComment);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post with id " + postId + " not found"));
        comment.setAuthorId(currentUserId);
        comment.setPost(post);
        log.info("Пользователь: {}, добавил комментарий", currentUserId);
        return commentMapper.convertToDTO(commentRepository.save(comment));
    }
    public PageCommentDto getPostComments(Long postId, Pageable pageable) {

        // Получаем все комментарии поста с сортировкой по времени
        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

        // Формируем объект PageCommentDto
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

        // Отделяем родительские комментарии от субкомментариев
        List<PageCommentDto.CommentContent> parentComments = commentPage.getContent().stream()
                .filter(comment -> comment.getParent() == null)  // только родительские
                .map(comment -> convertToCommentContent(comment))
                .collect(Collectors.toList());

        // Собираем итоговый список комментариев
        List<PageCommentDto.CommentContent> allComments = new ArrayList<>(parentComments);

        pageCommentDto.setContent(allComments);

        return pageCommentDto;
    }
    public PageCommentDto getSubComments(Long postId, Pageable pageable) {

        // Получаем все комментарии поста с сортировкой по времени
        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

        // Формируем объект PageCommentDto
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

        List<PageCommentDto.CommentContent> subComments = commentPage.getContent().stream()
                .filter(comment -> comment.getParent() != null)  // только субкомментарии
                .map(comment -> convertToCommentContent(comment))
                .collect(Collectors.toList());

        // Собираем итоговый список комментариев
        List<PageCommentDto.CommentContent> allComments = new ArrayList<>(subComments);

        pageCommentDto.setContent(allComments);

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


//    public PageCommentDto getPostComments(Long postId, Pageable pageable) {
//        UUID currentUserId = currentUsers.getCurrentUserId();
//        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);
//
//        // Формирование PageCommentDto
//        PageCommentDto pageCommentDto = new PageCommentDto();
//        pageCommentDto.setTotalElements(commentPage.getTotalElements());
//        pageCommentDto.setTotalPages(commentPage.getTotalPages());
//        pageCommentDto.setNumber(commentPage.getNumber());
//        pageCommentDto.setSize(commentPage.getSize());
//        pageCommentDto.setFirst(commentPage.isFirst());
//        pageCommentDto.setLast(commentPage.isLast());
//        pageCommentDto.setNumberOfElements(commentPage.getNumberOfElements());
//        pageCommentDto.setPageable(pageable);
//        pageCommentDto.setEmpty(commentPage.isEmpty());
//
//        // Маппинг Comment в CommentContent
//        List<PageCommentDto.CommentContent> content = commentPage.getContent().stream().map(comment -> {
//            comment.updateLikeAmount();
//            boolean isMyLike = comment.getLikes().stream()
//                    .anyMatch(likeComment -> likeComment.getAuthorId().equals(currentUserId));
//
//            return new PageCommentDto.CommentContent(
//                    comment.getId(),
//                    comment.getCommentType(),
//                    comment.getTime(),
//                    comment.getTimeChanged(),
//                    comment.getAuthorId(),
//                    comment.getParent() != null ? comment.getParent().getId() : 0L,
//                    comment.getCommentText(),
//                    comment.getPost().getId(),
//                    comment.isBlocked(),
//                    comment.isDeleted(),
//                    comment.getLikeAmount(),
//                    isMyLike,
//                    comment.getSubComments().size(),
//                    comment.getImagePath()
//            );
//        }).collect(Collectors.toList());
//
//        pageCommentDto.setContent(content);
//        return pageCommentDto;
//    }
//
//
//    public PageCommentDto getSubComments(Long postId, Long commentId, Pageable pageable) {
//        UUID currentUserId = currentUsers.getCurrentUserId();
//        Page<Comment> commentPage = commentRepository.findById(commentId, pageable);
//
//        // Формирование PageCommentDto
//        PageCommentDto pageCommentDto = new PageCommentDto();
//        pageCommentDto.setTotalElements(commentPage.getTotalElements());
//        pageCommentDto.setTotalPages(commentPage.getTotalPages());
//        pageCommentDto.setNumber(commentPage.getNumber());
//        pageCommentDto.setSize(commentPage.getSize());
//        pageCommentDto.setFirst(commentPage.isFirst());
//        pageCommentDto.setLast(commentPage.isLast());
//        pageCommentDto.setNumberOfElements(commentPage.getNumberOfElements());
//        pageCommentDto.setPageable(pageable);
//        pageCommentDto.setEmpty(commentPage.isEmpty());
//
//        // Маппинг Comment в CommentContent
//        List<PageCommentDto.CommentContent> content = commentPage.getContent().stream().map(comment -> {
//            comment.updateLikeAmount();
//            boolean isMyLike = comment.getLikes().stream()
//                    .anyMatch(likeComment -> likeComment.getAuthorId().equals(currentUserId));
//
//            return new PageCommentDto.CommentContent(
//                    comment.getId(),
//                    comment.getCommentType(),
//                    comment.getTime(),
//                    comment.getTimeChanged(),
//                    comment.getAuthorId(),
//                    comment.getParent() != null ? comment.getParent().getId() : 0L,
//                    comment.getCommentText(),
//                    comment.getPost().getId(),
//                    comment.isBlocked(),
//                    comment.isDeleted(),
//                    comment.getLikeAmount(),
//                    isMyLike,
//                    comment.getSubComments().size(),
//                    comment.getImagePath()
//            );
//        }).collect(Collectors.toList());
//
//        pageCommentDto.setContent(content);
//        return pageCommentDto;
//    }

    @Transactional
    public CommentDto updateComment(Long postId, Long commentId, CommentDto updatedCommentDto) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Comment updatedComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " not found"));
        UUID updatedCommentAuthor = updatedComment.getAuthorId();

        if (currentUserId.equals(updatedCommentAuthor) || currentUsers.hasRole("ADMIN") || currentUsers.hasRole("MODERATOR")) {
            updatedComment.setCommentText(updatedCommentDto.getCommentText());
            return commentMapper.convertToDTO(commentRepository.save(updatedComment));
        } else {
            throw new AccessDeniedException("У вас нет разрешения на обновление этого комментария");
        }
    }

    public void deleteComment(Long postId, Long commentId) {
        UUID currentUserId = currentUsers.getCurrentUserId();
        Comment deletedComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + commentId + " not found"));
        UUID deletedCommentAuthor = deletedComment.getAuthorId();
        if (currentUserId.equals(deletedCommentAuthor) || currentUsers.hasRole("ADMIN") || currentUsers.hasRole("MODERATOR")) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException("Post with postId " + postId + "not found"));
            // post.setCommentsCount(post.getCommentsCount() - 1);
            commentRepository.deleteById(commentId);
        } else {
            throw new AccessDeniedException("У вас нет разрешения на удаление этого комментария");
        }
    }
}

