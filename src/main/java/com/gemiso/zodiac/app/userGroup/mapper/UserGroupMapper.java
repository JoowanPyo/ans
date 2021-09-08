package com.gemiso.zodiac.app.userGroup.mapper;

import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserGroupMapper extends GenericMapper<UserGroupDTO, UserGroup, UserGroupDTO> {

    List<UserGroupDTO> toDtoList(List<UserGroup> userGroups);
}
