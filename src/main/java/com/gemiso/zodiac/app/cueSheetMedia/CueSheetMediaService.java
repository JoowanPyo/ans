package com.gemiso.zodiac.app.cueSheetMedia;

import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaCreateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaUpdateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaCreateMapper;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaMapper;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaUpdateMapper;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetMediaService {

    private final CueSheetMediaRepository cueSheetMediaRepository;

    private final CueSheetMediaMapper cueSheetMediaMapper;
    private final CueSheetMediaCreateMapper cueSheetMediaCreateMapper;
    private final CueSheetMediaUpdateMapper cueSheetMediaUpdateMapper;

    private final UserAuthService userAuthService;

    public CueSheetMediaDTO find(Long cueMediaId){

        CueSheetMedia cueSheetMedia = cueSheetMediaFindOrFail(cueMediaId);

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaMapper.toDto(cueSheetMedia);

        return cueSheetMediaDTO;
    }

    public Long create(CueSheetMediaCreateDTO cueSheetMediaCreateDTO){

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        UserSimpleDTO userSimpleDTO = UserSimpleDTO.builder().userId(userId).build();
        cueSheetMediaCreateDTO.setInputr(userSimpleDTO);

        CueSheetMedia cueSheetMedia = cueSheetMediaCreateMapper.toEntity(cueSheetMediaCreateDTO);

        cueSheetMediaRepository.save(cueSheetMedia);

        return cueSheetMedia.getCueMediaId();
    }

    public void update(CueSheetMediaUpdateDTO cueSheetMediaUpdateDTO, Long cueMediaId){

        CueSheetMedia cueSheetMedia = cueSheetMediaFindOrFail(cueMediaId);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        UserSimpleDTO userSimpleDTO = UserSimpleDTO.builder().userId(userId).build();
        cueSheetMediaUpdateDTO.setUpdtr(userSimpleDTO);

        cueSheetMediaUpdateMapper.updateFromDto(cueSheetMediaUpdateDTO, cueSheetMedia);

        cueSheetMediaRepository.save(cueSheetMedia);

    }

    public void delete(Long cueMediaId){

        CueSheetMedia cueSheetMedia = cueSheetMediaFindOrFail(cueMediaId);

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaMapper.toDto(cueSheetMedia);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        UserSimpleDTO userSimpleDTO = UserSimpleDTO.builder().userId(userId).build();
        cueSheetMediaDTO.setDelr(userSimpleDTO);
        cueSheetMediaDTO.setDelDtm(new Date());
        cueSheetMediaDTO.setDelYn("Y");

        cueSheetMediaMapper.updateFromDto(cueSheetMediaDTO, cueSheetMedia);

        cueSheetMediaRepository.save(cueSheetMedia);

    }

    public CueSheetMedia cueSheetMediaFindOrFail(Long cueMediaId){

        Optional<CueSheetMedia> cueSheetMedia = cueSheetMediaRepository.findByCueSheetMedia(cueMediaId);

        if (!cueSheetMedia.isPresent()){
            throw new ResourceNotFoundException("해당 큐시트영상이 없습니다.");
        }

        return cueSheetMedia.get();

    }


}
