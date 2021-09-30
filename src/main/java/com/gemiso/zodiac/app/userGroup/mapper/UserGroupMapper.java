package com.gemiso.zodiac.app.userGroup.mapper;

import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.app.userGroup.dto.UserGroupDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserGroupMapper {

    @Mapping(target = "inputr.userId", source = "inputr.userId")
    @Mapping(target = "inputr.userNm", source = "inputr.userNm")
    @Mapping(target = "updtr.userId", source = "updtr.userId")
    @Mapping(target = "updtr.userNm", source = "updtr.userNm")
    @Mapping(target = "delr.userId", source = "delr.userId")
    @Mapping(target = "delr.userNm", source = "delr.userNm")
    UserGroupDTO toDto(UserGroup e);
    UserGroup toEntity(UserGroupDTO d);
    @Mapping(target = "inputr.userId", source = "inputr.userId")
    @Mapping(target = "inputr.userNm", source = "inputr.userNm")
    @Mapping(target = "updtr.userId", source = "updtr.userId")
    @Mapping(target = "updtr.userNm", source = "updtr.userNm")
    @Mapping(target = "delr.userId", source = "delr.userId")
    @Mapping(target = "delr.userNm", source = "delr.userNm")
    List<UserGroupDTO> toDtoList(List<UserGroup> listE);
    List<UserGroup> toEntityList(List<UserGroupDTO> listD);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UserGroupDTO updateDto, @MappingTarget UserGroup entity);
}
