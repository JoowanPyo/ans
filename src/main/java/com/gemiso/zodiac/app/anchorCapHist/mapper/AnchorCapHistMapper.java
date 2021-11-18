package com.gemiso.zodiac.app.anchorCapHist.mapper;

import com.gemiso.zodiac.app.anchorCapHist.AnchorCapHist;
import com.gemiso.zodiac.app.anchorCapHist.dto.AnchorCapHistDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AnchorCapHistMapper extends GenericMapper<AnchorCapHistDTO, AnchorCapHist, AnchorCapHistDTO> {
}
