package ru.skillbox.mapper;

import ru.skillbox.dto.user.UserDTO;
import ru.skillbox.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User convertToEntity(UserDTO userDTO);

    UserDTO convertToDTO(User user);
}