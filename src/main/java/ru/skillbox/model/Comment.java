package ru.skillbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String commentType;
    private LocalDateTime time;
    private LocalDateTime timeChanged;
    private Long authorId;
    private Long parentId;
    private String commentText;
    private Long postId;
    private boolean isBlocked;
    private boolean isDelete;
    private int likeAmount;
    private boolean myLike;
    private int commentsCount;
    private String imagePath;
}

