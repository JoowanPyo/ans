package com.gemiso.zodiac.app.userGroup.mapper;

import com.gemiso.zodiac.app.userGroup.UserGroupAuth;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupAuthDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface UserGroupAuthMapper extends GenericMapper<UserGroupAuthDTO, UserGroupAuth, UserGroupAuthDTO> {

}
