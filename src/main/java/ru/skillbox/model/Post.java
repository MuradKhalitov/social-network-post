package ru.skillbox.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime time;
    @Column(name = "time_changed")
    private LocalDateTime timeChanged;
    @Column(name = "author_id")
    private Long authorId;
    private String title;
    private String type;
    @Column(name = "post_text")
    private String postText;
    @Column(name = "is_blocked")
    private boolean isBlocked;
    @Column(name = "is_delete")
    private boolean isDelete;
    @Column(name = "comments_count")
    private int commentsCount;
    @Column(name = "like_amount")
    private int likeAmount;
    @Column(name = "my_like")
    private boolean myLike;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "publish_date")
    private LocalDateTime publishDate;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    private String tags;
}

