package com.gemiso.zodiac.app.spareCueSheetItem;

import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import com.gemiso.zodiac.app.spareCueSheetItem.dto.SpareCueSheetItemCreateDTO;
import com.gemiso.zodiac.app.spareCueSheetItem.dto.SpareCueSheetItemDTO;
import com.gemiso.zodiac.app.spareCueSheetItem.dto.SpareCueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.spareCueSheetItem.mapper.SpareCueSheetItemCreateMapper;
import com.gemiso.zodiac.app.spareCueSheetItem.mapper.SpareCueSheetItemMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SpareCueSheetItemService {

    private final SpareCueSheetItemRepository spareCueSheetItemRepository;

    private final SpareCueSheetItemMapper spareCueSheetItemMapper;
    private final SpareCueSheetItemCreateMapper spareCueSheetItemCreateMapper;

    private final UserAuthService userAuthService;


    //예비 큐시트 아이템 목록조회
    public List<SpareCueSheetItemDTO> findAll(Long cueId){

        BooleanBuilder booleanBuilder = getSearch(cueId); //조회조건 빌드

        //빌드된 조회조건으로 예비 큐시트 아이템 목록조회
        List<SpareCueSheetItem> spareCueSheetItemList =
                (List<SpareCueSheetItem>) spareCueSheetItemRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cueItemOrd"));

        //조회된 리스트 엔티티를 DTO 리스트로 변환.
        List<SpareCueSheetItemDTO> spareCueSheetItemDTOList = spareCueSheetItemMapper.toDtoList(spareCueSheetItemList);

        //DTO리스트 리턴
        return spareCueSheetItemDTOList;
    }

    //예비 큐시트 아이템 상세조회
    public SpareCueSheetItemDTO find(Long spareCueItemId){

        SpareCueSheetItem spareCueSheetItem = findSpareCueItem(spareCueItemId);//예비 큐시트 아이템 조회 및 존재 유무 확인.[조회조건 아이디]

        //리턴 시켜줄 예비 큐시트 아이템 DTO변환
        SpareCueSheetItemDTO spareCueSheetItemDTO = spareCueSheetItemMapper.toDto(spareCueSheetItem);

        return spareCueSheetItemDTO;
    }

    //예비 큐시트 아이템 등록
    public SpareCueSheetItemSimpleDTO create(SpareCueSheetItemCreateDTO spareCueSheetItemCreateDTO, Long cueId){

        String userId = userAuthService.authUser.getUserId();//토큰에서 현재 사용자 Id get
        spareCueSheetItemCreateDTO.setInputrId(userId); //입력자 등록.

        //예비 큐시트 아이템에 등록할 큐시트 아이디 빌드
        CueSheetSimpleDTO cueSheetSimpleDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();
        spareCueSheetItemCreateDTO.setCueSheet(cueSheetSimpleDTO);//큐시트 아이디 큐시트아이템에 set

        //예비 큐시트 아이템 등록을 위해 엔티티 변환
        SpareCueSheetItem spareCueSheetItem = spareCueSheetItemCreateMapper.toEntity(spareCueSheetItemCreateDTO);

        //등록
        spareCueSheetItemRepository.save(spareCueSheetItem);

        //리턴시켜줄 예비 큐시트 아이템 아이디
        Long spareCueItemId = spareCueSheetItem.getSpareCueItemId();
        SpareCueSheetItemSimpleDTO spareCueSheetItemSimpleDTO = new SpareCueSheetItemSimpleDTO();
        spareCueSheetItemSimpleDTO.setSpareCueItemId(spareCueItemId);

        return spareCueSheetItemSimpleDTO;
    }

    //예비 큐시트 아이템 조회 및 존재 유무 확인.[조회조건 아이디]
    public SpareCueSheetItem findSpareCueItem(Long spareCueItemId){

        //null포인트 방지를 위해 옵셔널로 조회
        Optional<SpareCueSheetItem> spareCueSheetItem = spareCueSheetItemRepository.findSareCueItem(spareCueItemId);

        if (spareCueSheetItem.isPresent() == false){ // 예비 큐시트 아이디로 조회시 조회건수가 없으면 에러
            throw new ResourceNotFoundException("예비 큐시트 아이템을 찾을 수 없습니다. 예비 큐시트 아이템 아이디 : "+spareCueItemId);
        }
        //조회된 예비 큐시트 아이템 리턴
        return spareCueSheetItem.get();
    }

    //예비 큐시트 아이템 목록조회 조회조건 빌드
    public BooleanBuilder getSearch(Long cueId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QSpareCueSheetItem qSpareCueSheetItem = QSpareCueSheetItem.spareCueSheetItem;

        //예비 큐시트 아이템 조회조건 삭제 여부값 N 디폴트
        booleanBuilder.and(qSpareCueSheetItem.delYn.eq("N"));

        //예비 큐시트 아이템 조회조건 큐시트 아이디
        if (ObjectUtils.isEmpty(cueId)){
            booleanBuilder.and(qSpareCueSheetItem.cueSheet.cueId.eq(cueId));
        }

        return booleanBuilder;
    }
}
