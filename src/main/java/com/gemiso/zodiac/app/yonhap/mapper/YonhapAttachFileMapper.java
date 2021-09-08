package com.gemiso.zodiac.app.yonhap.mapper;

import com.gemiso.zodiac.app.yonhap.YonhapAttchFile;
import com.gemiso.zodiac.app.yonhap.dto.YonhapAttachFileDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YonhapAttachFileMapper extends GenericMapper<YonhapAttachFileDTO, YonhapAttchFile, YonhapAttachFileDTO> {
}
