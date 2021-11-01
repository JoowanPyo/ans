package com.gemiso.zodiac.app.articleCap.mapper;

import com.gemiso.zodiac.app.articleCap.AnchorCap;
import com.gemiso.zodiac.app.articleCap.dto.AnchorCapSimpleDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnchorCapSimpleMapper extends GenericMapper<AnchorCapSimpleDTO, AnchorCap, AnchorCapSimpleDTO> {
}
