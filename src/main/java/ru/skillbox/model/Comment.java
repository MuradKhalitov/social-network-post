package ru.skillbox.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "comment_type")
    private String commentType;
    @CreationTimestamp
    private LocalDateTime time;
    @UpdateTimestamp
    @Column(name = "time_changed")
    private LocalDateTime timeChanged;
    @ManyToOne
    private User author;
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
    private Integer likeAmount;
    @Column(name = "my_like")
    private boolean myLike;
    @Column(name = "comment_Count")
    private Integer commentCount;
    @Column(name = "image_path")
    private String imagePath;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeComment> likes;
}
