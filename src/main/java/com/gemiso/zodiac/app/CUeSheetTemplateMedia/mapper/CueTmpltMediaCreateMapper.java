package com.gemiso.zodiac.app.CUeSheetTemplateMedia.mapper;

import com.gemiso.zodiac.app.CUeSheetTemplateMedia.CueTmpltMedia;
import com.gemiso.zodiac.app.CUeSheetTemplateMedia.dto.CueTmpltMediaCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmpltMediaCreateMapper extends GenericMapper<CueTmpltMediaCreateDTO, CueTmpltMedia, CueTmpltMediaCreateDTO> {
}
