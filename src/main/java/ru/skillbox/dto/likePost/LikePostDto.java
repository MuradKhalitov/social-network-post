package ru.skillbox.dto.likePost;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LikePostDto {

    private Long id;
    private Long authorId;
    private Long postId;

}

