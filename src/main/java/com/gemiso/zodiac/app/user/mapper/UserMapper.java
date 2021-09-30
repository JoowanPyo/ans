package com.gemiso.zodiac.app.user.mapper;

import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.user.dto.UserUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper  {

    @Mapping(target = "inputr.userId", source = "inputr.userId")
    @Mapping(target = "inputr.userNm", source = "inputr.userNm")
    @Mapping(target = "updtr.userId", source = "updtr.userId")
    @Mapping(target = "updtr.userNm", source = "updtr.userNm")
    @Mapping(target = "delr.userId", source = "delr.userId")
    @Mapping(target = "delr.userNm", source = "delr.userNm")
    UserDTO toDto(User e);
    User toEntity(UserDTO d);
    @Mapping(target = "inputr.userId", source = "inputr.userId")
    @Mapping(target = "inputr.userNm", source = "inputr.userNm")
    @Mapping(target = "updtr.userId", source = "updtr.userId")
    @Mapping(target = "updtr.userNm", source = "updtr.userNm")
    @Mapping(target = "delr.userId", source = "delr.userId")
    @Mapping(target = "delr.userNm", source = "delr.userNm")
    List<UserDTO> toDtoList(List<User> listE);
    List<User> toEntityList(List<UserDTO> listD);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UserDTO updateDto, @MappingTarget User entity);
}
