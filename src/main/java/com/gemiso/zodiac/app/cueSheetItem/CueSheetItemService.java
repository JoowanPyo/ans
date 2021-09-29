package com.gemiso.zodiac.app.cueSheetItem;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemCreateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemUpdateDTO;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemCreateMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetItemService {

    private final CueSheetItemRepository cueSheetItemRepository;

    private final CueSheetItemMapper cueSheetItemMapper;
    private final CueSheetItemCreateMapper cueSheetItemCreateMapper;
    private final CueSheetItemUpdateMapper cueSheetItemUpdateMapper;

    private final UserAuthService userAuthService;

    public CueSheetItemDTO find(Long cueItemId){

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);

        return cueSheetItemDTO;

    }

    public Long create(CueSheetItemCreateDTO cueSheetItemCreateDTO, Long cueId){

        cueSheetItemCreateDTO.setCueId(cueId);
        String userId = userAuthService.authUser.getUserId();
        cueSheetItemCreateDTO.setInputrId(userId);

        CueSheetItem cueSheetItem = cueSheetItemCreateMapper.toEntity(cueSheetItemCreateDTO);
        cueSheetItemRepository.save(cueSheetItem); //큐시트아이템 등록

        //큐시트 아이템 순번 재등록
        Long cueItemId = cueSheetItem.getCueItemId();
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId);

        for (int i = 0; i < cueSheetItemList.size(); i++){
            if (cueItemId.equals(cueSheetItemList.get(i).getCueItemId())){
                cueSheetItemList.remove(i);//신규 등록된 큐시트 아이템 리스트에서 삭제
            }
        }

        cueSheetItemList.add(cueSheetItemCreateDTO.getCueItemOrd(), cueSheetItem); //신규등록하려는 큐시트 아이템 원하는 순번에 리스트 추가

        //조회된 큐시트 아이템 Ord 업데이트
        int index = 1;
        for (CueSheetItem cueSheetItems : cueSheetItemList){

            CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItems);
            cueSheetItemDTO.setCueItemOrd(index);
            CueSheetItem setCueSheetItem = cueSheetItemMapper.toEntity(cueSheetItemDTO);
            cueSheetItemRepository.save(setCueSheetItem);
            index++;
        }

        return cueSheetItem.getCueItemId();
    }

    public void update(CueSheetItemUpdateDTO cueSheetItemUpdateDTO, Long cueId, Long cueItemId){

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        cueSheetItemUpdateDTO.setCueId(cueId);
        cueSheetItemUpdateDTO.setCueItemId(cueItemId);
        String userId = userAuthService.authUser.getUserId();
        cueSheetItemUpdateDTO.setUpdtrId(userId);

        cueSheetItemUpdateMapper.updateFromDto(cueSheetItemUpdateDTO, cueSheetItem);

        cueSheetItemRepository.save(cueSheetItem);
    }

    public void delete(Long cueId, Long cueItemId){

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);

        String userId = userAuthService.authUser.getUserId();
        cueSheetItemDTO.setDelrId(userId);
        cueSheetItemDTO.setDelYn("Y");
        cueSheetItemDTO.setDelDtm(new Date());

        cueSheetItemMapper.updateFromDto(cueSheetItemDTO, cueSheetItem);

        cueSheetItemRepository.save(cueSheetItem);
    }

    public void ordUpdate(CueSheetItemUpdateDTO cueSheetItemUpdateDTO, Long cueId, Long cueItemId){


        cueSheetItemUpdateDTO.setCueItemId(cueItemId);

        /*String userId = authAddService.authUser.getUserId(); //순서변경도 수정자 아이디 등록?
        cueSheetItemUpdateDTO.setUpdtrId(userId);*/

        CueSheetItem cueSheetItem = cueSheetItemUpdateMapper.toEntity(cueSheetItemUpdateDTO);
        cueSheetItemRepository.save(cueSheetItem); //큐시트아이템 등록

        //큐시트 아이템 순번 재등록
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId);

        for (int i = 0; i < cueSheetItemList.size(); i++){
            if (cueItemId.equals(cueSheetItemList.get(i).getCueItemId())){
                cueSheetItemList.remove(i);//신규 등록된 큐시트 아이템 리스트에서 삭제
            }
        }

        cueSheetItemList.add(cueSheetItemUpdateDTO.getCueItemOrd(), cueSheetItem); //신규등록하려는 큐시트 아이템 원하는 순번에 리스트 추가

        //조회된 큐시트 아이템 Ord 업데이트
        int index = 1;
        for (CueSheetItem cueSheetItems : cueSheetItemList){

            CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItems);
            cueSheetItemDTO.setCueItemOrd(index);
            CueSheetItem setCueSheetItem = cueSheetItemMapper.toEntity(cueSheetItemDTO);
            cueSheetItemRepository.save(setCueSheetItem);
            index++;
        }

    }

    public CueSheetItem cueItemFindOrFail(Long cueItemId){

        Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findByCueItem(cueItemId);

        if (!cueSheetItem.isPresent()){
            throw new ResourceNotFoundException("CueSheetItem not found. CueSheet Item Id : " + cueItemId);
        }

        return cueSheetItem.get();

    }

}
