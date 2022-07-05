package com.gemiso.zodiac.app.cueSheetTemplateMedia;

import com.gemiso.zodiac.app.cueSheetTemplateMedia.dto.CueTmpltMediaCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.dto.CueTmpltMediaDTO;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.dto.CueTmpltMediaUpdateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.mapper.CueTmpltMediaCreateMapper;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.mapper.CueTmpltMediaMapper;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.mapper.CueTmpltMediaUpdateMapper;
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
public class CueTmpltMediaService {

    private final CueTmpltMediaRepository cueTmpltMediaRepository;

    private final CueTmpltMediaMapper cueTmpltMediaMapper;
    private final CueTmpltMediaCreateMapper cueTmpltMediaCreateMapper;
    private final CueTmpltMediaUpdateMapper cueTmpltMediaUpdateMapper;


    //큐시트 템플릿 미디어 목록조회
    public List<CueTmpltMediaDTO> findAll(Date sdate, Date edate, String trnsfFileNm, Long cueTmpltItemId, String mediaTypCd) {

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, trnsfFileNm, cueTmpltItemId, mediaTypCd);

        List<CueTmpltMedia> cueTmpltMediaList = (List<CueTmpltMedia>) cueTmpltMediaRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "mediaOrd"));

        List<CueTmpltMediaDTO> cueTmpltMediaDTOList = cueTmpltMediaMapper.toDtoList(cueTmpltMediaList);

        return cueTmpltMediaDTOList;
    }

    //큐시트 템플릿 미디어 상세조회
    public CueTmpltMediaDTO find(Long cueTmpltMediaId){

        CueTmpltMedia cueTmpltMedia = findCueTmpltMedia(cueTmpltMediaId);

        CueTmpltMediaDTO cueTmpltMediaDTO = cueTmpltMediaMapper.toDto(cueTmpltMedia);


        return cueTmpltMediaDTO;
    }

    //큐시트 템플릿 미디어 등록
    public CueTmpltMediaDTO create(CueTmpltMediaCreateDTO cueTmpltMediaCreateDTO, String userId){

        cueTmpltMediaCreateDTO.setInputrId(userId);

        CueTmpltMedia cueTmpltMedia = cueTmpltMediaCreateMapper.toEntity(cueTmpltMediaCreateDTO);

        cueTmpltMediaRepository.save(cueTmpltMedia);

        Long cueTmpltMediaId = cueTmpltMedia.getCueTmpltMediaId();
        Integer contId = cueTmpltMedia.getContId();

        CueTmpltMediaDTO cueTmpltMediaDTO = new CueTmpltMediaDTO();
        cueTmpltMediaDTO.setCueTmpltMediaId(cueTmpltMediaId);
        cueTmpltMediaDTO.setContId(contId);

        return cueTmpltMediaDTO;

    }

    //큐시트 미디어 수정
    public CueTmpltMediaDTO update(CueTmpltMediaUpdateDTO cueTmpltMediaUpdateDTO, Long cueTmpltMediaId, String userId) {

        CueTmpltMedia cueTmpltMedia = findCueTmpltMedia(cueTmpltMediaId);

        cueTmpltMediaUpdateDTO.setUpdtrId(userId);

        cueTmpltMediaUpdateMapper.updateFromDto(cueTmpltMediaUpdateDTO, cueTmpltMedia);

        cueTmpltMediaRepository.save(cueTmpltMedia);

        //Long cueTmpltMediaId = cueTmpltMedia.getCueTmpltMediaId();
        Integer contId = cueTmpltMedia.getContId();

        CueTmpltMediaDTO cueTmpltMediaDTO = new CueTmpltMediaDTO();
        cueTmpltMediaDTO.setCueTmpltMediaId(cueTmpltMediaId);
        cueTmpltMediaDTO.setContId(contId);

        return cueTmpltMediaDTO;

    }

    //큐시트 템플릿 미디어 삭제
    public void delete(Long cueTmpltMediaId, String userId) {

        CueTmpltMedia cueTmpltMedia = findCueTmpltMedia(cueTmpltMediaId);

        CueTmpltMediaDTO cueTmpltMediaDTO = cueTmpltMediaMapper.toDto(cueTmpltMedia);

        cueTmpltMediaDTO.setDelrId(userId);
        cueTmpltMediaDTO.setDelDtm(new Date());
        cueTmpltMediaDTO.setDelYn("Y");

        cueTmpltMediaMapper.updateFromDto(cueTmpltMediaDTO, cueTmpltMedia);

        cueTmpltMediaRepository.save(cueTmpltMedia);

    }

    //큐시트 템플릿 미디어 단건조회 및 존재유무 확인
    public CueTmpltMedia findCueTmpltMedia(Long cueTmpltMediaId){

        Optional<CueTmpltMedia> cueTmpltMedia = cueTmpltMediaRepository.findCueTmpltMedia(cueTmpltMediaId);

        if (cueTmpltMedia.isPresent() == false){
            throw new ResourceNotFoundException("큐시트 템플릿 미디어를 찾을 수 없습니다. CueSheet Template Media Id : "+cueTmpltMedia);
        }

        return cueTmpltMedia.get();
    }

    //목록조회 조건 빌드
    public BooleanBuilder getSearch(Date sdate, Date edate, String trnsfFileNm, Long cueTmpltItemId, String mediaTypCd){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueTmpltMedia qCueTmpltMedia = QCueTmpltMedia.cueTmpltMedia;

        booleanBuilder.and(qCueTmpltMedia.delYn.eq("N"));

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qCueTmpltMedia.inputDtm.between(sdate, edate));
        }
        if (trnsfFileNm != null && trnsfFileNm.trim().isEmpty() == false) {
            booleanBuilder.and(qCueTmpltMedia.trnsfFileNm.contains(trnsfFileNm));
        }
        if (ObjectUtils.isEmpty(cueTmpltItemId) == false){
            booleanBuilder.and(qCueTmpltMedia.cueTmpltItem.cueTmpltItemId.eq(cueTmpltItemId));
        }

        if (mediaTypCd != null && mediaTypCd.trim().isEmpty() == false){
            booleanBuilder.and(qCueTmpltMedia.mediaTypCd.eq(mediaTypCd));
        }


        return booleanBuilder;
    }
}
