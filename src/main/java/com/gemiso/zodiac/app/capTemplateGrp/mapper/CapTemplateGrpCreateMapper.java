package com.gemiso.zodiac.app.capTemplateGrp.mapper;

import com.gemiso.zodiac.app.capTemplateGrp.CapTemplateGrp;
import com.gemiso.zodiac.app.capTemplateGrp.dto.CapTemplateGrpCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CapTemplateGrpCreateMapper extends GenericMapper<CapTemplateGrpCreateDTO, CapTemplateGrp, CapTemplateGrpCreateDTO> {
}
