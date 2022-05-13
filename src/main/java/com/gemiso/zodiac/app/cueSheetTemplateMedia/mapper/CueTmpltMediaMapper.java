package com.gemiso.zodiac.app.cueSheetTemplateMedia.mapper;

import com.gemiso.zodiac.app.cueSheetTemplateMedia.CueTmpltMedia;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.dto.CueTmpltMediaDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmpltMediaMapper extends GenericMapper<CueTmpltMediaDTO, CueTmpltMedia, CueTmpltMediaDTO> {
}
