package com.gemiso.zodiac.app.userGroupUser.mapper;

import com.gemiso.zodiac.app.userGroupUser.UserGroupUser;
import com.gemiso.zodiac.app.userGroupUser.dto.UserGroupUserDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserGroupUserMapper extends GenericMapper<UserGroupUserDTO, UserGroupUser, UserGroupUserDTO> {

    /*List<UserGroupUserDTO> dtoList(List<UserGroupUser> userGroupUsers);

    List<UserGroupUser> toEntityList(List<UserGroupUserDTO> userGroupUserDTOList);*/

}
