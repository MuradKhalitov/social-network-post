package ru.skillbox.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    @ManyToOne()
    private User author;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> subComments;
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
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeComment> likes = new ArrayList<>();

    public void addLike(LikeComment like) {
        likes.add(like);
        like.setComment(this);
    }

    public void removeLike(LikeComment like) {
        likes.remove(like);
        like.setComment(null);
    }
}
