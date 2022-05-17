package com.gemiso.zodiac.app.cueSheetMedia;

import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaCreateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaUpdateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaCreateMapper;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaMapper;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaUpdateMapper;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.QCueTmpltMedia;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
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


    public List<CueSheetMediaDTO> findAll(Date sdate, Date edate, String trnsfFileNm, Long cueItemId, String mediaTypCd, String delYn){

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, trnsfFileNm, cueItemId, mediaTypCd, delYn);

        List<CueSheetMedia> cueSheetMedia = (List<CueSheetMedia>) cueSheetMediaRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "mediaOrd"));

        List<CueSheetMediaDTO> cueSheetMediaDTOList = cueSheetMediaMapper.toDtoList(cueSheetMedia);

        return cueSheetMediaDTOList;
    }

    //목록조회 조건 빌드
    public BooleanBuilder getSearch(Date sdate, Date edate, String trnsfFileNm, Long cueItemId, String mediaTypCd, String delYn){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueSheetMedia qCueSheetMedia = QCueSheetMedia.cueSheetMedia;

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qCueSheetMedia.inputDtm.between(sdate, edate));
        }
        if (trnsfFileNm != null && trnsfFileNm.trim().isEmpty() == false) {
            booleanBuilder.and(qCueSheetMedia.trnsfFileNm.contains(trnsfFileNm));
        }
        if (ObjectUtils.isEmpty(cueItemId) == false){
            booleanBuilder.and(qCueSheetMedia.cueSheetItem.cueItemId.eq(cueItemId));
        }

        if (mediaTypCd != null && mediaTypCd.trim().isEmpty() == false){
            booleanBuilder.and(qCueSheetMedia.mediaTypCd.eq(mediaTypCd));
        }

        if (delYn != null && delYn.trim().isEmpty() == false){
            booleanBuilder.and(qCueSheetMedia.delYn.eq(delYn));
        }


        return booleanBuilder;
    }

    public CueSheetMediaDTO find(Long cueMediaId){

        CueSheetMedia cueSheetMedia = cueSheetMediaFindOrFail(cueMediaId);

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaMapper.toDto(cueSheetMedia);

        return cueSheetMediaDTO;
    }

    public Long create(CueSheetMediaCreateDTO cueSheetMediaCreateDTO){

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        cueSheetMediaCreateDTO.setInputrId(userId);

        CueSheetMedia cueSheetMedia = cueSheetMediaCreateMapper.toEntity(cueSheetMediaCreateDTO);

        cueSheetMediaRepository.save(cueSheetMedia);

        return cueSheetMedia.getCueMediaId();
    }

    public void update(CueSheetMediaUpdateDTO cueSheetMediaUpdateDTO, Long cueMediaId){

        CueSheetMedia cueSheetMedia = cueSheetMediaFindOrFail(cueMediaId);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        cueSheetMediaUpdateDTO.setUpdtrId(userId);

        cueSheetMediaUpdateMapper.updateFromDto(cueSheetMediaUpdateDTO, cueSheetMedia);

        cueSheetMediaRepository.save(cueSheetMedia);

    }

    public void delete(Long cueMediaId){

        CueSheetMedia cueSheetMedia = cueSheetMediaFindOrFail(cueMediaId);

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaMapper.toDto(cueSheetMedia);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        cueSheetMediaDTO.setDelrId(userId);
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
