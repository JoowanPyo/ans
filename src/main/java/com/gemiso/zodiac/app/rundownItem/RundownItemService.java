package com.gemiso.zodiac.app.rundownItem;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.rundown.Rundown;
import com.gemiso.zodiac.app.rundownItem.dto.RundownItemCreateDTO;
import com.gemiso.zodiac.app.rundownItem.dto.RundownItemDTO;
import com.gemiso.zodiac.app.rundownItem.dto.RundownItemUpdateDTO;
import com.gemiso.zodiac.app.rundownItem.mapper.RundownItemCreateMapper;
import com.gemiso.zodiac.app.rundownItem.mapper.RundownItemMapper;
import com.gemiso.zodiac.app.rundownItem.mapper.RundownItemUpdateMapper;
import com.gemiso.zodiac.core.helper.PageHelper;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RundownItemService {

    private final RundownItemRepository rundownItemRepository;

    private final RundownItemMapper rundownItemMapper;
    private final RundownItemCreateMapper rundownItemCreateMapper;
    private final RundownItemUpdateMapper rundownItemUpdateMapper;


    //런다운 아이템 목록 조회
    public List<RundownItemDTO> findAll(Long rundownId){

        List<RundownItem> rundownItems = rundownItemRepository.findAllItems(rundownId);

        List<RundownItemDTO> rundownItemDTOS = rundownItemMapper.toDtoList(rundownItems);

        return rundownItemDTOS;
    }

    //런다운 아이템 목록 조회 [홈 화면]
    public PageResultDTO<RundownItemDTO, RundownItem> findMyRundown(Date sdate, Date edate, String rptrId, Integer page, Integer limit){

        //페이지 셋팅 page, limit null일시 page = 1 limit = 50 디폴트 셋팅
        PageHelper pageHelper = new PageHelper(page, limit);
        Pageable pageable = pageHelper.getArticlePageInfo();

        Page<RundownItem> rundownItems = rundownItemRepository.findMyRundown(sdate, edate, rptrId, pageable);

        Function<RundownItem, RundownItemDTO> fn = (entity -> rundownItemMapper.toDto(entity));

        return new PageResultDTO<RundownItemDTO, RundownItem>(rundownItems, fn);
    }

    //런다운 아이템 조회
    public RundownItemDTO find(Long rundownItemId){

        RundownItem rundownItem = rundownItemFindOrFail(rundownItemId);

        RundownItemDTO rundownItemDTO = rundownItemMapper.toDto(rundownItem);

        return rundownItemDTO;
    }

    //런다운 아이템 등록
    public Long create(RundownItemCreateDTO rundownItemCreateDTO, String userId){

        /*RundownSimpleDTO rundownSimpleDTO = rundownItemCreateDTO.getRundown();

        Long rundownId = rundownSimpleDTO.getRundownId();


*/
        RundownItem rundownItem = rundownItemCreateMapper.toEntity(rundownItemCreateDTO);

        rundownItemRepository.save(rundownItem);

        Rundown rundown  = rundownItem.getRundown();
        Long rundownId = rundown.getRundownId();
        Long rundownItemId = rundownItem.getRundownItemId();
        Integer ord = rundownItem.getRundownItemOrd();

        ordUpdate(rundownId, rundownItemId, ord);

        return rundownItemId;

    }

    //런다운 아이템 업데이트
    public void update(RundownItemUpdateDTO rundownItemUpdateDTO, Long rundownItemId){


        RundownItem rundownItem = rundownItemFindOrFail(rundownItemId);

        rundownItemUpdateMapper.updateFromDto(rundownItemUpdateDTO, rundownItem);

        rundownItemRepository.save(rundownItem);


    }

    //런다운 아이템 삭제
    public void delete(Long rundownItemId){

        RundownItem rundownItem = rundownItemFindOrFail(rundownItemId);

        Rundown rundown = rundownItem.getRundown();
        Long rundownId = rundown.getRundownId();

        rundownItemRepository.deleteById(rundownItemId);

        deleteOrdUpdate(rundownId);
    }

    //런다운 아이템 순서 변경
    public void ordUpdate(Long rundownId, Long rundownItemId, Integer rundownItemOrd){

        RundownItem rundownItem = rundownItemFindOrFail(rundownItemId);

        RundownItemDTO rundownItemDTO = rundownItemMapper.toDto(rundownItem);
        rundownItemDTO.setRundownItemOrd(rundownItemOrd);

        rundownItemMapper.updateFromDto(rundownItemDTO, rundownItem);

        rundownItemRepository.save(rundownItem);

        List<RundownItem> rundownItems = rundownItemRepository.findByRundownId(rundownId);

        for (int i = rundownItems.size() - 1; i >= 0; i-- ){

            Long chkId = rundownItems.get(i).getRundownItemId();
            if (rundownItemId.equals(chkId) == false){
                continue;
            }

            rundownItems.remove(i);
        }

        rundownItems.add(rundownItemOrd, rundownItem);

        int index = 0;

        for (RundownItem rundownItemEntity : rundownItems){

            RundownItemDTO itemDTO = rundownItemMapper.toDto(rundownItemEntity);
            itemDTO.setRundownItemOrd(index);
            RundownItem item = rundownItemMapper.toEntity(itemDTO);
            rundownItemRepository.save(item);
            index++;
        }
    }

    //런다운 아이템 순서 변경 [ 삭제 ]
    public void deleteOrdUpdate(Long rundownId){

        List<RundownItem> rundownItems = rundownItemRepository.findByRundownId(rundownId);

        int index = 0;

        for (RundownItem rundownItemEntity : rundownItems){

            RundownItemDTO itemDTO = rundownItemMapper.toDto(rundownItemEntity);
            itemDTO.setRundownItemOrd(index);
            RundownItem item = rundownItemMapper.toEntity(itemDTO);
            rundownItemRepository.save(item);
            index++;
        }
    }

    //런다운 아이템 조회 및 존재 유무 확인
    public RundownItem rundownItemFindOrFail(Long rundownItemId){

        Optional<RundownItem> rundownItemEntity = rundownItemRepository.findRundownItem(rundownItemId);

        if (rundownItemEntity.isPresent() == false){
            throw new ResourceNotFoundException("런다운 아이템을 찾을 수 없습니다. 런다운 아이템 아이디 : "+rundownItemId);
        }

        return rundownItemEntity.get();
    }
}
