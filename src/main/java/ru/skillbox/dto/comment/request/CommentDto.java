package ru.skillbox.dto.comment.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
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
    private boolean isDeleted;
    private int likeAmount;
    private boolean myLike;
    private int commentsCount;
    private String imagePath;
}
