package com.gemiso.zodiac.app.auth.mapper;

import com.gemiso.zodiac.app.auth.Auth;
import com.gemiso.zodiac.app.auth.dto.AuthDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper extends GenericMapper<AuthDTO, Auth, AuthDTO> {
}
