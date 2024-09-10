package ru.skillbox.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String username;
    private String email;
    private String password;
    private String roles;
}

