package com.gemiso.zodiac.app.user.mapper;

import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.dto.UserDTO;
import com.gemiso.zodiac.app.user.dto.UserUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper extends GenericMapper<UserDTO, User, UserDTO>{

    /*@Mapping(target = "inputr.userId", source = "inputr.userId")
    @Mapping(target = "inputr.userNm", source = "inputr.userNm")
    @Mapping(target = "updtr.userId", source = "updtr.userId")
    @Mapping(target = "updtr.userNm", source = "updtr.userNm")
    @Mapping(target = "delr.userId", source = "delr.userId")
    @Mapping(target = "delr.userNm", source = "delr.userNm")
    UserDTO toDto(MisUser e);
    MisUser toEntity(UserDTO d);
    @Mapping(target = "inputr.userId", source = "inputr.userId")
    @Mapping(target = "inputr.userNm", source = "inputr.userNm")
    @Mapping(target = "updtr.userId", source = "updtr.userId")
    @Mapping(target = "updtr.userNm", source = "updtr.userNm")
    @Mapping(target = "delr.userId", source = "delr.userId")
    @Mapping(target = "delr.userNm", source = "delr.userNm")
    List<UserDTO> toDtoList(List<MisUser> listE);
    List<MisUser> toEntityList(List<UserDTO> listD);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UserDTO updateDto, @MappingTarget MisUser entity);*/
}
