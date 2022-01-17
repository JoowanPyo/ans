package com.gemiso.zodiac.app.userGroupAuth.mapper;

import com.gemiso.zodiac.app.userGroupAuth.UserGroupAuth;
import com.gemiso.zodiac.app.userGroupAuth.dto.UserGroupAuthDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface UserGroupAuthMapper extends GenericMapper<UserGroupAuthDTO, UserGroupAuth, UserGroupAuthDTO> {

}
