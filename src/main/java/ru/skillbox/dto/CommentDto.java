package ru.skillbox.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.skillbox.model.LikeComment;
import ru.skillbox.model.Post;
import ru.skillbox.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CommentDTO {
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
}
