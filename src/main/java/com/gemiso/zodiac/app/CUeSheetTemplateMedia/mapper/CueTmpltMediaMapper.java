package com.gemiso.zodiac.app.CUeSheetTemplateMedia.mapper;

import com.gemiso.zodiac.app.CUeSheetTemplateMedia.CueTmpltMedia;
import com.gemiso.zodiac.app.CUeSheetTemplateMedia.dto.CueTmpltMediaDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmpltMediaMapper extends GenericMapper<CueTmpltMediaDTO, CueTmpltMedia, CueTmpltMediaDTO> {
}
