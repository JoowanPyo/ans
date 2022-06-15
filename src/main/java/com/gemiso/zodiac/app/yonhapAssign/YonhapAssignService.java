package com.gemiso.zodiac.app.yonhapAssign;

import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignCreateDTO;
import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignDTO;
import com.gemiso.zodiac.app.yonhapAssign.dto.YonhapAssignUpdateDTO;
import com.gemiso.zodiac.app.yonhapAssign.mapper.YonhapAssignCreateMapper;
import com.gemiso.zodiac.app.yonhapAssign.mapper.YonhapAssignMapper;
import com.gemiso.zodiac.app.yonhapAssign.mapper.YonhapAssignUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
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
public class YonhapAssignService {

    private final YonhapAssignRepository yonhapAssignRepository;

    private final YonhapAssignMapper yonhapAssignMapper;
    private final YonhapAssignCreateMapper yonhapAssignCreateMapper;
    private final YonhapAssignUpdateMapper yonhapAssignUpdateMapper;

    private final UserAuthService userAuthService;


    public List<YonhapAssignDTO> findAll(Date sdate, Date edate, Long yonhapId, Long wireId, String designatorId, String assignerId){

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, yonhapId, wireId, designatorId, assignerId);

        List<YonhapAssign> yonhapAssignList = (List<YonhapAssign>) yonhapAssignRepository.findAll(booleanBuilder,
                Sort.by(Sort.Direction.ASC, "inputDtm"));

        List<YonhapAssignDTO> yonhapAssignDTOList = yonhapAssignMapper.toDtoList(yonhapAssignList);

        return yonhapAssignDTOList;
    }

    //목록조회 검색조건 빌드.
    public BooleanBuilder getSearch(Date sdate, Date edate, Long yonhapId, Long wireId, String designatorId, String assignerId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QYonhapAssign qYonhapAssign = QYonhapAssign.yonhapAssign;

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){
            booleanBuilder.and(qYonhapAssign.inputDtm.between(sdate, edate));
        }
        if(ObjectUtils.isEmpty(yonhapId) == false){
            booleanBuilder.and(qYonhapAssign.yonhap.yonhapId.eq(yonhapId));
        }
        if(ObjectUtils.isEmpty(wireId) == false){
            booleanBuilder.and(qYonhapAssign.yonhapWire.wireId.eq(wireId));
        }

        if(designatorId != null && designatorId.trim().isEmpty() == false){
            booleanBuilder.and(qYonhapAssign.designatorId.eq(designatorId));
        }
        if(assignerId != null && assignerId.trim().isEmpty() == false){
            booleanBuilder.and(qYonhapAssign.assignerId.eq(assignerId));
        }
        return booleanBuilder;
    }

    public YonhapAssignDTO find(Long assignId){ //연합담당자지정 서비스 상세조회

        YonhapAssign yonhapAssign = findYonhapAssign(assignId);

        YonhapAssignDTO yonhapAssignDTO = yonhapAssignMapper.toDto(yonhapAssign);

        return yonhapAssignDTO;
    }

    public Long create(YonhapAssignCreateDTO yonhapAssignCreateDTO){

        //연합기사 담당자 지정자 아이디set
        String userId = userAuthService.authUser.getUserId();
        yonhapAssignCreateDTO.setDesignatorId(userId);

        YonhapAssign yonhapAssign = yonhapAssignCreateMapper.toEntity(yonhapAssignCreateDTO);

        yonhapAssignRepository.save(yonhapAssign);

        return yonhapAssign.getAssignId();
    }

    public void update(YonhapAssignUpdateDTO yonhapAssignUpdateDTO, Long assignId){//연합담당자지정 수정

        YonhapAssign yonhapAssign = findYonhapAssign(assignId);

        yonhapAssignUpdateMapper.updateFromDto(yonhapAssignUpdateDTO, yonhapAssign);

        yonhapAssignRepository.save(yonhapAssign);

    }
    
    public void delete(Long assignId){

        YonhapAssign yonhapAssign = findYonhapAssign(assignId); //아이디로 존재여부 확인.
        
        yonhapAssignRepository.deleteById(assignId); //삭제
        
    }

    public YonhapAssign findYonhapAssign(Long assignId){ //연합담당자지정 단건 조회 및 등록여부 체크

        Optional<YonhapAssign> yonhapAssign = yonhapAssignRepository.findById(assignId);

        if (yonhapAssign.isPresent() == false){
            throw new ResourceNotFoundException("연합승인 정보를 찾을 수 없습니다. 연합 승인 아이디: " + assignId);
        }

        return yonhapAssign.get();

    }
}
