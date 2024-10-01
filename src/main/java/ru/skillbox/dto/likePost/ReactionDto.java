package ru.skillbox.dto.likePost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDto {
    private String reactionType; // Тип реакции (например, "heart", "funny")
    private int count;           // Количество таких реакций
}
