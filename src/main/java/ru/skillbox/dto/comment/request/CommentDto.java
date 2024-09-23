package ru.skillbox.dto.comment.request;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.dto.likeComment.LikeCommentDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String commentType;
    private LocalDateTime time;
    private LocalDateTime timeChanged;
    private UUID authorId;
    private Long parentId;
    private String commentText;
    private Long postId;
    private boolean isBlocked;
    private boolean isDelete;
    private int likeAmount;
    private boolean myLike;
    private int commentsCount;
    private String imagePath;
    private List<CommentDto> subComments;
    private List<LikeCommentDto> likes;
}
