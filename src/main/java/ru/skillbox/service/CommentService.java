package ru.skillbox.service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.dto.comment.request.CommentDto;
import ru.skillbox.dto.comment.response.PageCommentDto;
import ru.skillbox.exception.CommentNotFoundException;
import ru.skillbox.mapper.CommentMapper;
import ru.skillbox.model.Account;
import ru.skillbox.model.Comment;
import ru.skillbox.model.Post;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final CurrentUsers currentUsers;


    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository, CommentMapper commentMapper, CurrentUsers currentUsers) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
        this.currentUsers = currentUsers;
    }

    public CommentDto createComment(Long postId, CommentDto commentDTO, Long parentCommentId) {
        Comment comment = commentMapper.convertToEntity(commentDTO);
        if (parentCommentId != null) {
            Comment parentComment = commentRepository.findById(parentCommentId)
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParent(parentComment);
        }
        UUID userId = currentUsers.getCurrentUserId();
        Account account = userRepository.findById(userId).get();
        Post post = postRepository.findById(postId).get();
        comment.setAuthor(account);
        post.setCommentsCount(post.getCommentsCount() + 1);
        comment.setPost(post);
        //post.updateCommentsCount();
        log.info("Пользователь: {}, добавил комментарий", account.getEmail());
        return commentMapper.convertToDTO(commentRepository.save(comment));
    }

    public List<CommentDto> getCommentsByNewsId(Long postId, PageRequest pageRequest) {
        Page<Comment> page = commentRepository.findByPostId(postId, pageRequest);

        return page.getContent().stream()
                .map(commentMapper::convertToDTO)
                .collect(Collectors.toList());
    }
    public PageCommentDto getComments(Long postId, Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

        // Формирование PageCommentDto
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

        // Маппинг Comment в CommentContent
        List<PageCommentDto.CommentContent> content = commentPage.getContent().stream().map(comment -> new PageCommentDto.CommentContent(
                comment.getId(),
                comment.getCommentType(),
                comment.getTime(),
                comment.getTimeChanged(),
                comment.getAuthor().getId(),
                comment.getParent() != null ? comment.getParent().getId() : 0L,
                comment.getCommentText(),
                comment.getPost().getId(),
                comment.isBlocked(),
                comment.isDelete(),
                comment.getLikeAmount(),
                comment.isMyLike(),
                comment.getSubComments().size(),
                comment.getImagePath()
        )).collect(Collectors.toList());

        pageCommentDto.setContent(content);
        return pageCommentDto;
    }


    public CommentDto getCommentById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::convertToDTO)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found"));
    }

    @Transactional
    public CommentDto updateComment(Long id, CommentDto updatedCommentDto) {
        UUID userId = currentUsers.getCurrentUserId();
        Account currentAccount = userRepository.findById(userId).get();

        Comment oldComment = commentMapper.convertToEntity(getCommentById(id));
        Account authorComment = oldComment.getAuthor();

        if (currentAccount.getId().equals(authorComment.getId())) {
            oldComment.setCommentText(updatedCommentDto.getCommentText());

            return commentMapper.convertToDTO(commentRepository.save(oldComment));
        }
        return null;
    }

    public void deleteComment(Long commentId) {

        UUID userId = currentUsers.getCurrentUserId();
        Account account = userRepository.findById(userId).get();

        Optional<Comment> existingComment = commentRepository.findByIdAndAuthorId(commentId, account.getId());
        if (existingComment.isPresent()) {
            Post post = postRepository.findById(existingComment.get().getPost().getId()).get();
            post.setCommentsCount(post.getCommentsCount() - 1);
            commentRepository.deleteById(commentId);
        } else new CommentNotFoundException("Comment with id " + commentId + " not found");

    }
}

