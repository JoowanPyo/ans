package com.gemiso.zodiac.app.user.mapper;

import com.gemiso.zodiac.app.user.UserGroupUser;
import com.gemiso.zodiac.app.user.dto.UserGroupUserDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserGroupUserMapper extends GenericMapper<UserGroupUserDTO, UserGroupUser, UserGroupUserDTO> {

    /*List<UserGroupUserDTO> dtoList(List<UserGroupUser> userGroupUsers);

    List<UserGroupUser> toEntityList(List<UserGroupUserDTO> userGroupUserDTOList);*/

}
