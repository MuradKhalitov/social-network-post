package ru.skillbox.dto.response;

import lombok.Getter;
import lombok.Setter;
import ru.skillbox.dto.CommentDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BriefPostDTO {
    private Long id;
    private String title;
    private String postText;
    private Long authorId;
    private LocalDateTime time;
    private LocalDateTime timeChanged;
    private String type;
    private boolean isBlocked;
    private boolean isDelete;
//    private Integer commentsCount;
    private int likeAmount;
    private boolean myLike;
    private String imagePath;
    private LocalDateTime publishDate;
    private Integer comments;

    public void setUsername(String username) {
    }
}
