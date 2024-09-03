package ru.skillbox.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PostDto {
    private Long id;
    private String title;
    private String postText;
    private Long authorId;
    private LocalDateTime time;
    private LocalDateTime timeChanged;
    private String type;
    private boolean isBlocked;
    private boolean isDelete;
    private Integer commentsCount;
    private List<String> tags;
    private int likeAmount;
    private boolean myLike;
    private String imagePath;
    private LocalDateTime publishDate;
    private List<CommentDto> comments = new ArrayList<>();
}
