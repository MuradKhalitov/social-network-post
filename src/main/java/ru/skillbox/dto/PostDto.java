package ru.skillbox.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
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
    private List<String> tags;
    private int likeAmount;
    private boolean myLike;
    private String imagePath;
    private LocalDateTime publishDate;
}

