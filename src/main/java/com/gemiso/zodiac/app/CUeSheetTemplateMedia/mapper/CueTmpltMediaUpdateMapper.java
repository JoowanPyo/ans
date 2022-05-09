package com.gemiso.zodiac.app.CUeSheetTemplateMedia.mapper;

import com.gemiso.zodiac.app.CUeSheetTemplateMedia.CueTmpltMedia;
import com.gemiso.zodiac.app.CUeSheetTemplateMedia.dto.CueTmpltMediaUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CueTmpltMediaUpdateMapper extends GenericMapper<CueTmpltMediaUpdateDTO, CueTmpltMedia, CueTmpltMediaUpdateDTO> {
}
