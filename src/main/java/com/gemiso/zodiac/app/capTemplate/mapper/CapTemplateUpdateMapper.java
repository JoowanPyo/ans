package com.gemiso.zodiac.app.capTemplate.mapper;

import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateUpdateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CapTemplateUpdateMapper extends GenericMapper<CapTemplateUpdateDTO, CapTemplate, CapTemplateUpdateDTO> {
}
