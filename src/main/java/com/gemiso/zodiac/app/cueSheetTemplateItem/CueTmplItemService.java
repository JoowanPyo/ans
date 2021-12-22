package com.gemiso.zodiac.app.cueSheetTemplateItem;

import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplateRepository;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmplItemCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmplItemDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmplItemSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmplItemUpdateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.mapper.CueTmplItemCreateMapper;
import com.gemiso.zodiac.app.cueSheetTemplateItem.mapper.CueTmplItemMapper;
import com.gemiso.zodiac.app.cueSheetTemplateItem.mapper.CueTmplItemUpdateMapper;
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
public class CueTmplItemService {

    private final CueTmplItemRepository cueTmplItemRepository;
    /*private final CueSheetTemplateRepository cueSheetTemplateRepository;*/

    private final CueTmplItemMapper cueTmplItemMapper;
    private final CueTmplItemCreateMapper cueTmplItemCreateMapper;
    private final CueTmplItemUpdateMapper cueTmplItemUpdateMapper;

    private final UserAuthService userAuthService;

    //큐시트 템플릿 아이템 목록조회
    public List<CueTmplItemDTO> findAll(Long cueTmpltId, String searchWord){

        BooleanBuilder booleanBuilder = getSearch(cueTmpltId, searchWord);  //큐시트 템플릿 아이템 목록조회 조건빌드

        //빌드된 목록조회 조건으로 큐시트 템플릿 아이템 목록조회
        List<CueTmplItem> cueTmplItems = 
                (List<CueTmplItem>) cueTmplItemRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cueItemOrd"));

        //큐시트템필릿 아이템 엔티티 리스트 DTO리스트로 변환
        List<CueTmplItemDTO> cueTmplItemDTOList = cueTmplItemMapper.toDtoList(cueTmplItems);
        
        return cueTmplItemDTOList;
    }

    //큐시트 템플릿 아이템 상세조회
    public CueTmplItemDTO find(Long cueTmpltItemId){

        CueTmplItem cueTmplItem = findCueTmplItem(cueTmpltItemId); //큐시트 템플릿 아이템 조회 및 존재유무 확인

        CueTmplItemDTO cueTmplItemDTO = cueTmplItemMapper.toDto(cueTmplItem);

        return cueTmplItemDTO;

    }

    //큐시트 템플릿 아이템 등록
    public CueTmplItemSimpleDTO create(CueTmplItemCreateDTO cueTmplItemCreateDTO, Long cueTmpltId){

        //큐시트 템플릿 아이디 빌드
        CueSheetTemplateSimpleDTO cueSheetTemplateSimpleDTO = CueSheetTemplateSimpleDTO.builder().cueTmpltId(cueTmpltId).build();
        String userId = userAuthService.authUser.getUserId(); //토큰에서 사장자 아이디 get
        cueTmplItemCreateDTO.setInputrId(userId); //입력자아이디  set
        cueTmplItemCreateDTO.setCueSheetTemplate(cueSheetTemplateSimpleDTO);//큐시트 템플릿 아이디  set

        CueTmplItem cueTmplItem = cueTmplItemCreateMapper.toEntity(cueTmplItemCreateDTO); //엔티티 변환

        cueTmplItemRepository.save(cueTmplItem); //등록

        Long cueTmpltItemId = cueTmplItem.getCueTmpltItemId(); //생성된 큐시트템플릿 아이템 아이디 get

        //리턴시켜줄 큐시트템플릿아이템 아이디를 DTO에 set
        CueTmplItemSimpleDTO cueTmplItemSimpleDTO = new CueTmplItemSimpleDTO();
        cueTmplItemSimpleDTO.setCueTmpltItemId(cueTmpltItemId);

        return cueTmplItemSimpleDTO;
    }

    //큐시트 템플릿 아이템 업데이트
    public CueTmplItemSimpleDTO update(CueTmplItemUpdateDTO cueTmplItemUpdateDTO, Long cueTmpltItemId){

        CueTmplItem cueTmplItem = findCueTmplItem(cueTmpltItemId); //큐시트 템플릿 아이템 조회 및 존재유무 확인

        String userId = userAuthService.authUser.getUserId(); //토큰에서 사장자 아이디 get
        cueTmplItemUpdateDTO.setUpdtrId(userId);//수정자 등록

        cueTmplItemUpdateMapper.updateFromDto(cueTmplItemUpdateDTO, cueTmplItem);

        cueTmplItemRepository.save(cueTmplItem);

        //리턴시켜줄 큐시트템플릿아이템 아이디를 DTO에 set
        CueTmplItemSimpleDTO cueTmplItemSimpleDTO = new CueTmplItemSimpleDTO();
        cueTmplItemSimpleDTO.setCueTmpltItemId(cueTmpltItemId);

        return cueTmplItemSimpleDTO;
    }

    //큐시트 템플릿 아이템 삭제
    public void delete(Long cueTmpltItemId){

        CueTmplItem cueTmplItem = findCueTmplItem(cueTmpltItemId); //큐시트 템플릿 아이템 조회 및 존재유무 확인

        CueTmplItemDTO cueTmplItemDTO = cueTmplItemMapper.toDto(cueTmplItem);

        String userId = userAuthService.authUser.getUserId(); //토큰에서 사장자 아이디 get
        cueTmplItemDTO.setDelrId(userId); // 삭제자 등록
        cueTmplItemDTO.setDelDtm(new Date()); //삭제일시 등록
        cueTmplItemDTO.setDelYn("Y"); //삭제여부 Y

        cueTmplItemMapper.updateFromDto(cueTmplItemDTO, cueTmplItem);

        cueTmplItemRepository.save(cueTmplItem);

    }

    //큐시트 템플릿 아이템 순변 변경
    public void ordUpdate(Long cueTmpltItemId, Long cueTmpltId, int cueItemOrd){

        CueTmplItem cueTmplItem = findCueTmplItem(cueTmpltItemId); //큐시트 템플릿 아이템 조회 및 존재유무 확인

        CueTmplItemDTO cueTmplItemDTO = cueTmplItemMapper.toDto(cueTmplItem);//수정을위해 엔티티 DTO 변환
        cueTmplItemDTO.setCueItemOrd(cueItemOrd); //큐시트 템플릿 아이템 순번 set

        cueTmplItemMapper.updateFromDto(cueTmplItemDTO, cueTmplItem); //순번 업데이트
        cueTmplItemRepository.save(cueTmplItem); //순번수정

        //큐시트 템플릿 리스트 조회
        List<CueTmplItem> cueTmplItemList = cueTmplItemRepository.findCueTmplItemList(cueTmpltId);

        //조회된 리스트중 순번 업데이트된 큐시트 템플릿 아이템 삭제
        for (int i = cueTmplItemList.size() -1; i >= 0; i--){
            if (cueTmpltItemId.equals(cueTmplItemList.get(i).getCueTmpltItemId() ) == false){
                continue;
            }
            cueTmplItemList.remove(i);
        }

        cueTmplItemList.add(cueItemOrd, cueTmplItem); // 신규등록한 큐시트 아이템 정확한 순번에 리스트 추가.

        //큐시트 템플릿 아이템 리스트 순번을 새로 정확하게 맞추어 등록.
        int index = 0;
        for (CueTmplItem cueTmplItem1 : cueTmplItemList){

            CueTmplItemDTO resetCueTmplItemDTO = cueTmplItemMapper.toDto(cueTmplItem1);
            resetCueTmplItemDTO.setCueItemOrd(index);
            CueTmplItem resetCueTmplItem = cueTmplItemMapper.toEntity(resetCueTmplItemDTO);
            cueTmplItemRepository.save(resetCueTmplItem);
            index++;
        }

    }

    //큐시트 템플릿 아이템 조회 및 존재유무 확인
    public CueTmplItem findCueTmplItem(Long cueTmpltItemId){

        //큐시트 템플릿 아이템 조회 [null error 방지를 위해 옵셔널로 조회]
        Optional<CueTmplItem> cueTmplItem = cueTmplItemRepository.findCueTmplItem(cueTmpltItemId);

        if (cueTmplItem.isPresent() == false){ //조회된 큐시트 템플릿 아이템이 없을 경우 error
            throw new ResourceNotFoundException("큐시트 템플릿 아이템을 찾을 수 없습니다. 큐시트 템플릿 아이템 아이디 : "+cueTmpltItemId);
        }

        return cueTmplItem.get(); //조회된 큐시트 템플릿 리턴
    }

    //큐시트 템플릿 아이템 목록조회 조건빌드
    public BooleanBuilder getSearch(Long cueTmpltId, String searchWord){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueTmplItem qCueTmplItem = QCueTmplItem.cueTmplItem;

        //검색조건이 큐시트템플릿 아이디가 들어올 경우
        if (ObjectUtils.isEmpty(cueTmpltId) == false){
            booleanBuilder.and(qCueTmplItem.cueSheetTemplate.cueTmpltId.eq(cueTmpltId));
        }
        //조회조건이 검색키워드로 들어왔을 경어[제목, 영문제목 contatins]
        if (searchWord != null && searchWord.trim().isEmpty() == false){
            booleanBuilder.and(qCueTmplItem.cueItemTitl.contains(searchWord).or(qCueTmplItem.cueItemTitlEn.contains(searchWord)));
        }

        return booleanBuilder;
    }
}
