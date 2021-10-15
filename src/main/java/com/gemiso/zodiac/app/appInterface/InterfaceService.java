package com.gemiso.zodiac.app.appInterface;

import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetRepository;
import com.gemiso.zodiac.app.cueSheet.QCueSheet;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetDTO;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetMapper;
import com.gemiso.zodiac.app.cueSheetItem.*;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolDTO;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemSymbolMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class InterfaceService {

    private final CueSheetRepository cueSheetRepository;
    private final CueSheetItemRepository cueSheetItemRepository;
    private final CueSheetItemSymbolRepository cueSheetItemSymbolRepository;

    private final CueSheetMapper cueSheetMapper;
    private final CueSheetItemMapper cueSheetItemMapper;
    private final CueSheetItemSymbolMapper cueSheetItemSymbolMapper;


    public List<CueSheetDTO> cueFindAll(Date sdate, Date edate, Long brdcPgmId, String brdcPgmNm, String searchWord){

        BooleanBuilder booleanBuilder = getSearch( sdate,  edate,  brdcPgmId,  brdcPgmNm,  searchWord);

        List<CueSheet> cueSheets = (List<CueSheet>) cueSheetRepository.findAll(booleanBuilder);

        List<CueSheetDTO> cueSheetDTOList = cueSheetMapper.toDtoList(cueSheets);

        return cueSheetDTOList;

    }

    public List<CueSheetItemDTO> cueItemFindAll(Long artclId, Long cueId){

        BooleanBuilder booleanBuilder = cueItemGetSearch(artclId, cueId);

        List<CueSheetItem> cueSheetItemList = (List<CueSheetItem>) cueSheetItemRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cueItemOrd"));

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);

        cueSheetItemDTOList = setSymbol(cueSheetItemDTOList); //방송아이콘 맵핑 테이블 추가.

        return cueSheetItemDTOList;
    }

    public CueSheetDTO cueFind(Long cueId){

        CueSheet cueSheet = cueSheetFindOrFail(cueId);

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);

        return cueSheetDTO;

    }

    private BooleanBuilder getSearch(Date sdate, Date edate, Long brdcPgmId, String brdcPgmNm, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueSheet qCueSheet = QCueSheet.cueSheet;

        booleanBuilder.and(qCueSheet.delYn.eq("N"));

        if (!StringUtils.isEmpty(sdate) && !StringUtils.isEmpty(edate)){
            booleanBuilder.and(qCueSheet.inputDtm.between(sdate, edate));
        }
        if(!StringUtils.isEmpty(brdcPgmId)){
            booleanBuilder.and(qCueSheet.program.brdcPgmId.eq(brdcPgmId));
        }
        if(!StringUtils.isEmpty(brdcPgmNm)){
            booleanBuilder.and(qCueSheet.brdcPgmNm.contains(brdcPgmNm));
        }
        if(!StringUtils.isEmpty(searchWord)){
            booleanBuilder.and(qCueSheet.brdcPgmNm.contains(searchWord).or(qCueSheet.pd1.userNm.contains(searchWord))
                    .or(qCueSheet.pd2.userNm.contains(searchWord)));
        }

        return booleanBuilder;
    }

    public BooleanBuilder cueItemGetSearch(Long artclId, Long cueId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //dsl q쿼리 생성
        QCueSheetItem qCueSheetItem = QCueSheetItem.cueSheetItem;

        booleanBuilder.and(qCueSheetItem.delYn.eq("N"));
        //쿼리 where 조건 추가.
        if (StringUtils.isEmpty(artclId) == false){
            booleanBuilder.and(qCueSheetItem.article.artclId.eq(artclId));
        }
        //쿼리 where 조건 추가.
        if (StringUtils.isEmpty(cueId) == false){
            booleanBuilder.and(qCueSheetItem.cueId.eq(cueId));
        }


        return booleanBuilder;
    }

    public CueSheet cueSheetFindOrFail(Long cueId){

        Optional<CueSheet> cueSheet = cueSheetRepository.findByCue(cueId);

        if (!cueSheet.isPresent()){
            throw new ResourceNotFoundException("CueSheetId not found. cueSheetId : " + cueId);
        }

        return cueSheet.get();

    }

    public List<CueSheetItemDTO> setSymbol(List<CueSheetItemDTO> cueSheetItemDTOList){

        for (CueSheetItemDTO cueSheetItemDTO : cueSheetItemDTOList){ //조회된 아이템에 List

            Long cueItemId = cueSheetItemDTO.getCueItemId(); //아이템 아이디 get

            //아이템 아이디로 방송아이콘 맵핑테이블 조회
            List<CueSheetItemSymbol> cueSheetItemSymbolList = cueSheetItemSymbolRepository.findSymbol(cueItemId);

            if (ObjectUtils.isEmpty(cueSheetItemSymbolList) == false){

                List<CueSheetItemSymbolDTO> cueSheetItemSymbolDTO = cueSheetItemSymbolMapper.toDtoList(cueSheetItemSymbolList);

                cueSheetItemDTO.setCueSheetItemSymbolDTO(cueSheetItemSymbolDTO); //아이템에 set방송아이콘List

            }
        }

        return cueSheetItemDTOList;

    }
}
