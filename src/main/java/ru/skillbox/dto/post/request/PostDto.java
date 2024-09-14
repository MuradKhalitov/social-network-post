package ru.skillbox.dto.post.request;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.dto.comment.request.CommentDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PostDto {
    private Long id;
    private String title;
    private String postText;
    private UUID authorId;
    private LocalDateTime time;
    private LocalDateTime timeChanged;
    private String type;
    private boolean isBlocked;
    private boolean isDeleted;
    private Integer commentsCount;
    private List<String> tags;
    private int likeAmount;
    private boolean myLike;
    private String imagePath;
    private LocalDateTime publishDate;
    private List<CommentDto> comments = new ArrayList<>();
}
