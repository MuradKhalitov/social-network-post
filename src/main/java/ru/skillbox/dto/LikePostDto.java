package ru.skillbox.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikePostDto {

    private Long id;
    private Long authorId;
    private Long postId;

}

