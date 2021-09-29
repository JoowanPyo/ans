package com.gemiso.zodiac.app.capTemplate.mapper;

import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CapTemplateCreateMapper extends GenericMapper<CapTemplateCreateDTO, CapTemplate, CapTemplateCreateDTO> {
}
