package com.gemiso.zodiac.app.appAuth.mapper;

import com.gemiso.zodiac.app.appAuth.AppAuth;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthDTO;
import com.gemiso.zodiac.app.appAuth.dto.AppAuthUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AppAuthMapper extends GenericMapper<AppAuthDTO, AppAuth, AppAuthUpdateDTO> {

    List<AppAuthDTO> toDtoList(List<AppAuth> appAuths);
}
