package com.gemiso.zodiac.app.userGroup.mapper;

import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserGroupCreateMapper extends GenericMapper<UserGroupCreateDTO, UserGroup, UserGroupCreateDTO> {
}
