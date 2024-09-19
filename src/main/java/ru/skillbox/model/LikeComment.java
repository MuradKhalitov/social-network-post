package ru.skillbox.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "like_comment")
public class LikeComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //    @ManyToOne
//    private Account author;
    @Column(name = "author_id")
    private UUID authorId;
    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

}

