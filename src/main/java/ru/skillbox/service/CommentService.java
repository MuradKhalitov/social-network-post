package ru.skillbox.service;

import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dto.CommentDto;
import ru.skillbox.exception.CommentNotFoundException;
import ru.skillbox.mapper.CommentMapper;
import ru.skillbox.model.Comment;
import ru.skillbox.model.Post;
import ru.skillbox.model.User;
import ru.skillbox.repository.CommentRepository;
import ru.skillbox.repository.PostRepository;
import ru.skillbox.repository.UserRepository;
import ru.skillbox.util.CurrentUsers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;


    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
    }

    public CommentDto createComment(Long postId, CommentDto commentDTO, Long parentCommentId) {
        Comment comment = commentMapper.convertToEntity(commentDTO);
        if (parentCommentId != null) {
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParent(parentComment);
        }
        String currentUsername = CurrentUsers.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername).get();
        Optional<Post> post = postRepository.findById(postId);
        comment.setAuthor(user);
        comment.setPost(post.get());
        log.info("Пользователь: {}, добавил комментарий", currentUsername);
        return commentMapper.convertToDTO(commentRepository.save(comment));
    }

    public List<CommentDto> getCommentsByNewsId(Long postId, PageRequest pageRequest) {
        Page<Comment> page = commentRepository.findByPostId(postId, pageRequest);

        return page.getContent().stream()
                .map(commentMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommentDto getCommentById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::convertToDTO)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found"));
    }

    @Transactional
    public CommentDto updateComment(Long id, CommentDto updatedCommentDto) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User currentUser = userRepository.findByUsername(currentUsername).get();

        Comment oldComment = commentMapper.convertToEntity(getCommentById(id));
        User authorComment = oldComment.getAuthor();

        if (currentUser.getId().equals(authorComment.getId())) {
            oldComment.setCommentText(updatedCommentDto.getCommentText());

            return commentMapper.convertToDTO(commentRepository.save(oldComment));
        }
        return null;
    }

    public void deleteComment(Long id) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User currentUser = userRepository.findByUsername(currentUsername).get();
        Comment deletedComment = commentMapper.convertToEntity(getCommentById(id));
        User authorComment = deletedComment.getAuthor();

        if (currentUser.getId().equals(authorComment.getId()) || CurrentUsers.hasRole("ADMIN") || CurrentUsers.hasRole("MODERATOR")) {
            commentRepository.deleteById(id);
        } else new CommentNotFoundException("Comment with id " + id + " not found");

    }
}

