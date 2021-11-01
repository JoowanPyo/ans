package com.gemiso.zodiac.app.articleCap.mapper;

import com.gemiso.zodiac.app.articleCap.AnchorCap;
import com.gemiso.zodiac.app.articleCap.dto.AnchorCapDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnchorCapMapper extends GenericMapper<AnchorCapDTO, AnchorCap, AnchorCapDTO> {
}
