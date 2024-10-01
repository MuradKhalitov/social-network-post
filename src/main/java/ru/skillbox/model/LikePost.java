package ru.skillbox.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "like_post")
public class LikePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "author_id")
    private UUID authorId;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    @Column(name = "reaction_type")
    private String reactionType;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}

