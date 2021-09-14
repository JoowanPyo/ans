package com.gemiso.zodiac.app.CueItemCap;

import com.gemiso.zodiac.app.CueItemCap.dto.CueItemCapCreateDTO;
import com.gemiso.zodiac.app.CueItemCap.dto.CueItemCapDTO;
import com.gemiso.zodiac.app.CueItemCap.dto.CueItemCapUpdateDTO;
import com.gemiso.zodiac.app.CueItemCap.mapper.CueItemCapCreateMapper;
import com.gemiso.zodiac.app.CueItemCap.mapper.CueItemCapMapper;
import com.gemiso.zodiac.app.CueItemCap.mapper.CueItemCapUpdateMapper;
import com.gemiso.zodiac.app.userGroup.QUserGroup;
import com.gemiso.zodiac.app.userGroup.UserGroup;
import com.gemiso.zodiac.core.service.AuthAddService;
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
public class CueItemCapService {

    private final CueItemCapRepotitory cueItemCapRepotitory;

    private final CueItemCapMapper cueItemCapMapper;
    private final CueItemCapCreateMapper cueItemCapCreateMapper;
    private final CueItemCapUpdateMapper cueItemCapUpdateMapper;

    private final AuthAddService authAddService;



    public List<CueItemCapDTO> findAll(Long cueId, Long cueItemId, String cueItemCapDivCd){

        //수정. 여기서도 cueId가 왜들어가는지? 자막미리보기?

        BooleanBuilder booleanBuilder = getSearch(cueItemId, cueItemCapDivCd);

        List<CueItemCap> cueItemCapList = (List<CueItemCap>) cueItemCapRepotitory
                .findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "capOrd", "lnNo" ));

        List<CueItemCapDTO> cueItemCapDTOList = cueItemCapMapper.toDtoList(cueItemCapList);

        return cueItemCapDTOList;
    }

    public CueItemCapDTO find(Long cueItemId, Long cueItemCapId){

        //수정. 큐시트 아이템을 조회해서 큐시트 아이템 존재 유무 확인?? cueItemId..

        CueItemCap cueItemCap = cueItemCapFindOrFail(cueItemCapId);

        CueItemCapDTO cueItemCapDTO = cueItemCapMapper.toDto(cueItemCap);

        return cueItemCapDTO;
    }

    public Long create(CueItemCapCreateDTO cueItemCapCreateDTO, Long cueId, Long cueItemId){

        //수정. cueId???, cueItemCapDivCd값으로 큐시트아이템아티클캡 업데이트

        cueItemCapCreateDTO.setCueItemId(cueItemId);//큐아이템 아이디 추가.
        String userId = authAddService.authUser.getUserId();
        cueItemCapCreateDTO.setInputrId(userId);//등록자 아이디 추가.

        CueItemCap cueItemCap = cueItemCapCreateMapper.toEntity(cueItemCapCreateDTO);

        cueItemCapRepotitory.save(cueItemCap);

        return cueItemCap.getCueItemCapId();

    }


    public void createList(List<CueItemCapCreateDTO> cueItemCapCreateDTOList, Long cueId, Long cueItemId){

        if (!ObjectUtils.isEmpty(cueItemCapCreateDTOList)){

            cueItemCapRepotitory.deleteCueSheeItemCap(cueItemId, cueItemCapCreateDTOList.get(0).getCueItemCapDivCd());

            for (CueItemCapCreateDTO cueItemCapCreateDTO : cueItemCapCreateDTOList){ //큐시트 아이템 자막 저장
                cueItemCapCreateDTO.setCueItemId(cueItemId);

                String userId = authAddService.authUser.getUserId();
                cueItemCapCreateDTO.setInputrId(userId);//등록자 아이디 추가.

                CueItemCap cueItemCap = cueItemCapCreateMapper.toEntity(cueItemCapCreateDTO);
                cueItemCapRepotitory.save(cueItemCap);
            }
        }//수정. cueId 큐시트 아이디로?????

    }

    public void update(CueItemCapUpdateDTO cueItemCapUpdateDTO, Long cueId, Long cueItemId, Long cueItemCapId){

        CueItemCap cueItemCap = cueItemCapFindOrFail(cueItemCapId);

        cueItemCapUpdateDTO.setCueItemId(cueItemId);//큐아이템 아이디 등록

        String userId = authAddService.authUser.getUserId();
        cueItemCapUpdateDTO.setUpdtrId(userId);

        cueItemCapUpdateMapper.updateFromDto(cueItemCapUpdateDTO, cueItemCap);

        cueItemCapRepotitory.save(cueItemCap);
    }

    public void delete(Long cueId, Long cueItemId, Long cueItemCapId){

        CueItemCap cueItemCap = cueItemCapFindOrFail(cueItemCapId);

        cueItemCapRepotitory.deleteCueSheeItemCapId(cueItemId, cueItemCapId);

    }

    private BooleanBuilder getSearch(Long cueItemId, String cueItemCapDivCd) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

         QCueItemCap qCueItemCap = QCueItemCap.cueItemCap;

         booleanBuilder.and(qCueItemCap.cueItemId.eq(cueItemId));

        if(!StringUtils.isEmpty(cueItemCapDivCd)){
            booleanBuilder.and(qCueItemCap.cueItemCapDivCd.eq(cueItemCapDivCd));
        }

        return booleanBuilder;
    }

    public CueItemCap cueItemCapFindOrFail(Long cueItemCapId){

        /*return userGroupRepository.findById(userGrpId)
                .orElseThrow(() -> new ResourceNotFoundException("UserGroupId not found. UserGroupId : " + userGrpId));*/

        Optional<CueItemCap> cueItemCap = cueItemCapRepotitory.findByCueItemCap(cueItemCapId);
        if (!cueItemCap.isPresent()){
            throw new ResourceNotFoundException("Cuesheet item caption not found. Cuesheet Item caation Id :" + cueItemCapId);
        }

        return cueItemCap.get();
    }
}
