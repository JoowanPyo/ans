package com.gemiso.zodiac.app.appAuth.mapper;

import com.gemiso.zodiac.app.appAuth.AppAuth;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppAuthUpdateMapper extends GenericMapper<AppAuthUpdateDTO, AppAuth, AppAuthUpdateDTO> {
}
