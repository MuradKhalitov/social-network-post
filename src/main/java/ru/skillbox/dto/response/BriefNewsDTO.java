package ru.skillbox.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BriefNewsDTO {
    private Long id;
    private String title;
    private String postText;
    private Long authorId;
    private LocalDateTime time;
    private LocalDateTime timeChanged;
    private Integer comments;

    public void setUsername(String username) {
    }
}
