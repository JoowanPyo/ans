package com.gemiso.zodiac.app.cueSheetItem;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleRepository;
import com.gemiso.zodiac.app.article.ArticleService;
import com.gemiso.zodiac.app.article.dto.ArticleUpdateDTO;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleCap.ArticleCapRepository;
import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.articleMedia.ArticleMediaRepository;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetService;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.*;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemCreateMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemUpdateMapper;
import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCap;
import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCapRepotitory;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.mapper.CueSheetItemCapCreateMapper;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbolRepository;
import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolDTO;
import com.gemiso.zodiac.app.cueSheetItemSymbol.mapper.CueSheetItemSymbolMapper;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetItemService {

    //방송아이콘 url저장 주소
    @Value("${files.url-key}")
    private String fileUrl;

    private final ArticleService articleService;

    private final CueSheetItemRepository cueSheetItemRepository;
    private final ArticleRepository articleRepository;
    private final ArticleCapRepository articleCapRepository;
    private final ArticleMediaRepository articleMediaRepository;
    private final CueSheetItemSymbolRepository cueSheetItemSymbolRepository;
    private final CueSheetItemCapRepotitory cueSheetItemCapRepotitory;
    //private final UserGroupUserRepository userGroupUserRepository;
    //private final UserGroupAuthRepository userGroupAuthRepository;
    //private final CueSheetRepository cueSheetRepository;

    private final CueSheetItemMapper cueSheetItemMapper;
    private final CueSheetItemCreateMapper cueSheetItemCreateMapper;
    private final CueSheetItemUpdateMapper cueSheetItemUpdateMapper;
    private final CueSheetItemSymbolMapper cueSheetItemSymbolMapper;
    //private final ArticleMapper articleMapper;
    private final CueSheetItemCapCreateMapper cueSheetItemCapCreateMapper;

    private final CueSheetService cueSheetService;
    private final UserAuthService userAuthService;

    public List<CueSheetItemDTO> findAll(Long artclId, Long cueId, String delYn, String spareYn){

        BooleanBuilder booleanBuilder = getSearch(artclId, cueId, delYn, spareYn);

        List<CueSheetItem> cueSheetItemList = (List<CueSheetItem>) cueSheetItemRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cueItemOrd"));

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);

        cueSheetItemDTOList = setSymbol(cueSheetItemDTOList); //방송아이콘 맵핑 테이블 추가.

        return cueSheetItemDTOList;
    }


    //방송아이콘 url 추가
    public List<CueSheetItemDTO> setSymbol(List<CueSheetItemDTO> cueSheetItemDTOList){

        for (CueSheetItemDTO cueSheetItemDTO : cueSheetItemDTOList){ //조회된 아이템에 List

            Long cueItemId = cueSheetItemDTO.getCueItemId(); //아이템 아이디 get

            //아이템 아이디로 방송아이콘 맵핑테이블 조회
            List<CueSheetItemSymbol> cueSheetItemSymbolList = cueSheetItemSymbolRepository.findSymbol(cueItemId);

            if (ObjectUtils.isEmpty(cueSheetItemSymbolList) == false){ //조회된 방송아콘 맵핑테이블 List가 있으면 url추가후 큐시트 아이템DTO에 추가

                List<CueSheetItemSymbolDTO> cueSheetItemSymbolDTO = cueSheetItemSymbolMapper.toDtoList(cueSheetItemSymbolList);

                for (CueSheetItemSymbolDTO getCueSheetItemSymbol : cueSheetItemSymbolDTO){

                    SymbolDTO symbolDTO = getCueSheetItemSymbol.getSymbol();

                    String fileLoc = symbolDTO.getAttachFile().getFileLoc();//파일로그 get
                    String url = fileUrl + fileLoc; //url + 파일로그

                    symbolDTO.setUrl(url);//방송아이콘이 저장된 url set

                    getCueSheetItemSymbol.setSymbol(symbolDTO);//url 추가 DTO set
                }


                cueSheetItemDTO.setCueSheetItemSymbolDTO(cueSheetItemSymbolDTO); //아이템에 set방송아이콘List

            }
        }

        return cueSheetItemDTOList;

    }

    public CueSheetItemDTO find(Long cueItemId){//큐시트 아이템 상세 조회

        //큐시트 아이템 조회
        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);
        //큐시트 아이템 방송아이콘 List 조회
        List<CueSheetItemSymbol> cueSheetItemSymbol = cueSheetItemSymbolRepository.findSymbol(cueItemId);

        List<CueSheetItemSymbolDTO> cueSheetItemSymbolDTO = cueSheetItemSymbolMapper.toDtoList(cueSheetItemSymbol);

        cueSheetItemSymbolDTO = createUrl(cueSheetItemSymbolDTO);//방송아이콘 url 추가

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);

        cueSheetItemDTO.setCueSheetItemSymbolDTO(cueSheetItemSymbolDTO);

        return cueSheetItemDTO;

    }

    //방송아이콘 url 추가
    public List<CueSheetItemSymbolDTO> createUrl(List<CueSheetItemSymbolDTO> cueSheetItemSymbolDTOList){

        //큐시트 아이템 방송아이콘 리스트를 Url을 추가해서 리턴해줄 DTO 리스트 생성
         List<CueSheetItemSymbolDTO> returnCueSheetItemSymbolDTOList = new ArrayList<>();

        if (CollectionUtils.isEmpty(cueSheetItemSymbolDTOList) == false){

            for (CueSheetItemSymbolDTO cueSheetItemSymbolDTO : cueSheetItemSymbolDTOList){

                SymbolDTO symbolDTO = new SymbolDTO();

                symbolDTO = cueSheetItemSymbolDTO.getSymbol();

                if (ObjectUtils.isEmpty(symbolDTO) == false) {
                    String fileLoc = symbolDTO.getAttachFile().getFileLoc(); //등록되어 있던 파일로그 get
                    String url = fileUrl + fileLoc; //url + 파일로그

                    symbolDTO.setUrl(url);//url주소 set
                    cueSheetItemSymbolDTO.setSymbol(symbolDTO);//url주소 추가된 방송아이콘 큐시트아이템 방송아이콘에 set

                    //새로 url추가한 방송아이콘 리스트를 리턴해줄 리스트 모델에 add
                    returnCueSheetItemSymbolDTOList.add(cueSheetItemSymbolDTO);
                }
            }
        }
        return returnCueSheetItemSymbolDTOList;
    }

    //큐시트 아이템 등록
    public Long create(CueSheetItemCreateDTO cueSheetItemCreateDTO, Long cueId){


        //큐시트 아이디 등록
        CueSheetSimpleDTO cueSheetDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();
        cueSheetItemCreateDTO.setCueSheet(cueSheetDTO);
        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        cueSheetItemCreateDTO.setInputrId(userId);

        CueSheetItem cueSheetItem = cueSheetItemCreateMapper.toEntity(cueSheetItemCreateDTO);
        cueSheetItemRepository.save(cueSheetItem); //큐시트아이템 등록

        Long cueItemId = cueSheetItem.getCueItemId();

        //큐시트 아이템 자막 리스트 get
        List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList = cueSheetItemCreateDTO.getCueSheetItemCap();
        //큐시트 아이템 자막 리스트가 있으면 등록
        if (CollectionUtils.isEmpty(cueSheetItemCapDTOList) == false){
            createCap(cueSheetItemCapDTOList, cueItemId, userId);//큐시트 아이템 자막 리스트 등록
        }

        int cueItemOrd = cueSheetItemCreateDTO.getCueItemOrd();

        String spareYn = cueSheetItem.getSpareYn(); //예비여부값 get

        ordUpdate( cueId,  cueItemId,  cueItemOrd, spareYn);

        return cueItemId;//return 큐시트 아이템 아이디
    }

    public void createTemplate(List<CueSheetItemCreateDTO> cueSheetItemCreateDTOList, Long cueId){

        //큐시트 아이디 등록
        CueSheetSimpleDTO cueSheetDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();

        for (CueSheetItemCreateDTO cueSheetItemCreateDTO : cueSheetItemCreateDTOList) {
            cueSheetItemCreateDTO.setCueSheet(cueSheetDTO);
            //토큰 사용자 Id(현재 로그인된 사용자 ID)
            String userId = userAuthService.authUser.getUserId();
            cueSheetItemCreateDTO.setInputrId(userId);

            CueSheetItem cueSheetItem = cueSheetItemCreateMapper.toEntity(cueSheetItemCreateDTO);
            cueSheetItemRepository.save(cueSheetItem); //큐시트아이템 등록

            Long cueItemId = cueSheetItem.getCueItemId();

            //큐시트 아이템 자막 리스트 get
            List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList = cueSheetItemCreateDTO.getCueSheetItemCap();
            //큐시트 아이템 자막 리스트가 있으면 등록
            if (CollectionUtils.isEmpty(cueSheetItemCapDTOList) == false) {
                createCap(cueSheetItemCapDTOList, cueItemId, userId);//큐시트 아이템 자막 리스트 등록
            }

            //순서변경을 타야하나?
            //ordUpdate( cueId,  cueItemId,  cueItemOrd, "N");
        }


    }

    //큐시트 아이템 자막 등록
    public void createCap(List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList, Long cueItemId, String userId){

        //큐시트 아이템 아이디 빌드
        CueSheetItemSimpleDTO cueSheetItem = CueSheetItemSimpleDTO.builder().cueItemId(cueItemId).build();

        //큐시트 아이템 자막 리스트 등록
        for (CueSheetItemCapCreateDTO capCreateDTO : cueSheetItemCapDTOList){

            capCreateDTO.setCueSheetItem(cueSheetItem); //큐시트 아이템 아이디 set
            capCreateDTO.setInputrId(userId);//등록자 set

            CueSheetItemCap cueSheetItemCap = cueSheetItemCapCreateMapper.toEntity(capCreateDTO);

            cueSheetItemCapRepotitory.save(cueSheetItemCap);//등록
        }
    }

    public void update(CueSheetItemUpdateDTO cueSheetItemUpdateDTO, Long cueId, Long cueItemId){

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        /*CueSheetSimpleDTO cueSheetDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();
        cueSheetItemUpdateDTO.setCueSheet(cueSheetDTO);
        cueSheetItemUpdateDTO.setCueItemId(cueItemId);*/
        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        cueSheetItemUpdateDTO.setUpdtrId(userId);

        cueSheetItemUpdateMapper.updateFromDto(cueSheetItemUpdateDTO, cueSheetItem);

        cueSheetItemRepository.save(cueSheetItem);

        //큐시트 아이템 자막 리스트 get
        List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList = cueSheetItemUpdateDTO.getCueSheetItemCap();
        //큐시트 아이템 자막 리스트가 있으면 등록
        if (CollectionUtils.isEmpty(cueSheetItemCapDTOList) == false){
            updateCap(cueSheetItemCapDTOList, cueItemId, userId);//큐시트 아이템 자막 리스트 등록
        }
    }

    //업데이트 큐시트 아이템 자막
    public void updateCap(List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList, Long cueItemId, String userId){

        //큐시트아이템 자막 리스트 조회 [조회조건 큐시트 아이템 아이디]
        List<CueSheetItemCap> cueSheetItemCapList = cueSheetItemCapRepotitory.findCueSheetItemCapList(cueItemId);

        //조회된 큐시트아이템 자막 리스트 삭제처리.
        for (CueSheetItemCap cueSheetItemCap : cueSheetItemCapList){

            Long cueItemCapId = cueSheetItemCap.getCueItemCapId();

            cueSheetItemCapRepotitory.deleteById(cueItemCapId);
        }

        //큐시트 아이템 자막 재등록
        createCap(cueSheetItemCapDTOList, cueItemId, userId);
    }

    public void updateCueItemArticle(CueSheetItemUpdateDTO cueSheetItemUpdateDTO, Long cueId, Long cueItemId) throws Exception {

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        //CueSheetSimpleDTO cueSheetDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();
        //cueSheetItemUpdateDTO.setCueSheet(cueSheetDTO);
        //cueSheetItemUpdateDTO.setCueItemId(cueItemId);
        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        cueSheetItemUpdateDTO.setUpdtrId(userId);

        cueSheetItemUpdateMapper.updateFromDto(cueSheetItemUpdateDTO, cueSheetItem);

        cueSheetItemRepository.save(cueSheetItem);

        ArticleUpdateDTO articleUpdateDTO = cueSheetItemUpdateDTO.getArticle(); //큐시트 아이템의 수정기사를 불러온다

        if (ObjectUtils.isEmpty(articleUpdateDTO) == false) {//기사내용 수정이 들어왔을경우.
            Long artclId = articleUpdateDTO.getArtclId(); //수정기사의 아이디를 불러온다.

            String newCueItemTypCd = cueSheetItemUpdateDTO.getCueItemTypCd();//새로 들어온 큐시트 아이템 유형 코드

            if (newCueItemTypCd != null && newCueItemTypCd.trim().isEmpty() == false){//새로 들어온 큐시트 아이템 유형 코드가 있을경우
                articleUpdateDTO.setArtclTypDtlCd(newCueItemTypCd);
            }

            articleService.update(articleUpdateDTO, artclId); //기사 수정.
        }
    }

    public void delete(Long cueId, Long cueItemId) throws Exception {

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        cueSheetItemDTO.setDelrId(userId);
        cueSheetItemDTO.setDelYn("Y");
        cueSheetItemDTO.setDelDtm(new Date());

        cueSheetItemMapper.updateFromDto(cueSheetItemDTO, cueSheetItem);

        //큐시트 아이템 삭제시 포함된 기사(복사된 기사)도 삭제
        Article article = cueSheetItem.getArticle();
        if (ObjectUtils.isEmpty(article) == false){
            Long artclId = article.getArtclId();
            Article chkArticle = articleService.articleFindOrFailCueItem(artclId);

            if (ObjectUtils.isEmpty(chkArticle) == false){
                articleService.deleteCueItem(artclId);
            }
            cueSheetItem.setArticle(null);
        }

        cueSheetItemRepository.save(cueSheetItem);


    }

    public void ordCdUpdate(Long cueId, String spareYn, List<CueSheetItemOrdUpdateDTO> cueSheetItemOrdUpdateDTOList){

        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemListSpareYn(cueId, spareYn);

        // 조회된 큐시트아이템 리스트와 업데이트할 리스트 size가 같아야 한다.
        if (cueSheetItemList.size() == cueSheetItemOrdUpdateDTOList.size()) { 

            for (CueSheetItem cueSheetItem : cueSheetItemList) { //조회된 원본 큐시트 아이템

                Long orgCueItemId = cueSheetItem.getCueItemId();//원본 큐시트 아이템 아이디를 가져온다.

                //순번과 순번코드를 새로 등록할 큐시트아이템 DTO리스트
                for (CueSheetItemOrdUpdateDTO cueSheetItemOrdUpdateDTO : cueSheetItemOrdUpdateDTOList) {

                    Long newCueItemId = cueSheetItemOrdUpdateDTO.getCueItemId(); //업데이트된 순서정보 큐시트아이템 아이디를 가져온다.
                    int newOrd = cueSheetItemOrdUpdateDTO.getCueItemOrd(); //업데이트할 순서
                    String newOrdCd = cueSheetItemOrdUpdateDTO.getCueItemOrdCd(); //업데이트할 순서코드

                    //조회된 큐시트 아이디와 업데이트 큐시트 아이디가 같으면 순서,순서코드 업데이트후 수정.
                    if (orgCueItemId.equals(newCueItemId)) {

                        cueSheetItem.setCueItemOrd(newOrd);
                        cueSheetItem.setCueItemOrdCd(newOrdCd);

                        cueSheetItemRepository.save(cueSheetItem);
                        continue;
                    }

                }

            }
        }else{
            throw new ResourceNotFoundException("조회된 큐시트아이템과 업데이트할 큐시트아이템 값이 맞지 않습니다.");
        }

    }

    public CueSheetItem cueItemFindOrFail(Long cueItemId){

        Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findByCueItem(cueItemId);

        if (cueSheetItem.isPresent() == false){
            throw new ResourceNotFoundException("CueSheetItem not found. CueSheet Item Id : " + cueItemId);
        }

        return cueSheetItem.get();

    }

    public BooleanBuilder getSearch(Long artclId, Long cueId, String delYn, String spareYn){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //dsl q쿼리 생성
        QCueSheetItem qCueSheetItem = QCueSheetItem.cueSheetItem;

        if (delYn != null && delYn.trim().isEmpty() == false){
            booleanBuilder.and(qCueSheetItem.delYn.eq(delYn));
        }else {
            booleanBuilder.and(qCueSheetItem.delYn.eq("N"));
        }
        //쿼리 where 조건 추가.
        if (ObjectUtils.isEmpty(artclId) == false){
            booleanBuilder.and(qCueSheetItem.article.artclId.eq(artclId));
        }

        //쿼리 where 조건 추가.
        if (ObjectUtils.isEmpty(cueId) == false){
            booleanBuilder.and(qCueSheetItem.cueSheet.cueId.eq(cueId));
        }

        //스페어 여부
        if (spareYn != null && spareYn.trim().isEmpty() == false){
            booleanBuilder.and(qCueSheetItem.spareYn.eq(spareYn));
        }


        return booleanBuilder;
    }

    public BooleanBuilder getSearchOrd(Long cueId, String spareYn){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //dsl q쿼리 생성
        QCueSheetItem qCueSheetItem = QCueSheetItem.cueSheetItem;

        booleanBuilder.and(qCueSheetItem.delYn.eq("N"));
        //쿼리 where 조건 추가.
        if (ObjectUtils.isEmpty(cueId) == false){
            booleanBuilder.and(qCueSheetItem.cueSheet.cueId.eq(cueId));
        }

        if (spareYn != null && spareYn.trim().isEmpty() == false){
            booleanBuilder.and(qCueSheetItem.spareYn.eq(spareYn));
        }

        return booleanBuilder;
    }

    public void createCueItem(Long cueId, Long artclId, int cueItemOrd, String cueItemDivCd, String spareYn){

        //기사 복사[복사된 기사 Id get]
        Article copyArtcl = copyArticle(artclId, cueId);

        //예비큐시트 값 set
        if (spareYn == null && spareYn.trim().isEmpty() ){
            spareYn = "N"; //예비여부값이 안들어오면 N 값 디폴트
        }

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        String userId = userAuthService.authUser.getUserId();
        //큐시트아이디 큐시트엔티티로 빌드 :: 큐시트아이템에 큐시트 아이디set해주기 위해
        CueSheet cueSheet = CueSheet.builder().cueId(cueId).build();
        //큐시트아이템create 빌드
        CueSheetItem cueSheetItem = CueSheetItem.builder()
                .cueSheet(cueSheet)
                .cueItemOrd(cueItemOrd)
                .cueItemDivCd(cueItemDivCd)
                .inputrId(userId)
                .article(copyArtcl)
                .spareYn(spareYn)//예비큐시트 값
                .build();


        //빌드된 큐시트아이템 등록
        cueSheetItemRepository.save(cueSheetItem);

        Long cueItemId = cueSheetItem.getCueItemId();

        //큐시트아이템 순서 update
        ordUpdateDrop(cueSheetItem, cueId, cueItemOrd, spareYn);


    }

    public void createCueItemList(List<CueSheetItemCreateListDTO> cueSheetItemCreateListDTO, Long cueId, String spareYn){

        //예비큐시트 값 set
        if (spareYn == null && spareYn.trim().isEmpty() ){
            spareYn = "N"; //예비여부값이 안들어오면 N 값 디폴트
        }

        for (CueSheetItemCreateListDTO createListDTO : cueSheetItemCreateListDTO){
            Long artclId = createListDTO.getArtclId();
            int cueItemOrd = createListDTO.getCueItemOrd();
            String cueItemDivCd = createListDTO.getCueItemDivCd();

            //기사 복사[복사된 기사 Id get]
            Article copyArtcl = copyArticle(artclId, cueId);

            //토큰 사용자 Id(현재 로그인된 사용자 ID)
            String userId = userAuthService.authUser.getUserId();
            //큐시트아이디 큐시트엔티티로 빌드 :: 큐시트아이템에 큐시트 아이디set해주기 위해
            CueSheet cueSheet = CueSheet.builder().cueId(cueId).build();

            //큐시트아이템create 빌드
            CueSheetItem cueSheetItem = CueSheetItem.builder()
                    .cueSheet(cueSheet)
                    .cueItemOrd(cueItemOrd)
                    .inputrId(userId)
                    .cueItemDivCd(cueItemDivCd)
                    .article(copyArtcl)
                    .spareYn(spareYn)
                    .build();


            //빌드된 큐시트아이템 등록
            cueSheetItemRepository.save(cueSheetItem);

            Long cueItemId = cueSheetItem.getCueItemId();

            //큐시트아이템 순서 update
            ordUpdateDrop(cueSheetItem, cueId, cueItemOrd, spareYn);
        }

    }
    
    public void ordUpdateDrop(CueSheetItem cueSheetItem, Long cueId, int cueItemOrd, String spareYn){

        // 쿼리문 만들기
        BooleanBuilder booleanBuilder = getSearchOrd(cueId, spareYn);

        //큐시트 아이템 순번 재등록
        List<CueSheetItem> cueSheetItemList = (List<CueSheetItem>) cueSheetItemRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cueItemOrd"));
        if(cueSheetItemList== null)
            return;

        //ClearCueSheetList(cueSheetItemList);

        Long cueSheetItemId = cueSheetItem.getCueItemId();
        for (int i =  cueSheetItemList.size() - 1; i >= 0 ; i--){ //out of index때문에 역순으로 검사.
            if (cueSheetItemId.equals(cueSheetItemList.get(i).getCueItemId()) == false){
                continue;
            }
            cueSheetItemList.remove(i);//신규 등록된 큐시트 아이템 리스트에서 삭제
        }

        cueSheetItemList.add(cueItemOrd, cueSheetItem); //신규등록하려는 큐시트 아이템 원하는 순번에 리스트 추가

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

    public Article copyArticle(Long artclId, Long cueId){

        /*********************/
        /* 기사를 저장하는 부분 */

        //기사 아이디로 기사 검색
        Optional<Article> getArticle = articleRepository.findArticle(artclId);
        //조회된 기사 빈값 검사.

        if (getArticle.isPresent() == false){
            throw new ResourceNotFoundException(String.format("기사아이디에 해당하는 기사가 없습니다. {%ld}", artclId));
        }
        //조회된 기사 get
        Article article = getArticle.get();

        //기사 시퀀스[오리지널 기사 0 = 그밑으로 복사된 기사 + 1 증가]
        int artclOrd = article.getArtclOrd() + 1;

        Article articleEntity = getArticleEntity(article, artclOrd, cueId);
        articleRepository.save(articleEntity);

        /*********************/
        /* 자막을 저장하는 부분 */

        //기사 아이디로 기사자막 조회
        List<ArticleCap> articleCap = articleCapRepository.findArticleCap(artclId);
        for( int i  = 0 ; i < articleCap.size() ; i++ )
        {
            ArticleCap getArticleCap =  articleCap.get(i);

            ArticleCap articleCapEntity = getArticleCapEntity(getArticleCap, articleEntity);

            articleCapRepository.save(articleCapEntity);
        }

        /*********************/
        /* 미디어 정보 저장 하는 부분 */

        //기사 아이디로 기사 미디어 조회
        List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaList(artclId);

        //조회된 기사 미디어가 있으면 복사된 기사 아이디로 기사미디어 신규 저장.
        if (ObjectUtils.isEmpty(articleMediaList) ==false){

            for (ArticleMedia articleMedia : articleMediaList){

                ArticleMedia articleMediaEntity = getArticleMediaEntity(articleMedia, articleEntity);


                articleMediaRepository.save(articleMediaEntity);
            }
        }


        return articleEntity;

    }

    // 기사 저장하기 위한 엔터티 만들기 2021-10-27
    private Article getArticleEntity(Article article, int artclOrd, Long cueId) {

        Long orgArtclId = article.getOrgArtclId();//원본기사 아이디

        if (ObjectUtils.isEmpty(orgArtclId)) { //원본기사가 아이디가없고 최초 복사일시

            return Article.builder()
                    .chDivCd(article.getChDivCd())
                    .artclKindCd(article.getArtclKindCd())
                    .artclFrmCd(article.getArtclFrmCd())
                    .artclDivCd(article.getArtclDivCd())
                    .artclFldCd(article.getArtclFldCd())
                    .apprvDivCd(article.getApprvDivCd())
                    .prdDivCd(article.getPrdDivCd())
                    .artclTypCd(article.getArtclTypCd())
                    .artclTypDtlCd(article.getArtclTypDtlCd())
                    .artclCateCd(article.getArtclCateCd())
                    .artclTitl(article.getArtclTitl())
                    .artclTitlEn(article.getArtclTitlEn())
                    .artclCtt(article.getArtclCtt())
                    .ancMentCtt(article.getAncMentCtt())
                    .rptrNm(article.getRptrNm())
                    .userGrpId(article.getUserGrpId())
                    .artclReqdSecDivYn(article.getArtclReqdSecDivYn())
                    .artclReqdSec(article.getArtclReqdSec())
                    .lckYn(article.getLckYn())
                    .lckDtm(article.getLckDtm())
                    .apprvDtm(article.getApprvDtm())
                    .artclOrd(artclOrd)//기사 시퀀스 +1
                    .brdcCnt(article.getBrdcCnt())
                    .orgArtclId(article.getArtclId())//원본기사 아이디set
                    .urgYn(article.getUrgYn())
                    .frnotiYn(article.getFrnotiYn())
                    .embgYn(article.getEmbgYn())
                    .embgDtm(article.getEmbgDtm())
                    .inputrNm(article.getInputrNm())
                    .delYn(article.getDelYn())
                    .notiYn(article.getNotiYn())
                    .regAppTyp(article.getRegAppTyp())
                    .brdcPgmId(article.getBrdcPgmId())
                    .brdcSchdDtm(article.getBrdcSchdDtm())
                    .inputrId(article.getInputrId())
                    .updtrId(article.getUpdtrId())
                    .delrId(article.getDelrId())
                    .apprvrId(article.getApprvrId())
                    .lckrId(article.getLckrId())
                    .rptrId(article.getRptrId())
                    .artclCttTime(article.getArtclCttTime())
                    .ancMentCttTime(article.getAncMentCttTime())
                    .artclExtTime(article.getArtclExtTime())
                    .videoTime(article.getVideoTime())
                    .deptCd(article.getDeptCd())
                    .deviceCd(article.getDeviceCd())
                    .parentArtlcId(article.getArtclId())//복사한 기사 아이디 set
                    .issue(article.getIssue())
                    .cueId(cueId)//큐시트 아이디 set
                    .build();
        }else { //원본기사 아이디가 있을시[복사된 기사 다시 복사일시]

            return Article.builder()
                    .chDivCd(article.getChDivCd())
                    .artclKindCd(article.getArtclKindCd())
                    .artclFrmCd(article.getArtclFrmCd())
                    .artclDivCd(article.getArtclDivCd())
                    .artclFldCd(article.getArtclFldCd())
                    .apprvDivCd(article.getApprvDivCd())
                    .prdDivCd(article.getPrdDivCd())
                    .artclTypCd(article.getArtclTypCd())
                    .artclTypDtlCd(article.getArtclTypDtlCd())
                    .artclCateCd(article.getArtclCateCd())
                    .artclTitl(article.getArtclTitl())
                    .artclTitlEn(article.getArtclTitlEn())
                    .artclCtt(article.getArtclCtt())
                    .ancMentCtt(article.getAncMentCtt())
                    .rptrNm(article.getRptrNm())
                    .userGrpId(article.getUserGrpId())
                    .artclReqdSecDivYn(article.getArtclReqdSecDivYn())
                    .artclReqdSec(article.getArtclReqdSec())
                    .lckYn(article.getLckYn())
                    .lckDtm(article.getLckDtm())
                    .apprvDtm(article.getApprvDtm())
                    .artclOrd(artclOrd)//기사 시퀀스 +1
                    .brdcCnt(article.getBrdcCnt())
                    .orgArtclId(article.getOrgArtclId())//원본기사 아이디set
                    .urgYn(article.getUrgYn())
                    .frnotiYn(article.getFrnotiYn())
                    .embgYn(article.getEmbgYn())
                    .embgDtm(article.getEmbgDtm())
                    .inputrNm(article.getInputrNm())
                    .delYn(article.getDelYn())
                    .notiYn(article.getNotiYn())
                    .regAppTyp(article.getRegAppTyp())
                    .brdcPgmId(article.getBrdcPgmId())
                    .brdcSchdDtm(article.getBrdcSchdDtm())
                    .inputrId(article.getInputrId())
                    .updtrId(article.getUpdtrId())
                    .delrId(article.getDelrId())
                    .apprvrId(article.getApprvrId())
                    .lckrId(article.getLckrId())
                    .rptrId(article.getRptrId())
                    .artclCttTime(article.getArtclCttTime())
                    .ancMentCttTime(article.getAncMentCttTime())
                    .artclExtTime(article.getArtclExtTime())
                    .videoTime(article.getVideoTime())
                    .deptCd(article.getDeptCd())
                    .deviceCd(article.getDeviceCd())
                    .parentArtlcId(article.getArtclId())//복사한 기사 아이디 set
                    .issue(article.getIssue())
                    .cueId(cueId)//큐시트 아이디 set
                    .build();
        }
    }

    public ArticleCap getArticleCapEntity(ArticleCap getArticleCap, Article articleEntity){
        return ArticleCap.builder()
                .capDivCd(getArticleCap.getCapDivCd())
                .lnNo(getArticleCap.getLnNo())
                .capCtt(getArticleCap.getCapCtt())
                .capRmk(getArticleCap.getCapRmk())
                .article(articleEntity)
                .capTemplate(getArticleCap.getCapTemplate())
                .symbol(getArticleCap.getSymbol())
                .build();
    }

    public ArticleMedia getArticleMediaEntity(ArticleMedia articleMedia, Article articleEntity){

        return ArticleMedia.builder()
                .mediaTypCd(articleMedia.getMediaTypCd())
                .mediaOrd(articleMedia.getMediaOrd())
                .contId(articleMedia.getContId())
                .trnsfFileNm(articleMedia.getTrnsfFileNm())
                .mediaDurtn(articleMedia.getMediaDurtn())
                .mediaMtchDtm(articleMedia.getMediaMtchDtm())
                .trnsfStCd(articleMedia.getTrnsfStCd())
                .assnStCd(articleMedia.getAssnStCd())
                .videoEdtrNm(articleMedia.getVideoEdtrNm())
                .delYn(articleMedia.getDelYn())
                .delDtm(articleMedia.getDelDtm())
                .inputrId(articleMedia.getInputrId())
                .updtrId(articleMedia.getUpdtrId())
                .delrId(articleMedia.getDelrId())
                .videoEdtrId(articleMedia.getVideoEdtrId())
                .article(articleEntity)
                .build();
    }

    //큐시트 방송완료 후 예비큐시트 추가수정.
    public CueSheetSimpleDTO updateSpareCueItem(Long cueId, Long cueItemId, int cueItemOrd, String spareYn){

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);
        String cueStCd = cueSheet.getCueStCd();

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);
        cueSheetItemDTO.setSpareYn(spareYn);

        cueSheetItemMapper.updateFromDto(cueSheetItemDTO, cueSheetItem);

        cueSheetItemRepository.save(cueSheetItem);

        ordUpdate(cueId, cueItemId, cueItemOrd, spareYn);

        CueSheetSimpleDTO cueSheetSimpleDTO = new CueSheetSimpleDTO();
        cueSheetSimpleDTO.setCueId(cueId);

        return cueSheetSimpleDTO;
    }

    public void ordUpdate( Long cueId, Long cueItemId, int cueItemOrd, String spareYn){


        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheetItemDTO updateCueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);
        updateCueSheetItemDTO.setCueItemOrd(cueItemOrd);

        cueSheetItemMapper.updateFromDto(updateCueSheetItemDTO, cueSheetItem);
        cueSheetItemRepository.save(cueSheetItem); //큐시트아이템 등록

        //큐시트 아이템 순번 재등록
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemListSpareYn(cueId, spareYn);

        for (int i =  cueSheetItemList.size() - 1; i >= 0 ; i--){
            if (!cueItemId.equals(cueSheetItemList.get(i).getCueItemId())){
                continue;
            }
            cueSheetItemList.remove(i);//신규 등록된 큐시트 아이템 리스트에서 삭제
        }

        cueSheetItemList.add(cueItemOrd, cueSheetItem); //신규등록하려는 큐시트 아이템 원하는 순번에 리스트 추가

        //조회된 큐시트 아이템 Ord 업데이트
        int index = 0;
        for (CueSheetItem cueSheetItems : cueSheetItemList){

            CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItems);
            cueSheetItemDTO.setCueItemOrd(index);//순번 재등록
            CueSheetItem setCueSheetItem = cueSheetItemMapper.toEntity(cueSheetItemDTO);
            cueSheetItemRepository.save(setCueSheetItem);//순번 업데이트
            index++;//순번 + 1
        }

    }



}
