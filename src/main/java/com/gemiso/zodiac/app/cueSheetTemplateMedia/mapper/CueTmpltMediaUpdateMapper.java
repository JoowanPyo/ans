package com.gemiso.zodiac.app.cueSheetTemplateMedia.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateMedia.CueTmpltMedia;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.dto.CueTmpltMediaUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmpltMediaUpdateMapper extends GenericMapper<CueTmpltMediaUpdateDTO, CueTmpltMedia, CueTmpltMediaUpdateDTO> {
}
