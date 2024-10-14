package ru.skillbox.dto.like_post;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LikePostDto {

    private Long id;
    private UUID authorId;
    private Long postId;
    private String type;
    private String reactionType;

}

