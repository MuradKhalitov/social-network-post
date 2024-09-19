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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(name = "post_text")
    private String postText;

    //@ManyToOne
    //private Account author;
    @Column(name = "author_id")
    private UUID authorId;
    @CreationTimestamp
    private LocalDateTime time;
    @UpdateTimestamp
    @Column(name = "time_changed")
    private LocalDateTime timeChanged;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments =  new ArrayList<>();
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikePost> likes= new ArrayList<>();
    private String type;
    @Column(name = "is_blocked")
    private boolean isBlocked;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "comments_count")
    private Integer commentsCount = 0;
    @Column(name = "like_amount")
    private Integer likeAmount;
    @Column(name = "my_like")
    private boolean myLike;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "publish_date")
    private LocalDateTime publishDate;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

    public void updateCommentsCount() {
        this.commentsCount = comments.size();
    }

    private void updateLikeAmount() {
        this.likeAmount = likes.size();
    }
}


