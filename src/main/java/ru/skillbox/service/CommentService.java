package ru.skillbox.service;

import ru.skillbox.dto.CommentDto;
import ru.skillbox.model.Comment;
import ru.skillbox.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<CommentDto> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public CommentDto getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.map(this::convertToDto).orElse(null);
    }

    public CommentDto createComment(CommentDto commentDto) {
        commentDto.setTime(LocalDateTime.now());
        commentDto.setTimeChanged(LocalDateTime.now());
        Comment comment = convertToEntity(commentDto);

        comment = commentRepository.save(comment);
        return convertToDto(comment);
    }

    public CommentDto updateComment(Long id, CommentDto commentDto) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setCommentText(commentDto.getCommentText());
            comment.setTimeChanged(LocalDateTime.now());
            comment = commentRepository.save(comment);
            return convertToDto(comment);
        } else {
            return null;
        }
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }

    // Преобразование между Entity и DTO
    private CommentDto convertToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setPostId(comment.getPostId());
        commentDto.setAuthorId(comment.getAuthorId());
        commentDto.setCommentText(comment.getCommentText());
        commentDto.setTime(comment.getTime());
        commentDto.setTimeChanged(comment.getTimeChanged());
        commentDto.setBlocked(comment.isBlocked());
        commentDto.setDelete(comment.isDelete());
        commentDto.setLikeAmount(comment.getLikeAmount());
        return commentDto;
    }

    private Comment convertToEntity(CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setPostId(commentDto.getPostId());
        comment.setAuthorId(commentDto.getAuthorId());
        comment.setCommentText(commentDto.getCommentText());
        comment.setTime(commentDto.getTime());
        comment.setTimeChanged(commentDto.getTimeChanged());
        comment.setBlocked(commentDto.isBlocked());
        comment.setDelete(commentDto.isDelete());
        comment.setLikeAmount(commentDto.getLikeAmount());
        return comment;
    }
}

