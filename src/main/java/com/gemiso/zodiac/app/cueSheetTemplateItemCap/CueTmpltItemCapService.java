package com.gemiso.zodiac.app.cueSheetTemplateItemCap;

import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapUpdateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.mapper.CueTmpltItemCapCreateMapper;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.mapper.CueTmpltItemCapMapper;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.mapper.CueTmpltItemCapUpdateMapper;
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
public class CueTmpltItemCapService {

    private final CueTmpltItemCapRepository cueTmpltItemCapRepository;

    private final CueTmpltItemCapMapper cueTmpltItemCapMapper;
    private final CueTmpltItemCapCreateMapper cueTmpltItemCapCreateMapper;
    private final CueTmpltItemCapUpdateMapper cueTmpltItemCapUpdateMapper;

    //private final UserAuthService userAuthService;

    //큐시트 템플릿 아이템 자막 목록조회
    public List<CueTmpltItemCapDTO> findAll(Long cueTmpltItemId, String cueItemCapDivCd){

        BooleanBuilder booleanBuilder = getSearch(cueTmpltItemId, cueItemCapDivCd);//큐시트 템플릿 아이템 자막 목록조회 조회조건 빌드

        List<CueTmpltItemCap> cueTmpltItemCapList = (List<CueTmpltItemCap>) cueTmpltItemCapRepository.findAll(
                booleanBuilder, Sort.by(Sort.Direction.ASC, "capOrd"));

        List<CueTmpltItemCapDTO> cueTmpltItemCapDTOList = cueTmpltItemCapMapper.toDtoList(cueTmpltItemCapList);

        return cueTmpltItemCapDTOList;
    }

    //큐시트 템플릿 아이템 자막 상세조회
    public CueTmpltItemCapDTO find(Long cueItemCapId){

        CueTmpltItemCap cueTmpltItemCap = findCueTmplItemCap(cueItemCapId); //큐시트 템플릿 아이템 자막 조회 및 존재유무 확인.

        CueTmpltItemCapDTO cueTmpltItemCapDTO = cueTmpltItemCapMapper.toDto(cueTmpltItemCap);

        return cueTmpltItemCapDTO;
    }

    //큐시트 템플릿 아이템 자막 등록
    public CueTmpltItemCapSimpleDTO create(CueTmpltItemCapCreateDTO cueTmpltItemCapCreateDTO, Long cueTmpltItemId, String userId){

        //String userId = userAuthService.authUser.getUserId(); //사용자 아이디 get
        cueTmpltItemCapCreateDTO.setInputrId(userId); //템플릿 아이템 자막 입력자 등록

        //큐시트 템플릿 아이템 아이디 빌드
        CueTmpltItemSimpleDTO cueTmpltItemSimpleDTO = CueTmpltItemSimpleDTO.builder()
                .cueTmpltItemId(cueTmpltItemId).build();
        cueTmpltItemCapCreateDTO.setCueTmpltItem(cueTmpltItemSimpleDTO); //큐시트 템플릿 아이템 아이디 set

        CueTmpltItemCap cueTmpltItemCap = cueTmpltItemCapCreateMapper.toEntity(cueTmpltItemCapCreateDTO);
        cueTmpltItemCapRepository.save(cueTmpltItemCap);

        //생선된 아이디 CueTmpltItemCapSimpleDTO로 리턴.
        Long cueItemCapId = cueTmpltItemCap.getCueItemCapId();
        CueTmpltItemCapSimpleDTO cueTmpltItemCapSimpleDTO = new CueTmpltItemCapSimpleDTO();
        cueTmpltItemCapSimpleDTO.setCueItemCapId(cueItemCapId);

        return cueTmpltItemCapSimpleDTO;
    }

    //큐시트 템플릿 아이템 자막 수정
    public void update(CueTmpltItemCapUpdateDTO cueTmpltItemCapUpdateDTO, Long cueItemCapId, String userId){

        CueTmpltItemCap cueTmpltItemCap = findCueTmplItemCap(cueItemCapId); //큐시트 템플릿 아이템 자막 조회 및 존재유무 확인.

        //String userId = userAuthService.authUser.getUserId(); //사용자 아이디 get
        cueTmpltItemCapUpdateDTO.setUpdtrId(userId);

        cueTmpltItemCapUpdateMapper.updateFromDto(cueTmpltItemCapUpdateDTO, cueTmpltItemCap);

        cueTmpltItemCapRepository.save(cueTmpltItemCap);
    }

    //큐시트 템플릿 아이템 자막 삭제
    public void delete(Long cueItemCapId, String userId){

        CueTmpltItemCap cueTmpltItemCap = findCueTmplItemCap(cueItemCapId); //큐시트 템플릿 아이템 자막 조회 및 존재유무 확인.

        CueTmpltItemCapDTO cueTmpltItemCapDTO = cueTmpltItemCapMapper.toDto(cueTmpltItemCap);

        //String userId = userAuthService.authUser.getUserId(); //사용자 아이디 get
        cueTmpltItemCapDTO.setDelrId(userId); //삭제자 등록
        cueTmpltItemCapDTO.setDelYn("Y"); //삭제여부값 "Y"
        cueTmpltItemCapDTO.setDelDtm(new Date());//삭제 일시 등록

        cueTmpltItemCapMapper.updateFromDto(cueTmpltItemCapDTO, cueTmpltItemCap);

        cueTmpltItemCapRepository.save(cueTmpltItemCap);

    }

    //큐시트 템플릿 아이템 순서변경
    public void updateCapOrd(Long cueTmpltItemId, Long cueItemCapId, int capOrd){

        CueTmpltItemCap cueTmpltItemCap = findCueTmplItemCap(cueItemCapId); //큐시트 템플릿 아이템 자막 조회 및 존재유무 확인.

        //순서변경할 ord값 set
        CueTmpltItemCapDTO cueTmpltItemCapDTO = cueTmpltItemCapMapper.toDto(cueTmpltItemCap);
        cueTmpltItemCapDTO.setCapOrd(capOrd);
        
        cueTmpltItemCapMapper.updateFromDto(cueTmpltItemCapDTO, cueTmpltItemCap);
        
        cueTmpltItemCapRepository.save(cueTmpltItemCap);
        
        //큐시트 템플릿 아이템 자막 리스트 조회[조회조건 큐시트 템플릿 아이템 아이디]
        List<CueTmpltItemCap> cueTmpltItemCapList = cueTmpltItemCapRepository.findCueTmpltItemCapList(cueTmpltItemId);

        for (int i = cueTmpltItemCapList.size()-1; i >= 0; i-- ){
            if (cueItemCapId.equals(cueTmpltItemCapList.get(i).getCueItemCapId()) == false){
                continue;
            }
            cueTmpltItemCapList.remove(i);//신규 등록된 큐시트 템플릿 아이템 자막 리스트에서 삭제
        }

        cueTmpltItemCapList.add(capOrd, cueTmpltItemCap);  //신규등록하려는 큐시트 템플릿 아이템 자막 원하는 순번에 리스트 추가
        
        //조회된 큐시트 아이템 ord업데이트
        int index = 0;
        for (CueTmpltItemCap cueTmpltItemCapEntity : cueTmpltItemCapList){

            cueTmpltItemCapEntity.setCapOrd(index);//순번 재등록
            cueTmpltItemCapRepository.save(cueTmpltItemCapEntity); //순번 업데이트
            index++;//순번 + 1
        }
    }

    //큐시트 템플릿 아이템 자막 조회 및 존재유무 확인.
    public CueTmpltItemCap findCueTmplItemCap(Long cueItemCapId){

        Optional<CueTmpltItemCap> cueTmpltItemCap = cueTmpltItemCapRepository.findCueTmpltItemCap(cueItemCapId);

        if (cueTmpltItemCap.isPresent() ==false){
            throw new ResourceNotFoundException("큐시트 템플릿 아이템 자막을 찾을 수 없읍니다. 아이디 : "+ cueItemCapId);
        }

        return cueTmpltItemCap.get();
    }

    //큐시트 템플릿 아이템 자막 목록조회 조회조건 빌드
    public BooleanBuilder getSearch(Long cueTmpltItemId, String cueItemCapDivCd){

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QCueTmpltItemCap qCueTmpltItemCap = QCueTmpltItemCap.cueTmpltItemCap;

        //큐시트 템플릿 아이템 아이디가 조회조건으로 들어온 경우.
        if (ObjectUtils.isEmpty(cueTmpltItemId) == false){
            booleanBuilder.and(qCueTmpltItemCap.cueTmpltItem.cueTmpltItemId.eq(cueTmpltItemId));
        }
        //큐시트 아이템 자막 구분 코드가 조회조건으로 들어온 경우.
        if (cueItemCapDivCd != null && cueItemCapDivCd.trim().isEmpty() == false){
            booleanBuilder.and(qCueTmpltItemCap.cueItemCapDivCd.eq(cueItemCapDivCd));
        }

        return booleanBuilder;
    }
}
