package com.gemiso.zodiac.app.fileFtpInfo;

import com.gemiso.zodiac.app.fileFtpInfo.dto.FileFtpInfoDTO;
import com.gemiso.zodiac.app.fileFtpInfo.mapper.FileFtpInfoMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FileFtpInfoService {

    private final FileFtpInfoRepository fileFtpInfoRepository;

    private final FileFtpInfoMapper fileFtpInfoMapper;


    public FileFtpInfoDTO find(Long id){

        FileFtpInfo fileFtpInfo = findOrFail(id);

        FileFtpInfoDTO fileFtpInfoDTO = fileFtpInfoMapper.toDto(fileFtpInfo);

        return fileFtpInfoDTO;
    }

    public FileFtpInfo findOrFail(Long id){

        Optional<FileFtpInfo> fileFtpInfo = fileFtpInfoRepository.findFtpInfo(id);

        if (fileFtpInfo.isPresent() == false) {
            throw new ResourceNotFoundException("파일 FTP 전송 정보가 없습니다. id : " + id);
        }

        return fileFtpInfo.get();
    }
}
