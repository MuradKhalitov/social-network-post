package ru.skillbox.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    @NotNull
    @Size(min = 1, max = 255, message = "Min comment size is: {min}. Max comment size is: {max}")
    private String content;
    private Long authorId;
    private Long postId;
}
