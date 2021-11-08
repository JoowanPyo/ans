package com.gemiso.zodiac.app.anchorCap.mapper;

import com.gemiso.zodiac.app.anchorCap.AnchorCap;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapSimpleDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnchorCapSimpleMapper extends GenericMapper<AnchorCapSimpleDTO, AnchorCap, AnchorCapSimpleDTO> {
}
