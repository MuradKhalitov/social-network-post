package ru.skillbox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikeDto {

    private Long id;
    private Long userId;
    private Long postId;
    private Long commentId;

}

