package com.gemiso.zodiac.app.cueSheetTemplateItem;

import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.dto.CueTmpltItemUpdateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItem.mapper.CueTmpltItemCreateMapper;
import com.gemiso.zodiac.app.cueSheetTemplateItem.mapper.CueTmpltItemMapper;
import com.gemiso.zodiac.app.cueSheetTemplateItem.mapper.CueTmpltItemUpdateMapper;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.CueTmpltItemCap;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.CueTmpltItemCapRepository;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.dto.CueTmpltItemCapDTO;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.mapper.CueTmpltItemCapCreateMapper;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.mapper.CueTmpltItemCapMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueTmpltItemService {

    private final CueTmpltItemRepository cueTmpltItemRepository;
    private final CueTmpltItemCapRepository cueTmpltItemCapRepository;
    /*private final CueSheetTemplateRepository cueSheetTemplateRepository;*/

    private final CueTmpltItemMapper cueTmpltItemMapper;
    private final CueTmpltItemCreateMapper cueTmpltItemCreateMapper;
    private final CueTmpltItemUpdateMapper cueTmpltItemUpdateMapper;
    private final CueTmpltItemCapCreateMapper cueTmpltItemCapCreateMapper;

    private final UserAuthService userAuthService;

    //큐시트 템플릿 아이템 목록조회
    public List<CueTmpltItemDTO> findAll(Long cueTmpltId, String searchWord){

        BooleanBuilder booleanBuilder = getSearch(cueTmpltId, searchWord);  //큐시트 템플릿 아이템 목록조회 조건빌드

        //빌드된 목록조회 조건으로 큐시트 템플릿 아이템 목록조회
        List<CueTmpltItem> cueTmpltItems =
                (List<CueTmpltItem>) cueTmpltItemRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cueItemOrd"));

        //큐시트템필릿 아이템 엔티티 리스트 DTO리스트로 변환
        List<CueTmpltItemDTO> cueTmpltItemDTOList = cueTmpltItemMapper.toDtoList(cueTmpltItems);
        
        return cueTmpltItemDTOList;
    }

    //큐시트 템플릿 아이템 상세조회
    public CueTmpltItemDTO find(Long cueTmpltItemId){

        CueTmpltItem cueTmpltItem = findCueTmplItem(cueTmpltItemId); //큐시트 템플릿 아이템 조회 및 존재유무 확인

        CueTmpltItemDTO cueTmpltItemDTO = cueTmpltItemMapper.toDto(cueTmpltItem);

        return cueTmpltItemDTO;

    }

    //큐시트 템플릿 아이템 등록
    public CueTmpltItemSimpleDTO create(CueTmpltItemCreateDTO cueTmpltItemCreateDTO, Long cueTmpltId){

        //큐시트 템플릿 아이디 빌드
        CueSheetTemplateSimpleDTO cueSheetTemplateSimpleDTO = CueSheetTemplateSimpleDTO.builder().cueTmpltId(cueTmpltId).build();
        String userId = userAuthService.authUser.getUserId(); //토큰에서 사장자 아이디 get
        cueTmpltItemCreateDTO.setInputrId(userId); //입력자아이디  set
        cueTmpltItemCreateDTO.setCueSheetTemplate(cueSheetTemplateSimpleDTO);//큐시트 템플릿 아이디  set

        CueTmpltItem cueTmpltItem = cueTmpltItemCreateMapper.toEntity(cueTmpltItemCreateDTO); //엔티티 변환

        cueTmpltItemRepository.save(cueTmpltItem); //등록

        Long cueTmpltItemId = cueTmpltItem.getCueTmpltItemId(); //생성된 큐시트템플릿 아이템 아이디 get

        //큐시트 템플릿 아이템 자막 리스트가 있을 경우.
        List<CueTmpltItemCapCreateDTO> cueTmpltItemCapCreateDTOList = cueTmpltItemCreateDTO.getCueTmpltItemCap();
        if (CollectionUtils.isEmpty(cueTmpltItemCapCreateDTOList) == false){

            //큐시트 템플릿 아이템 자막 등록.
            createCap(cueTmpltItemCapCreateDTOList, cueTmpltItemId, userId);

        }

        //리턴시켜줄 큐시트템플릿아이템 아이디를 DTO에 set
        CueTmpltItemSimpleDTO cueTmpltItemSimpleDTO = new CueTmpltItemSimpleDTO();
        cueTmpltItemSimpleDTO.setCueTmpltItemId(cueTmpltItemId);

        return cueTmpltItemSimpleDTO;
    }

    //큐시트 템플릿 아이템 자막 등록.
    public void createCap(List<CueTmpltItemCapCreateDTO> cueTmpltItemCapCreateDTOList, Long cueTmpltItemId, String userId){

        //큐시트 템플릿 아이템 아이디 빌드
        CueTmpltItemSimpleDTO cueTmpltItem = CueTmpltItemSimpleDTO.builder().cueTmpltItemId(cueTmpltItemId).build();

        //들어온 큐시트 템플릿 아이템 자막 전부 등록.
        for (CueTmpltItemCapCreateDTO cueTmpltItemCapCreateDTO : cueTmpltItemCapCreateDTOList){

            cueTmpltItemCapCreateDTO.setCueTmpltItem(cueTmpltItem); //큐시트 템플릿 아이템 아이디 set
            cueTmpltItemCapCreateDTO.setInputrId(userId);// 등록자 set

            CueTmpltItemCap cueTmpltItemCap = cueTmpltItemCapCreateMapper.toEntity(cueTmpltItemCapCreateDTO);
            cueTmpltItemCapRepository.save(cueTmpltItemCap); //큐시트 템플릿 아이템 자막 등록

        }

    }

    //큐시트 템플릿 아이템 업데이트
    public CueTmpltItemSimpleDTO update(CueTmpltItemUpdateDTO cueTmpltItemUpdateDTO, Long cueTmpltItemId){

        CueTmpltItem cueTmpltItem = findCueTmplItem(cueTmpltItemId); //큐시트 템플릿 아이템 조회 및 존재유무 확인

        String userId = userAuthService.authUser.getUserId(); //토큰에서 사장자 아이디 get
        cueTmpltItemUpdateDTO.setUpdtrId(userId);//수정자 등록

        cueTmpltItemUpdateMapper.updateFromDto(cueTmpltItemUpdateDTO, cueTmpltItem);

        cueTmpltItemRepository.save(cueTmpltItem);

        //UpdatqDTO에서 큐시트 템플릿 아이템 자막 리스트 get
        List<CueTmpltItemCapCreateDTO> cueTmpltItemCapCreateDTOList = cueTmpltItemUpdateDTO.getCueTmpltItemCap();
        //큐시트 템플릿 아이템 자막 리스트가 있으면 등록.
        if (CollectionUtils.isEmpty(cueTmpltItemCapCreateDTOList) == false){
            updateCap(cueTmpltItemCapCreateDTOList, cueTmpltItemId, userId);
        }

        //리턴시켜줄 큐시트템플릿아이템 아이디를 DTO에 set
        CueTmpltItemSimpleDTO cueTmpltItemSimpleDTO = new CueTmpltItemSimpleDTO();
        cueTmpltItemSimpleDTO.setCueTmpltItemId(cueTmpltItemId);

        return cueTmpltItemSimpleDTO;
    }

    //큐시트 템플릿 아이템 자막 리스트 업데이트
    public void updateCap(List<CueTmpltItemCapCreateDTO> cueTmpltItemCapCreateDTOList, Long cueTmpltItemId,String userId){

        //등록되어있는 큐시트 템플릿 아이템 자막 리스트 조회
        List<CueTmpltItemCap> cueTmpltItemCapList = cueTmpltItemCapRepository.findCueTmpltItemCapList(cueTmpltItemId);

        //등록되어있는 큐시트 템플릿 아이템 자막 삭제
        for (CueTmpltItemCap cueTmpltItemCap : cueTmpltItemCapList){

            Long id = cueTmpltItemCap.getCueItemCapId();

            cueTmpltItemCapRepository.deleteById(id);
        }

        //큐시트 템플릿 아이템 자막 재등록.
        createCap(cueTmpltItemCapCreateDTOList, cueTmpltItemId, userId);
    }

    //큐시트 템플릿 아이템 삭제
    public void delete(Long cueTmpltItemId){

        CueTmpltItem cueTmpltItem = findCueTmplItem(cueTmpltItemId); //큐시트 템플릿 아이템 조회 및 존재유무 확인

        CueTmpltItemDTO cueTmpltItemDTO = cueTmpltItemMapper.toDto(cueTmpltItem);

        String userId = userAuthService.authUser.getUserId(); //토큰에서 사장자 아이디 get
        cueTmpltItemDTO.setDelrId(userId); // 삭제자 등록
        cueTmpltItemDTO.setDelDtm(new Date()); //삭제일시 등록
        cueTmpltItemDTO.setDelYn("Y"); //삭제여부 Y

        cueTmpltItemMapper.updateFromDto(cueTmpltItemDTO, cueTmpltItem);

        cueTmpltItemRepository.save(cueTmpltItem);

    }

    //큐시트 템플릿 아이템 순변 변경
    public void ordUpdate(Long cueTmpltItemId, Long cueTmpltId, int cueItemOrd){

        CueTmpltItem cueTmpltItem = findCueTmplItem(cueTmpltItemId); //큐시트 템플릿 아이템 조회 및 존재유무 확인

        CueTmpltItemDTO cueTmpltItemDTO = cueTmpltItemMapper.toDto(cueTmpltItem);//수정을위해 엔티티 DTO 변환
        cueTmpltItemDTO.setCueItemOrd(cueItemOrd); //큐시트 템플릿 아이템 순번 set

        cueTmpltItemMapper.updateFromDto(cueTmpltItemDTO, cueTmpltItem); //순번 업데이트
        cueTmpltItemRepository.save(cueTmpltItem); //순번수정

        //큐시트 템플릿 리스트 조회
        List<CueTmpltItem> cueTmpltItemList = cueTmpltItemRepository.findCueTmplItemList(cueTmpltId);

        //조회된 리스트중 순번 업데이트된 큐시트 템플릿 아이템 삭제
        for (int i = cueTmpltItemList.size() -1; i >= 0; i--){
            if (cueTmpltItemId.equals(cueTmpltItemList.get(i).getCueTmpltItemId() ) == false){
                continue;
            }
            cueTmpltItemList.remove(i);
        }

        cueTmpltItemList.add(cueItemOrd, cueTmpltItem); // 신규등록한 큐시트 아이템 정확한 순번에 리스트 추가.

        //큐시트 템플릿 아이템 리스트 순번을 새로 정확하게 맞추어 등록.
        int index = 0;
        for (CueTmpltItem cueTmpltItem1 : cueTmpltItemList){

            CueTmpltItemDTO resetCueTmpltItemDTO = cueTmpltItemMapper.toDto(cueTmpltItem1);
            resetCueTmpltItemDTO.setCueItemOrd(index);
            CueTmpltItem resetCueTmpltItem = cueTmpltItemMapper.toEntity(resetCueTmpltItemDTO);
            cueTmpltItemRepository.save(resetCueTmpltItem);
            index++;
        }

    }

    //큐시트 템플릿 아이템 조회 및 존재유무 확인
    public CueTmpltItem findCueTmplItem(Long cueTmpltItemId){

        //큐시트 템플릿 아이템 조회 [null error 방지를 위해 옵셔널로 조회]
        Optional<CueTmpltItem> cueTmplItem = cueTmpltItemRepository.findCueTmplItem(cueTmpltItemId);

        if (cueTmplItem.isPresent() == false){ //조회된 큐시트 템플릿 아이템이 없을 경우 error
            throw new ResourceNotFoundException("큐시트 템플릿 아이템을 찾을 수 없습니다. 큐시트 템플릿 아이템 아이디 : "+cueTmpltItemId);
        }

        return cueTmplItem.get(); //조회된 큐시트 템플릿 리턴
    }

    //큐시트 템플릿 아이템 목록조회 조건빌드
    public BooleanBuilder getSearch(Long cueTmpltId, String searchWord){

        BooleanBuilder booleanBuilder = new BooleanBuilder();


        QCueTmpltItem qCueTmpltItem = QCueTmpltItem.cueTmpltItem;

        //검색조건이 큐시트템플릿 아이디가 들어올 경우
        if (ObjectUtils.isEmpty(cueTmpltId) == false){
            booleanBuilder.and(qCueTmpltItem.cueSheetTemplate.cueTmpltId.eq(cueTmpltId));
        }
        //조회조건이 검색키워드로 들어왔을 경어[제목, 영문제목 contatins]
        if (searchWord != null && searchWord.trim().isEmpty() == false){
            booleanBuilder.and(qCueTmpltItem.cueItemTitl.contains(searchWord).or(qCueTmpltItem.cueItemTitlEn.contains(searchWord)));
        }

        return booleanBuilder;
    }
}