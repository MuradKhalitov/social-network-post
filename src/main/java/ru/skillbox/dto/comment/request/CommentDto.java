package ru.skillbox.dto.comment.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentDto {
    private Long id;
    private String commentType;
    private LocalDateTime time;
    private LocalDateTime timeChanged;
    private Long authorId;
    private Long parentId;
    @NotNull
    @Size(min = 1, max = 255, message = "Min comment size is: {min}. Max comment size is: {max}")
    private String commentText;

    private Long postId;
    private boolean isBlocked;
    private boolean isDelete;
    private int likeAmount;
    private boolean myLike;
    private int commentsCount;
    private String imagePath;
    private List<CommentDto> subComments;
}
