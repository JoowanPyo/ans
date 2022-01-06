package com.gemiso.zodiac.app.spareCueSheetItem;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.article.dto.ArticleUpdateDTO;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemService;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemDTO;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SpareCueSheetItemService {

    //큐시트 아이템 서비스[기사 복사 서비스 이용하기 위해.]
    private final CueSheetItemService cueSheetItemService;

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

    //예비 큐시트 아이템 삭제
    public void delete(Long spareCueItemId){

        SpareCueSheetItem spareCueSheetItem = findSpareCueItem(spareCueItemId);//예비 큐시트 아이템 조회 및 존재 유무 확인.[조회조건 아이디]

        SpareCueSheetItemDTO spareCueSheetItemDTO = spareCueSheetItemMapper.toDto(spareCueSheetItem);
        String userId = userAuthService.authUser.getUserId();//토큰에서 현재 사용자 Id get
        spareCueSheetItemDTO.setDelrId(userId); //삭제자 set
        spareCueSheetItemDTO.setDelDtm(new Date()); //삭제일시 set
        spareCueSheetItemDTO.setDelYn("Y"); //삭제 여부값 Y

        spareCueSheetItemMapper.updateFromDto(spareCueSheetItemDTO, spareCueSheetItem);
        spareCueSheetItemRepository.save(spareCueSheetItem);

    }

    //예비 큐시트아이템 순서변경
    public void ordUpdate(Long cueId, Long spareCueItemId, int cueItemOrd){

        SpareCueSheetItem spareCueSheetItem = findSpareCueItem(spareCueItemId);//예비 큐시트 아이템 조회 및 존재 유무 확인.[조회조건 아이디]

        //조회된 예비 큐시트 아이템 DTO변환
        SpareCueSheetItemDTO spareCueSheetItemDTO = spareCueSheetItemMapper.toDto(spareCueSheetItem);
        spareCueSheetItemDTO.setCueItemOrd(cueItemOrd);//새로 들어온 예비 큐시트아이템 순번 set

        spareCueSheetItemMapper.updateFromDto(spareCueSheetItemDTO, spareCueSheetItem); //예비 큐시트아이템 순번 업데이트
        spareCueSheetItemRepository.save(spareCueSheetItem);//예비 큐시트아이템 업데이트

        //큐시트 아이디로 예비 큐시트 리스트 조회
        List<SpareCueSheetItem> spareCueSheetItemList = spareCueSheetItemRepository.findSareCueItemList(cueId);

        //새로 등록한 예비 큐시트아이템을 조회한 리스트에서 삭제
        for (int i = spareCueSheetItemList.size() -1; i >=0; i--){
            if (spareCueItemId.equals(spareCueSheetItemList.get(i).getSpareCueItemId()) == false){
                continue;
            }
            spareCueSheetItemList.remove(i);
        }

        //정확한 순번을위해 다시 새로등록한 예비 큐시트아이템 리스트에 add
        spareCueSheetItemList.add(cueItemOrd, spareCueSheetItem);

        //큐시트 아이템 ord설정할 index
        int index = 0;

        for(SpareCueSheetItem spareCueSheetItemEntity : spareCueSheetItemList){

            //예비 큐시트아이템 DTO변환
            SpareCueSheetItemDTO resetSpareCueSheetItemDTO = spareCueSheetItemMapper.toDto(spareCueSheetItemEntity);
            resetSpareCueSheetItemDTO.setCueItemOrd(index); //순번순차로 재등록
            //DTO to Entity변환
            SpareCueSheetItem resetSpareCueSheetItem = spareCueSheetItemMapper.toEntity(resetSpareCueSheetItemDTO);
            spareCueSheetItemRepository.save(resetSpareCueSheetItem);//등록
            index++;//index + 1
        }
    }

    public void createSpareCueItem(Long cueId, Long artclId, int cueItemOrd){

        //Article copyArtcl = cueSheetItemService.copyArticle(artclId);
        ArticleSimpleDTO articleSimpleDTO = ArticleSimpleDTO.builder().artclId(artclId).build();

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();

        //큐시트아이디 큐시트엔티티로 빌드 :: 예비 큐시트아이템에 큐시트 아이디set해주기 위해
        CueSheetSimpleDTO cueSheet = CueSheetSimpleDTO.builder().cueId(cueId).build();
        //예비 큐시트아이템create 빌드
        SpareCueSheetItemCreateDTO spareCueSheetItem = SpareCueSheetItemCreateDTO.builder()
                .cueSheet(cueSheet)
                .cueItemOrd(cueItemOrd)
                .inputrId(userId)
                .article(articleSimpleDTO)
                .build();

        /*spareCueSheetItem.setCueSheet(cueSheet);
        spareCueSheetItem.setCueItemOrd(cueItemOrd);
        spareCueSheetItem.setInputrId(userId);
        spareCueSheetItem.setArticle(articleSimpleDTO);*/

        SpareCueSheetItem spareCueSheetItemEntity = spareCueSheetItemCreateMapper.toEntity(spareCueSheetItem);

        //빌드된 예비 큐시트아이템 등록
        spareCueSheetItemRepository.save(spareCueSheetItemEntity);

        Long spareCueItemId = spareCueSheetItemEntity.getSpareCueItemId(); //생성된 예비 큐시트아이템 아이디 get

        ordUpdateDrop(spareCueSheetItemEntity, cueId, cueItemOrd);
    }

    public void ordUpdateDrop(SpareCueSheetItem spareCueSheetItem, Long cueId, int cueItemOrd){

        // 쿼리문 만들기
        BooleanBuilder booleanBuilder = getSearchOrd(cueId);

        //큐시트 아이템 순번 재등록
        List<SpareCueSheetItem> spareCueSheetItemList =
                (List<SpareCueSheetItem>) spareCueSheetItemRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cueItemOrd"));
        if(spareCueSheetItemList== null)
            return;

        //ClearCueSheetList(cueSheetItemList);

        Long cueSheetItemId = spareCueSheetItem.getSpareCueItemId();
        for (int i =  spareCueSheetItemList.size() - 1; i >= 0 ; i--){ //out of index때문에 역순으로 검사.
            if (!cueSheetItemId.equals(spareCueSheetItemList.get(i).getSpareCueItemId())){
                continue;
            }
            spareCueSheetItemList.remove(i);//신규 등록된 큐시트 아이템 리스트에서 삭제
        }

        spareCueSheetItemList.add(cueItemOrd, spareCueSheetItem); //신규등록하려는 큐시트 아이템 원하는 순번에 리스트 추가

        //조회된 큐시트 아이템 Ord 업데이트

        int index = 0;
        for (SpareCueSheetItem spareCueSheetItemEntity : spareCueSheetItemList){
            //예비 큐시트아이템 DTO변환
            SpareCueSheetItemDTO resetSpareCueSheetItemDTO = spareCueSheetItemMapper.toDto(spareCueSheetItemEntity);
            resetSpareCueSheetItemDTO.setCueItemOrd(index); //순번순차로 재등록
            //DTO to Entity변환
            SpareCueSheetItem resetSpareCueSheetItem = spareCueSheetItemMapper.toEntity(resetSpareCueSheetItemDTO);
            spareCueSheetItemRepository.save(resetSpareCueSheetItem);//등록
            index++;//index + 1

            index++;
        }
    }

    //예비 큐시트아이템 순서정렬을 위해 목록조회 조회조건 빌드[조회조건 큐시트 아이디]
    public BooleanBuilder getSearchOrd(Long cueId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QSpareCueSheetItem qSpareCueSheetItem = QSpareCueSheetItem.spareCueSheetItem;

        booleanBuilder.and(qSpareCueSheetItem.delYn.eq("N"));

        //쿼리 where 조건 추가.
        if (ObjectUtils.isEmpty(cueId) == false){
            booleanBuilder.and(qSpareCueSheetItem.cueSheet.cueId.eq(cueId));
        }

        return booleanBuilder;
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
