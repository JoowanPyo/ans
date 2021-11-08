package com.gemiso.zodiac.app.anchorCap.mapper;

import com.gemiso.zodiac.app.anchorCap.AnchorCap;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnchorCapMapper extends GenericMapper<AnchorCapDTO, AnchorCap, AnchorCapDTO> {
}
