package com.gemiso.zodiac.app.fileFtpInfo.mapper;

import com.gemiso.zodiac.app.fileFtpInfo.FileFtpInfo;
import com.gemiso.zodiac.app.fileFtpInfo.dto.FileFtpInfoDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileFtpInfoMapper extends GenericMapper<FileFtpInfoDTO, FileFtpInfo, FileFtpInfoDTO> {
}
