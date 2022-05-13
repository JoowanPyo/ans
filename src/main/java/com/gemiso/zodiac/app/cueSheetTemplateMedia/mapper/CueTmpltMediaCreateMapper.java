package com.gemiso.zodiac.app.cueSheetTemplateMedia.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateMedia.CueTmpltMedia;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.dto.CueTmpltMediaCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmpltMediaCreateMapper extends GenericMapper<CueTmpltMediaCreateDTO, CueTmpltMedia, CueTmpltMediaCreateDTO> {
}
