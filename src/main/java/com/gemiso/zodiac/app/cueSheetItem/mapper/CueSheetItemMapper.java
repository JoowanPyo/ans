package com.gemiso.zodiac.app.cueSheetItem.mapper;

import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CueSheetItemMapper {

    @Mapping(target = "inputr.userId", source = "inputr.userId")
    @Mapping(target = "inputr.userNm", source = "inputr.userNm")
    @Mapping(target = "updtr.userId", source = "updtr.userId")
    @Mapping(target = "updtr.userNm", source = "updtr.userNm")
    @Mapping(target = "delr.userId", source = "delr.userId")
    @Mapping(target = "delr.userNm", source = "delr.userNm")
    @Mapping(target = "lckr.userId", source = "lckr.userId")
    @Mapping(target = "lckr.userNm", source = "lckr.userNm")
    CueSheetItemDTO toDto(CueSheetItem cueSheetItem);
    CueSheetItem toEntity(CueSheetItemDTO d);
    List<CueSheetItemDTO> toDtoList(List<CueSheetItem> listE);
    List<CueSheetItem> toEntityList(List<CueSheetItemDTO> listD);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CueSheetItemDTO updateDto, @MappingTarget CueSheetItem entity);
}
