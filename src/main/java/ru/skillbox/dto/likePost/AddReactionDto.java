package ru.skillbox.dto.likePost;

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
