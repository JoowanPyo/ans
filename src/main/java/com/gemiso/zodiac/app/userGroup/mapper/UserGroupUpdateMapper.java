package com.gemiso.zodiac.app.userGroup.mapper;

import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserGroupUpdateMapper extends GenericMapper<UserGroupUpdateDTO, UserGroup, UserGroupUpdateDTO> {
}
