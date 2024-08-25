package ru.skillbox.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime time;
    private LocalDateTime timeChanged;
    private Long authorId;
    private String title;
    private String type;
    private String postText;
    private boolean isBlocked;
    private boolean isDelete;
    private int commentsCount;
    private int likeAmount;
    private boolean myLike;
    private String imagePath;
    private LocalDateTime publishDate;

    @ElementCollection
    private List<String> tags;
}

