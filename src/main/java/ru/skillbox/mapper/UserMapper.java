package ru.skillbox.mapper;

import ru.skillbox.dto.user.UserDTO;
import ru.skillbox.model.Account;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    Account convertToEntity(UserDTO userDTO);

    UserDTO convertToDTO(Account account);
}