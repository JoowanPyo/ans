package com.gemiso.zodiac.app.yonhapAttchFile.mapper;

import com.gemiso.zodiac.app.yonhapAttchFile.YonhapAttchFile;
import com.gemiso.zodiac.app.yonhapAttchFile.dto.YonhapAttachFileDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface YonhapAttachFileMapper extends GenericMapper<YonhapAttachFileDTO, YonhapAttchFile, YonhapAttachFileDTO> {
}
