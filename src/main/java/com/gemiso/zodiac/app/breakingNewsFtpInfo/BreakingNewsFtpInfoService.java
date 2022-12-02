package com.gemiso.zodiac.app.breakingNewsFtpInfo;

import com.gemiso.zodiac.app.breakingNewsFtpInfo.dto.BreakingNewsFtpInfoDTO;
import com.gemiso.zodiac.app.breakingNewsFtpInfo.mapper.BreakingNewsFtpInfoMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BreakingNewsFtpInfoService {

    private final BreakingNewsFtpInfoRepository breakingNewsFtpInfoRepository;

    private final BreakingNewsFtpInfoMapper breakingNewsFtpInfoMapper;

    public BreakingNewsFtpInfoDTO find(Long id){

        BreakingNewsFtpInfo breakingNewsFtpInfo = findBreakingNewsFtpInfo(id);

        BreakingNewsFtpInfoDTO breakingNewsFtpInfoDTO = breakingNewsFtpInfoMapper.toDto(breakingNewsFtpInfo);

        return breakingNewsFtpInfoDTO;

    }

    public BreakingNewsFtpInfo findBreakingNewsFtpInfo(Long id){

        Optional<BreakingNewsFtpInfo> entity = breakingNewsFtpInfoRepository.findFtpInfo(id);

        if (entity.isPresent() == false){
            throw new ResourceNotFoundException("속보뉴스 FPT 전송정보를 찾을 수 없습니다. 속보뉴스 FPT 전송정보 아이디 : " + id);
        }
        return entity.get();

    }
}
