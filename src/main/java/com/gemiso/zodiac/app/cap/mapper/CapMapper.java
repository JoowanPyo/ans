package com.gemiso.zodiac.app.cap.mapper;

import com.gemiso.zodiac.app.cap.Cap;
import com.gemiso.zodiac.app.cap.dto.CapDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CapMapper extends GenericMapper<CapDTO, Cap, CapDTO> {
}
