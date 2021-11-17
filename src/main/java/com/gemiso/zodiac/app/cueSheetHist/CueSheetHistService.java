package com.gemiso.zodiac.app.cueSheetHist;

import com.gemiso.zodiac.app.cueSheetHist.dto.CueSheetHistDTO;
import com.gemiso.zodiac.app.cueSheetHist.mapper.CueSheetHistCreateMapper;
import com.gemiso.zodiac.app.cueSheetHist.mapper.CueSheetHistMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetHistService {

    private final CueSheetHistRepository cueSheetHistRepository;

    private final CueSheetHistMapper cueSheetHistMapper;
    private final CueSheetHistCreateMapper cueSheetHistCreateMapper;


    //큐시트 이력 목록조회
    public List<CueSheetHistDTO> findAll(Long cueId, String cueAction){

        BooleanBuilder booleanBuilder = getSearch(cueId, cueAction);

        List<CueSheetHist> cueSheetHistList =
                (List<CueSheetHist>) cueSheetHistRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        List<CueSheetHistDTO> cueSheetHistDTOList = cueSheetHistMapper.toDtoList(cueSheetHistList);

        return cueSheetHistDTOList;
    }

    public BooleanBuilder getSearch(Long cueId, String cueAction){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueSheetHist qCueSheetHist = QCueSheetHist.cueSheetHist;

        if (ObjectUtils.isEmpty(cueId) == false){
            booleanBuilder.and(qCueSheetHist.cueSheet.cueId.eq(cueId));
        }
        if (StringUtils.isEmpty(cueAction) == false){
            booleanBuilder.and(qCueSheetHist.cueAction.eq(cueAction));
        }

        return booleanBuilder;
    }

    //큐시트 이력 상세조회
    public CueSheetHistDTO find(Long cueHistId){

        CueSheetHist cueSheetHist = findCueHist(cueHistId);

        CueSheetHistDTO cueSheetHistDTO = cueSheetHistMapper.toDto(cueSheetHist);

        return cueSheetHistDTO;
    }

    //큐시트 이력 조회 및 존재유무 확인.
    public CueSheetHist findCueHist(Long cueHistId){

        Optional<CueSheetHist> cueSheetHist = cueSheetHistRepository.findCueHist(cueHistId);

        if (cueSheetHist.isPresent() == false){
            throw new ResourceNotFoundException("큐시트 이력을 찾을 수 없습니다. 큐시트 이력 아이디 : "+cueHistId);
        }

        return cueSheetHist.get();
    }
}
