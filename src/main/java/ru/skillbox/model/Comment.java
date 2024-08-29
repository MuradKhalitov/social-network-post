package ru.skillbox.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "comment_type")
    private String commentType;
    private LocalDateTime time;
    @Column(name = "time_changed")
    private LocalDateTime timeChanged;
    @Column(name = "author_id")
    private Long authorId;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "comment_text")
    private String commentText;
    @ManyToOne
    private Post post;
    @Column(name = "is_blocked")
    private boolean isBlocked;
    @Column(name = "is_delete")
    private boolean isDelete;
    @Column(name = "like_amount")
    private int likeAmount;
    @Column(name = "my_like")
    private boolean myLike;
    @Column(name = "comments_count")
    private int commentsCount;
    @Column(name = "image_path")
    private String imagePath;
}

