package ru.skillbox.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Column(name = "author_id")
    private UUID authorId;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> subComments = new ArrayList<>();
    @Column(name = "comment_text")
    private String commentText;
    @ManyToOne
    private Post post;
    @Column(name = "is_blocked")
    private boolean isBlocked;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "like_amount")
    private int likeAmount;
    @Column(name = "my_like")
    private boolean myLike;
    @Column(name = "comments_count")
    private int commentsCount;
    @Column(name = "image_path")
    private String imagePath;
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeComment> likes = new ArrayList<>();

    public void updateLikeAmount() {
        this.likeAmount = likes.size();
    }

    public Long getParentId() {
        return parent.getId();
    }
}
