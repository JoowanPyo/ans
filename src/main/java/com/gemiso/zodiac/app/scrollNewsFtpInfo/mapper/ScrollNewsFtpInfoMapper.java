package com.gemiso.zodiac.app.scrollNewsFtpInfo.mapper;

import com.gemiso.zodiac.app.scrollNewsFtpInfo.ScrollNewsFtpInfo;
import com.gemiso.zodiac.app.scrollNewsFtpInfo.dto.ScrollNewsFtpInfoDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScrollNewsFtpInfoMapper extends GenericMapper<ScrollNewsFtpInfoDTO, ScrollNewsFtpInfo, ScrollNewsFtpInfoDTO> {
}
