package com.gemiso.zodiac.app.user.mapper;

import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.user.dto.UserUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<UserDTO, User, UserUpdateDTO> {

    List<UserDTO> toDtoList(List<User> users);
}
