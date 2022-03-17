package com.gemiso.zodiac.app.facilityManage;

import com.gemiso.zodiac.app.facilityManage.dto.FacilityManageCreateDTO;
import com.gemiso.zodiac.app.facilityManage.dto.FacilityManageDTO;
import com.gemiso.zodiac.app.facilityManage.dto.FacilityManageUpdateDTO;
import com.gemiso.zodiac.app.facilityManage.mapper.FacilityManageCreateMapper;
import com.gemiso.zodiac.app.facilityManage.mapper.FacilityManageMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class FacilityManageService {

    private final FacilityManageRepository facilityManageRepository;

    private final FacilityManageMapper facilityManageMapper;
    private final FacilityManageCreateMapper facilityManageCreateMapper;

    private final UserAuthService userAuthService;


    public List<FacilityManageDTO> findAll(String searchWord, String fcltyDivCd){ //시설관리 목록조회

        BooleanBuilder booleanBuilder = getSearch(searchWord, fcltyDivCd); //목록조회 조회조건 생성.

        //생성된 조회조건으로 시설관리 목록조회
        List<FacilityManage> facilityManageList = (List<FacilityManage>) facilityManageRepository.findAll(booleanBuilder);

        List<FacilityManageDTO> facilityManageDTOList = facilityManageMapper.toDtoList(facilityManageList);

        return facilityManageDTOList;

    }

    public FacilityManageDTO find(Long fcltyId){ //시설관리 상세조회.

        FacilityManage facilityManage = findFacility(fcltyId); //시설관리 등록여부 확인 및 엔티티 조회.

        FacilityManageDTO facilityManageDTO = facilityManageMapper.toDto(facilityManage);

        return facilityManageDTO;
    }

    public Long create(FacilityManageCreateDTO facilityManageCreateDTO){ //시설관리 등록

        //입력자 아이디 등록.
        String userId = userAuthService.authUser.getUserId();
        facilityManageCreateDTO.setInputrId(userId);

        //등록DTO 엔티티변환후 시설save.
        FacilityManage facilityManage = facilityManageCreateMapper.toEntity(facilityManageCreateDTO);
        facilityManageRepository.save(facilityManage);

        return facilityManage.getFcltyId(); //respornse 시설아이디.
    }

    //
    public void update(FacilityManageUpdateDTO facilityManageUpdateDTO, Long fcltyId){

    }

    public FacilityManage findFacility(Long fcltyId){ //시설관리 등록여부 확인 및 엔티티 조회.

        Optional<FacilityManage> facilityManage = facilityManageRepository.findById(fcltyId);

        if (facilityManage.isPresent() == false){
            throw new ResourceNotFoundException("등록된 시설이 없습니다. 시설 아이디 : "+ fcltyId);
        }

        return facilityManage.get();

    }

    public BooleanBuilder getSearch(String searchWord, String fcltyDivCd){ //목록조회조건 생성.

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QFacilityManage qFacilityManage = QFacilityManage.facilityManage;

        booleanBuilder.and(qFacilityManage.delYn.eq("N"));

        if (StringUtils.isEmpty(searchWord) == false){
            booleanBuilder.and(qFacilityManage.fcltyNm.contains(searchWord));
        }
        if (StringUtils.isEmpty(fcltyDivCd) == false){
            booleanBuilder.and(qFacilityManage.fcltyDivCd.eq(fcltyDivCd));
        }

        return booleanBuilder;
    }
}
