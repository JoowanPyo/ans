package com.gemiso.zodiac.app.breakingNewsFtpInfo.mapper;

import com.gemiso.zodiac.app.breakingNewsFtpInfo.BreakingNewsFtpInfo;
import com.gemiso.zodiac.app.breakingNewsFtpInfo.dto.BreakingNewsFtpInfoDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BreakingNewsFtpInfoMapper extends GenericMapper<BreakingNewsFtpInfoDTO, BreakingNewsFtpInfo, BreakingNewsFtpInfoDTO> {
}
