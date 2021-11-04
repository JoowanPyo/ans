package com.gemiso.zodiac.app.yonhapAssign.mapper;

import com.gemiso.zodiac.app.yonhapAssign.YonhapAssign;
import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YonhapAssignMapper extends GenericMapper<YonhapAssignDTO, YonhapAssign, YonhapAssignDTO> {
}
