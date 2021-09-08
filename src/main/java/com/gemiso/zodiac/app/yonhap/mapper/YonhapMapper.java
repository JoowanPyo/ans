package com.gemiso.zodiac.app.yonhap.mapper;

import com.gemiso.zodiac.app.yonhap.Yonhap;
import com.gemiso.zodiac.app.yonhap.dto.YonhapDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YonhapMapper extends GenericMapper<YonhapDTO, Yonhap, YonhapDTO> {
}
