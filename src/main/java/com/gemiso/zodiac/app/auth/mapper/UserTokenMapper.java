package com.gemiso.zodiac.app.auth.mapper;

import com.gemiso.zodiac.app.auth.UserToken;
import com.gemiso.zodiac.app.auth.dto.UserTokenDTO;
import com.gemiso.zodiac.app.user.dto.UserUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserTokenMapper extends GenericMapper<UserTokenDTO, UserToken, UserUpdateDTO> {
}
