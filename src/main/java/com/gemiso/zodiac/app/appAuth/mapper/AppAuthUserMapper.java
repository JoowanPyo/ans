package com.gemiso.zodiac.app.appAuth.mapper;

import com.gemiso.zodiac.app.appAuth.AppAuth;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthUserDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppAuthUserMapper extends GenericMapper<AppAuthUserDTO, AppAuth, AppAuthUserDTO> {
}
