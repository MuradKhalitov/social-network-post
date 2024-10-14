package ru.skillbox.dto.like_post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddReactionDto {
    private String type;
    private String reactionType;
}
