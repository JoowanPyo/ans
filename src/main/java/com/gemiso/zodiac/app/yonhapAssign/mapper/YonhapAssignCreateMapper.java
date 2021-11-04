package com.gemiso.zodiac.app.yonhapAssign.mapper;

import com.gemiso.zodiac.app.yonhapAssign.YonhapAssign;
import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YonhapAssignCreateMapper extends GenericMapper<YonhapAssignCreateDTO, YonhapAssign, YonhapAssignCreateDTO> {
}
