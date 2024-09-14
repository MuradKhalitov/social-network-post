package ru.skillbox.dto.likeComment;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class LikeCommentDto {

    private Long id;
    private UUID authorId;
    private Long commentId;

}

