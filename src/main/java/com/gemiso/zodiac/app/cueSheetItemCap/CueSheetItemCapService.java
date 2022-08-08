package com.gemiso.zodiac.app.cueSheetItemCap;

import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapUpdateDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.mapper.CueSheetItemCapCreateMapper;
import com.gemiso.zodiac.app.cueSheetItemCap.mapper.CueSheetItemCapMapper;
import com.gemiso.zodiac.app.cueSheetItemCap.mapper.CueSheetItemCapUpdateMapper;
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
public class CueSheetItemCapService {

    private final CueSheetItemCapRepotitory cueSheetItemCapRepotitory;

    private final CueSheetItemCapMapper cueSheetItemCapMapper;
    private final CueSheetItemCapCreateMapper cueSheetItemCapCreateMapper;
    private final CueSheetItemCapUpdateMapper cueSheetItemCapUpdateMapper;



    public List<CueSheetItemCapDTO> findAll(Long cueId, Long cueItemId, String cueItemCapDivCd){

        //수정. 여기서도 cueId가 왜들어가는지? 자막미리보기?

        BooleanBuilder booleanBuilder = getSearch(cueItemId, cueItemCapDivCd);

        List<CueSheetItemCap> cueSheetItemCapList = (List<CueSheetItemCap>) cueSheetItemCapRepotitory
                .findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "capOrd", "lnNo" ));

        List<CueSheetItemCapDTO> cueSheetItemCapDTOList = cueSheetItemCapMapper.toDtoList(cueSheetItemCapList);

        return cueSheetItemCapDTOList;
    }

    public CueSheetItemCapDTO find(Long cueItemId, Long cueItemCapId){

        //수정. 큐시트 아이템을 조회해서 큐시트 아이템 존재 유무 확인?? cueItemId..

        CueSheetItemCap cueSheetItemCap = cueItemCapFindOrFail(cueItemCapId);

        CueSheetItemCapDTO cueSheetItemCapDTO = cueSheetItemCapMapper.toDto(cueSheetItemCap);

        return cueSheetItemCapDTO;
    }

    public Long create(CueSheetItemCapCreateDTO cueSheetItemCapCreateDTO, Long cueId, Long cueItemId, String userId){

        //수정. cueId???, cueItemCapDivCd값으로 큐시트아이템아티클캡 업데이트

        cueSheetItemCapCreateDTO.setCueItemId(cueItemId);//큐아이템 아이디 추가.
        cueSheetItemCapCreateDTO.setInputrId(userId);//등록자 아이디 추가.

        CueSheetItemCap cueSheetItemCap = cueSheetItemCapCreateMapper.toEntity(cueSheetItemCapCreateDTO);

        cueSheetItemCapRepotitory.save(cueSheetItemCap);

        return cueSheetItemCap.getCueItemCapId();

    }


    public void createList(List<CueSheetItemCapCreateDTO> cueSheetItemCapCreateDTOList, Long cueId, Long cueItemId, String userId){

        if (ObjectUtils.isEmpty(cueSheetItemCapCreateDTOList) == false) {
            log.error("CueSheet Item Cap CreateDTO List is empty"+" cueId - "+cueId+" CueItemId - "+cueItemId);
            return;
        }

        String divcd = cueSheetItemCapCreateDTOList.get(0).getCueItemCapDivCd();
        cueSheetItemCapRepotitory.deleteCueSheeItemCap(cueItemId, divcd);

        for (CueSheetItemCapCreateDTO cueSheetItemCapCreateDTO : cueSheetItemCapCreateDTOList){ //큐시트 아이템 자막 저장
            cueSheetItemCapCreateDTO.setCueItemId(cueItemId);

            cueSheetItemCapCreateDTO.setInputrId(userId);//등록자 아이디 추가.

            CueSheetItemCap cueSheetItemCap = cueSheetItemCapCreateMapper.toEntity(cueSheetItemCapCreateDTO);
            cueSheetItemCapRepotitory.save(cueSheetItemCap);
        }
        //수정. cueId 큐시트 아이디로?????

    }

    public void update(CueSheetItemCapUpdateDTO cueSheetItemCapUpdateDTO, Long cueId, Long cueItemId
            , Long cueItemCapId, String userId){

        CueSheetItemCap cueSheetItemCap = cueItemCapFindOrFail(cueItemCapId);

        cueSheetItemCapUpdateDTO.setCueItemId(cueItemId);//큐아이템 아이디 등록

        cueSheetItemCapUpdateDTO.setUpdtrId(userId);

        cueSheetItemCapUpdateMapper.updateFromDto(cueSheetItemCapUpdateDTO, cueSheetItemCap);

        cueSheetItemCapRepotitory.save(cueSheetItemCap);
    }

    public void delete(Long cueId, Long cueItemId, Long cueItemCapId){

        CueSheetItemCap cueSheetItemCap = cueItemCapFindOrFail(cueItemCapId);

        cueSheetItemCapRepotitory.deleteCueSheeItemCapId(cueItemId, cueItemCapId);

    }

    private BooleanBuilder getSearch(Long cueItemId, String cueItemCapDivCd) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

         QCueSheetItemCap qCueItemCap = QCueSheetItemCap.cueSheetItemCap;

         booleanBuilder.and(qCueItemCap.cueSheetItem.cueItemId.eq(cueItemId));

        if(cueItemCapDivCd != null && cueItemCapDivCd.trim().isEmpty() == false){
            booleanBuilder.and(qCueItemCap.cueItemCapDivCd.eq(cueItemCapDivCd));
        }

        return booleanBuilder;
    }

    public CueSheetItemCap cueItemCapFindOrFail(Long cueItemCapId){

        /*return userGroupRepository.findById(userGrpId)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroupId not found. UserGroupId : " + userGrpId));*/

        Optional<CueSheetItemCap> cueItemCap = cueSheetItemCapRepotitory.findByCueItemCap(cueItemCapId);
        if (!cueItemCap.isPresent()){
            throw new ResourceNotFoundException("큐시트 아이템 자막을 찾을 수 없습니다. 큐시트 아이템 자막 아이디 :" + cueItemCapId);
        }

        return cueItemCap.get();
    }
}
