package ru.skillbox.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.skillbox.dto.CommentDto;
import ru.skillbox.exception.CommentNotFoundException;
import ru.skillbox.mapper.CommentMapper;
import ru.skillbox.model.Comment;
import ru.skillbox.model.Post;
import ru.skillbox.model.User;
import ru.skillbox.repository.CommentRepository;
import ru.skillbox.repository.NewsRepository;
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
    private final NewsRepository newsRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;


    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, NewsRepository newsRepository, CommentMapper commentMapper, UserService userService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.newsRepository = newsRepository;
        this.commentMapper = commentMapper;
        this.userService = userService;
    }

//    public CommentDTO createComment(Long postId, CommentDTO commentDTO) {
//        Comment comment = commentMapper.convertToEntity(commentDTO);
//        String currentUsername = CurrentUsers.getCurrentUsername();
//        User user = userRepository.findByUsername(currentUsername).get();
//        Optional<Post> post = newsRepository.findById(postId);
//        comment.setAuthor(user);
//        comment.setPost(post.get());
//        log.info("Пользователь: {}, добавил комментарий", currentUsername);
//        return commentMapper.convertToDTO(commentRepository.save(comment));
//    }

    public CommentDto createComment(Long postId, CommentDto commentDTO, Long parentCommentId) {
        Comment comment = commentMapper.convertToEntity(commentDTO);
        if (parentCommentId != null) {
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParent(parentComment);
        }
        String currentUsername = CurrentUsers.getCurrentUsername();
        User user = userRepository.findByUsername(currentUsername).get();
        Optional<Post> post = newsRepository.findById(postId);
        comment.setAuthor(user);
        comment.setPost(post.get());
        log.info("Пользователь: {}, добавил комментарий", currentUsername);
        return commentMapper.convertToDTO(commentRepository.save(comment));
    }

    public List<CommentDto> getCommentsByNewsId(Long newsId, PageRequest pageRequest) {
        Page<Comment> page = commentRepository.findAll(pageRequest);

        return page.getContent().stream()
                .map(commentMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public CommentDto getCommentById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::convertToDTO)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found"));
    }

    public CommentDto updateComment(Long id, CommentDto updatedCommentDto) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User currentUser = userService.findByUsername(currentUsername);

        Comment oldComment = commentMapper.convertToEntity(getCommentById(id));
        User authorComment = oldComment.getAuthor();

        if (currentUser.getId().equals(authorComment.getId())) {
            oldComment.setCommentText(updatedCommentDto.getCommentText());

            return commentMapper.convertToDTO(commentRepository.save(oldComment));
        }
        return null;
    }

    public ResponseEntity<Void> deleteComment(Long id) {
        String currentUsername = CurrentUsers.getCurrentUsername();
        User currentUser = userService.findByUsername(currentUsername);
        Comment deletedComment = commentMapper.convertToEntity(getCommentById(id));
        User authorComment = deletedComment.getAuthor();

        if (currentUser.getId().equals(authorComment.getId()) || CurrentUsers.hasRole("ADMIN") || CurrentUsers.hasRole("MODERATOR")) {
            commentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }
}

