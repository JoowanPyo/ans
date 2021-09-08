package com.gemiso.zodiac.app.user.mapper;

import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.dto.UserUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserUpdateMapper extends GenericMapper<UserUpdateDTO, User, UserUpdateDTO> {
}
