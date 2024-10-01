package ru.skillbox.dto.likePost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReactionInfoDto {
    private int likeAmount;           // Общее количество лайков
    private boolean myLike;           // Установлен ли лайк текущим пользователем
    private String myReaction;        // Тип реакции текущего пользователя
    private List<ReactionDto> reactions; // Список всех типов реакций и их количества
}

