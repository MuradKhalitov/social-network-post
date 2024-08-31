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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(name = "post_text")
    private String postText;

    @ManyToOne
    private User author;
    @CreationTimestamp
    private LocalDateTime time;
    @UpdateTimestamp
    @Column(name = "time_changed")
    private LocalDateTime timeChanged;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikePost> likes;
    private String type;
    @Column(name = "is_blocked")
    private boolean isBlocked;
    @Column(name = "is_delete")
    private boolean isDelete;
    @Column(name = "comments_count")
    private Integer commentsCount;
    @Column(name = "like_amount")
    private int likeAmount;
    @Column(name = "my_like")
    private boolean myLike;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "publish_date")
    private LocalDateTime publishDate;
    private String tags;

}


