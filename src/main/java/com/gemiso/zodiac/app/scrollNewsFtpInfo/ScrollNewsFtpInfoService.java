package com.gemiso.zodiac.app.scrollNewsFtpInfo;

import com.gemiso.zodiac.app.scrollNewsFtpInfo.dto.ScrollNewsFtpInfoDTO;
import com.gemiso.zodiac.app.scrollNewsFtpInfo.mapper.ScrollNewsFtpInfoMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScrollNewsFtpInfoService {

    private final ScrollNewsFtpInfoRepository scrollNewsFtpInfoRepository;

    private final ScrollNewsFtpInfoMapper scrollNewsFtpInfoMapper;


    public ScrollNewsFtpInfoDTO find(Long id){

        ScrollNewsFtpInfo scrollNewsFtpInfo = findScrollFtpInfo(id);

        ScrollNewsFtpInfoDTO scrollNewsFtpInfoDTO = scrollNewsFtpInfoMapper.toDto(scrollNewsFtpInfo);

        return scrollNewsFtpInfoDTO;

    }

    public ScrollNewsFtpInfo findScrollFtpInfo(Long id){

        Optional<ScrollNewsFtpInfo> scrollNewsFtpInfo = scrollNewsFtpInfoRepository.findScrollFtpInfo(id);

        if (scrollNewsFtpInfo.isPresent() == false){
            throw new ResourceNotFoundException("스크롤 뉴스 FTP전송 정보 를 찾을 수 없습니다. 스크롤 뉴스 FTP 전송 아이디 : " + id);
        }
        return scrollNewsFtpInfo.get();
    }
}
