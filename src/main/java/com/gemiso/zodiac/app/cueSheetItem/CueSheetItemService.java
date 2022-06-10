package com.gemiso.zodiac.app.cueSheetItem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.anchorCap.AnchorCap;
import com.gemiso.zodiac.app.anchorCap.AnchorCapRepository;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleRepository;
import com.gemiso.zodiac.app.article.ArticleService;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.article.dto.ArticleUpdateDTO;
import com.gemiso.zodiac.app.article.mapper.ArticleMapper;
import com.gemiso.zodiac.app.articleActionLog.ArticleActionLogRepository;
import com.gemiso.zodiac.app.articleActionLog.ArticleActionLogService;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleCap.ArticleCapRepository;
import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.articleMedia.ArticleMediaRepository;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaMapper;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetRepository;
import com.gemiso.zodiac.app.cueSheet.CueSheetService;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetMapper;
import com.gemiso.zodiac.app.cueSheetItem.dto.*;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemCreateMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemUpdateMapper;
import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCap;
import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCapRepotitory;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.mapper.CueSheetItemCapCreateMapper;
import com.gemiso.zodiac.app.cueSheetItemCap.mapper.CueSheetItemCapMapper;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbolRepository;
import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolDTO;
import com.gemiso.zodiac.app.cueSheetItemSymbol.mapper.CueSheetItemSymbolCreateMapper;
import com.gemiso.zodiac.app.cueSheetItemSymbol.mapper.CueSheetItemSymbolMapper;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMedia;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMediaRepository;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaCreateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaCreateMapper;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaMapper;
import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplateService;
import com.gemiso.zodiac.app.cueSheetTemplateItem.CueTmpltItem;
import com.gemiso.zodiac.app.cueSheetTemplateItem.CueTmpltItemService;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.CueTmpltItemCap;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.CueTmpltMedia;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.CueTmplSymbol;
import com.gemiso.zodiac.app.facilityManage.FacilityManage;
import com.gemiso.zodiac.app.facilityManage.FacilityManageService;
import com.gemiso.zodiac.app.lbox.LboxService;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.topic.CueSheetTopicService;
import com.gemiso.zodiac.core.topic.TopicSendService;
import com.gemiso.zodiac.core.topic.interfaceTopicDTO.TakerCueSheetTopicDTO;
import com.gemiso.zodiac.core.topic.cueSheetTopicDTO.CueSheetWebTopicDTO;
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

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetItemService {

    //방송아이콘 url저장 주소
    @Value("${files.url-key}")
    private String fileUrl;

    private final CueSheetRepository cueSheetRepository;
    private final CueSheetItemRepository cueSheetItemRepository;
    private final CueSheetItemCapRepotitory cueSheetItemCapRepotitory;
    private final ArticleRepository articleRepository;
    private final ArticleCapRepository articleCapRepository;
    private final AnchorCapRepository anchorCapRepository;
    private final ArticleMediaRepository articleMediaRepository;
    private final CueSheetItemSymbolRepository cueSheetItemSymbolRepository;
    private final CueSheetMediaRepository cueSheetMediaRepository;
    private final ArticleActionLogRepository articleActionLogRepository;
    //private final CueTmpltItemRepository cueTmpltItemRepository;

    private final CueSheetMapper cueSheetMapper;
    private final CueSheetItemMapper cueSheetItemMapper;
    private final CueSheetItemCreateMapper cueSheetItemCreateMapper;
    private final CueSheetItemUpdateMapper cueSheetItemUpdateMapper;
    private final CueSheetItemSymbolMapper cueSheetItemSymbolMapper;
    private final CueSheetItemSymbolCreateMapper cueSheetItemSymbolCreateMapper;
    private final CueSheetMediaCreateMapper cueSheetMediaCreateMapper;
    //private final ArticleMapper articleMapper;
    private final CueSheetItemCapCreateMapper cueSheetItemCapCreateMapper;
    private final ArticleMapper articleMapper;
    private final CueSheetMediaMapper cueSheetMediaMapper;
    private final CueSheetItemCapMapper cueSheetItemCapMapper;
    private final ArticleMediaMapper articleMediaMapper;

    private final CueSheetService cueSheetService;
    private final CueTmpltItemService cueTmpltItemService;
    //private final TopicSendService topicSendService;
    private final CueSheetTopicService cueSheetTopicService;
    private final LboxService lboxService;
    private final CueSheetTemplateService cueSheetTemplateService;
    private final ArticleActionLogService articleActionLogService;
    private final ArticleService articleService;

    //private final UserAuthService userAuthService;

    private final MarshallingJsonHelper marshallingJsonHelper;
    private final FacilityManageService facilityManageService;


    public List<CueSheetItemDTO> findAll(Long artclId, Long cueId, String delYn, String spareYn) {

        //BooleanBuilder booleanBuilder = getSearch(artclId, cueId, delYn, spareYn);

        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueSheetItemList(artclId, cueId, delYn, spareYn);

        List<CueSheetItemDTO> cueSheetItemDTOList = cueSheetItemMapper.toDtoList(cueSheetItemList);

        cueSheetItemDTOList = setSymbol(cueSheetItemDTOList); //방송아이콘 url 추가.

        return cueSheetItemDTOList;
    }

    //큐시트 아이템 상세 조회
    public CueSheetItemDTO find(Long cueItemId) {

        //큐시트 아이템 조회
        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        //큐시트 아이템 방송아이콘 List 조회
        Set<CueSheetItemSymbol> cueSheetItemSymbol = cueSheetItem.getCueSheetItemSymbol();

        List<CueSheetItemSymbol> cueSheetItemSymbols = new ArrayList<>(cueSheetItemSymbol);

        List<CueSheetItemSymbolDTO> cueSheetItemSymbolDTO = cueSheetItemSymbolMapper.toDtoList(cueSheetItemSymbols);

        cueSheetItemSymbolDTO = createUrl(cueSheetItemSymbolDTO);//방송아이콘 url 추가

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);

        cueSheetItemDTO.setCueSheetItemSymbol(cueSheetItemSymbolDTO);

        return cueSheetItemDTO;

    }

    //큐시트 아이템 등록
    public Long create(CueSheetItemCreateDTO cueSheetItemCreateDTO, Long cueId, String userId) throws JsonProcessingException {

        //큐시트 아이디로 큐시트 조회 및 존재유무 확인.
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);

        //큐시트 아이디 등록
        CueSheetSimpleDTO cueSheetDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();
        cueSheetItemCreateDTO.setCueSheet(cueSheetDTO);
        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        cueSheetItemCreateDTO.setInputrId(userId);

        CueSheetItem cueSheetItem = cueSheetItemCreateMapper.toEntity(cueSheetItemCreateDTO);
        cueSheetItemRepository.save(cueSheetItem); //큐시트아이템 등록

        Long cueItemId = cueSheetItem.getCueItemId();

        Integer cueItemOrd = cueSheetItem.getCueItemOrd();//순번
        String spareYn = cueSheetItem.getSpareYn();//스페어 여부

        ordUpdateDrop(cueSheetItem, cueId, cueItemOrd, spareYn); //큐시트 순번 정렬
        //신규 등록후 순서정렬
        //ordUpdate(cueId, cueItemId, cueItemOrd, spareYn);

        //큐시트 아이템 자막 리스트 get
        List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList = cueSheetItemCreateDTO.getCueSheetItemCap();
        //큐시트 아이템 자막 리스트가 있으면 등록
        if (CollectionUtils.isEmpty(cueSheetItemCapDTOList) == false) {
            createCap(cueSheetItemCapDTOList, cueItemId, userId);//큐시트 아이템 자막 리스트 등록
        }


        /************ ORDERVER UP *************/
        cueSheet = addCueVer(cueSheet); // 큐시트 버전 정보 업데이트

        //String cueItemDivCd = cueSheetItem.getCueItemDivCd();
        /************ MQ messages *************/
        //기사, 템플릿 이 포함되어 있는 경우 아이디를 TOPIC전송
        Article article = cueSheetItem.getArticle();
        CueSheetTemplate cueSheetTemplate = cueSheetItem.getCueSheetTemplate();
        Long artclId = null;
        Long cueTmpltId = null;
        if (ObjectUtils.isEmpty(article) == false) {
            artclId = article.getArtclId();
        }
        if (ObjectUtils.isEmpty(cueSheetTemplate) == false) {
            cueItemId = cueSheetTemplate.getCueTmpltId();
        }

        cueSheetTopicService.sendCueTopicCreate(cueSheet, cueId, cueItemId, artclId, cueTmpltId, "Create CueSheetItem",
                spareYn, "Y", "Y");

        return cueItemId;//return 큐시트 아이템 아이디
    }

    //큐시트 아이템 수정 [기사 x]
    public void update(CueSheetItemUpdateDTO cueSheetItemUpdateDTO, Long cueId, Long cueItemId, String userId) throws JsonProcessingException {

        //큐시트 아이디로 큐시트 조회 및 존재유무 확인.
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        /*CueSheetSimpleDTO cueSheetDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();
        cueSheetItemUpdateDTO.setCueSheet(cueSheetDTO);
        cueSheetItemUpdateDTO.setCueItemId(cueItemId);*/
        // 토큰 인증된 사용자 아이디를 입력자로 등록
        cueSheetItemUpdateDTO.setUpdtrId(userId);

        cueSheetItemUpdateMapper.updateFromDto(cueSheetItemUpdateDTO, cueSheetItem);

        cueSheetItemRepository.save(cueSheetItem);

        //큐시트 아이템 자막 리스트 get
        List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList = cueSheetItemUpdateDTO.getCueSheetItemCap();
        //큐시트 아이템 자막 리스트가 있으면 등록
        if (CollectionUtils.isEmpty(cueSheetItemCapDTOList) == false) {
            updateCap(cueSheetItemCapDTOList, cueItemId, userId);//큐시트 아이템 자막 리스트 등록
        }

        /************ MQ messages *************/
        //기사, 템플릿 이 포함되어 있는 경우 아이디를 TOPIC전송
        Article article = cueSheetItem.getArticle();
        CueSheetTemplate cueSheetTemplate = cueSheetItem.getCueSheetTemplate();
        Long artclId = null;
        Long cueTmpltId = null;
        if (ObjectUtils.isEmpty(article) == false) {
            artclId = article.getArtclId();
        }
        if (ObjectUtils.isEmpty(cueSheetTemplate) == false) {
            cueItemId = cueSheetTemplate.getCueTmpltId();
        }

        String spareYn = cueSheetItem.getSpareYn();

        cueSheet = addCueVer(cueSheet);

        cueSheetTopicService.sendCueTopicCreate(cueSheet, cueId, cueItemId, artclId, cueTmpltId, "Update CueSheetItem-NonArticle",
                spareYn, "Y", "N");
    }

    //큐시트 아이템 기사 수정시
    public void updateCueItemArticle(CueSheetItemUpdateDTO cueSheetItemUpdateDTO, Long cueId, Long cueItemId, String userId) throws Exception {

        //큐시트 아이디로 큐시트 조회 및 존재유무 확인.
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        //CueSheetSimpleDTO cueSheetDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();
        //cueSheetItemUpdateDTO.setCueSheet(cueSheetDTO);
        //cueSheetItemUpdateDTO.setCueItemId(cueItemId);
        // 토큰 인증된 사용자 아이디를 입력자로 등록
        cueSheetItemUpdateDTO.setUpdtrId(userId);

        cueSheetItemUpdateMapper.updateFromDto(cueSheetItemUpdateDTO, cueSheetItem);

        cueSheetItemRepository.save(cueSheetItem);

        ArticleUpdateDTO articleUpdateDTO = cueSheetItemUpdateDTO.getArticle(); //큐시트 아이템의 수정기사를 불러온다

        if (ObjectUtils.isEmpty(articleUpdateDTO) == false) {//기사내용 수정이 들어왔을경우.
            Long artclId = articleUpdateDTO.getArtclId(); //수정기사의 아이디를 불러온다.

            String newCueItemTypCd = cueSheetItemUpdateDTO.getCueItemTypCd();//새로 들어온 큐시트 아이템 유형 코드

            if (newCueItemTypCd != null && newCueItemTypCd.trim().isEmpty() == false) {//새로 들어온 큐시트 아이템 유형 코드가 있을경우
                articleUpdateDTO.setArtclTypDtlCd(newCueItemTypCd);
            }

            articleService.update(articleUpdateDTO, artclId, userId); //기사 수정.
        }

        /************ MQ messages *************/
        //기사, 템플릿 이 포함되어 있는 경우 아이디를 TOPIC전송
        Article article = cueSheetItem.getArticle();
        CueSheetTemplate cueSheetTemplate = cueSheetItem.getCueSheetTemplate();
        Long artclId = null;
        Long cueTmpltId = null;
        if (ObjectUtils.isEmpty(article) == false) {
            artclId = article.getArtclId();
        }
        if (ObjectUtils.isEmpty(cueSheetTemplate) == false) {
            cueItemId = cueSheetTemplate.getCueTmpltId();
        }

        String spareYn = cueSheetItem.getSpareYn();

        cueSheet = addCueVer(cueSheet);

        cueSheetTopicService.sendCueTopicCreate(cueSheet, cueId, cueItemId, artclId, cueTmpltId, "Update CueSheetItem-Article",
                spareYn, "Y", "N");
    }

    //큐시트 아이템 삭제
    public void delete(Long cueId, Long cueItemId, String userId) throws Exception {

        //큐시트 아이디로 큐시트 조회 및 존재유무 확인.
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);

        cueSheetItemDTO.setDelrId(userId);
        cueSheetItemDTO.setDelYn("Y");
        cueSheetItemDTO.setDelDtm(new Date());

        cueSheetItemMapper.updateFromDto(cueSheetItemDTO, cueSheetItem);
        cueSheetItemRepository.save(cueSheetItem);

        //큐시트 아이템 삭제시 포함된 기사(복사된 기사)도 삭제
        Article article = cueSheetItem.getArticle();

        if (ObjectUtils.isEmpty(article) == false) {
            Long artclId = article.getArtclId();

            //articleService.delete(artclId, userId);

            //Article chkArticle = articleService.articleFindOrFailCueItem(artclId);

            if (ObjectUtils.isEmpty(article) == false) {

                article.setDelrId(userId);
                article.setDelYn("Y");
                article.setDelDtm(new Date());

                articleRepository.save(article);

                //기사가 삭제될때 포함된 미디어정보도 같이 삭제처리 ( delYn = "Y")
                Set<ArticleMedia> articleMedia = article.getArticleMedia();
                deleteArticleMedia(articleMedia, userId);
            }
        }


        //삭제되 아이템이 포함된 미디어정보 삭제 플레그 처리 ( delYn = "Y")
        Set<CueSheetMedia> cueSheetMedia = cueSheetItem.getCueSheetMedia();
        deleteMedia(cueSheetMedia, userId);
        //삭제되 아이템이 포함된 자막 삭제 플레그 처리 ( delYn = "Y")
        Set<CueSheetItemCap> cueSheetItemCap = cueSheetItem.getCueSheetItemCap();
        deleteItemCap(cueSheetItemCap, userId);
        //삭제되 아이템이 포함된 방송아이콘 삭제
        /*Set<CueSheetItemSymbol> cueSheetItemSymbol = cueSheetItem.getCueSheetItemSymbol();
        deleteItemSymbol(cueSheetItemSymbol);*/

        String spareYn = cueSheetItem.getSpareYn();//스페어 여부값

        //삭제시 삭제된 데이터 배제하고 순서변경
        deleteOrdUpdate(cueId, cueItemId, spareYn);

        /************ MQ messages *************/
        //기사, 템플릿 이 포함되어 있는 경우 아이디를 TOPIC전송
        Article articleEntity = cueSheetItem.getArticle();
        CueSheetTemplate cueSheetTemplate = cueSheetItem.getCueSheetTemplate();
        Long artclId = null;
        Long cueTmpltId = null;
        if (ObjectUtils.isEmpty(articleEntity) == false) {
            artclId = articleEntity.getArtclId();
        }
        if (ObjectUtils.isEmpty(cueSheetTemplate) == false) {
            cueItemId = cueSheetTemplate.getCueTmpltId();
        }

        //String spareYn = cueSheetItem.getSpareYn();

        cueSheet = addCueVer(cueSheet);

        cueSheetTopicService.sendCueTopicCreate(cueSheet, cueId, cueItemId, artclId, cueTmpltId, "Update CueSheetItem-Article",
                spareYn, "Y", "Y");
    }

    //삭제되 아이템이 포함된 방송아이콘 삭제
    public void deleteItemSymbol(Set<CueSheetItemSymbol> cueSheetItemSymbolSet){

        for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolSet){

            Long id = cueSheetItemSymbol.getId();

            cueSheetItemSymbolRepository.deleteById(id);
        }
    }

    //기사가 삭제될때 포함된 미디어정보도 같이 삭제처리 ( delYn = "Y")
    public void deleteArticleMedia(Set<ArticleMedia> articleMediaSet, String userId){

        for (ArticleMedia articleMedia : articleMediaSet){

            ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);
            articleMediaDTO.setDelYn("Y");
            articleMediaDTO.setDelrId(userId);
            articleMediaDTO.setDelDtm(new Date());

            articleMediaMapper.updateFromDto(articleMediaDTO, articleMedia);

            articleMediaRepository.save(articleMedia);

        }
    }

    //삭제되 아이템이 포함된 자막 삭제 플레그 처리 ( delYn = "Y")
    public void deleteItemCap(Set<CueSheetItemCap> cueSheetItemCapSet, String userId){

        for (CueSheetItemCap cueSheetItemCap : cueSheetItemCapSet){

            CueSheetItemCapDTO cueSheetItemCapDTO = cueSheetItemCapMapper.toDto(cueSheetItemCap);
            cueSheetItemCapDTO.setDelYn("Y");
            cueSheetItemCapDTO.setDelrId(userId);
            cueSheetItemCapDTO.setDelDtm(new Date());

            cueSheetItemCapMapper.updateFromDto(cueSheetItemCapDTO, cueSheetItemCap);

            cueSheetItemCapRepotitory.save(cueSheetItemCap);
        }

    }

    //삭제되 아이템이 포함된 미디어정보 삭제 플레그 처리 ( delYn = "Y")
    public void deleteMedia(Set<CueSheetMedia> cueSheetMediaSet, String userId){

        for (CueSheetMedia cueSheetMedia :  cueSheetMediaSet){

            CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaMapper.toDto(cueSheetMedia);
            cueSheetMediaDTO.setDelYn("Y");
            cueSheetMediaDTO.setDelrId(userId);
            cueSheetMediaDTO.setDelDtm(new Date());

            cueSheetMediaMapper.updateFromDto(cueSheetMediaDTO, cueSheetMedia);

            cueSheetMediaRepository.save(cueSheetMedia);

        }

    }

    //큐시트 아이템 생성[Drag and Drop CueSheetItem]
    public void createCueItem(Long cueId, Long cueItemId, Integer cueItemOrd, String cueItemDivCd, String spareYn, String userId) throws Exception {

        //큐시트 아이디로 큐시트 조회 및 존재유무 확인.
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);

        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        //예비큐시트 값 set
        if (spareYn == null && spareYn.trim().isEmpty()) {
            spareYn = "N"; //예비여부값이 안들어오면 N 값 디폴트
        }

        Article article = null;
        Article copyArticle = cueSheetItem.getArticle();
        if (ObjectUtils.isEmpty(copyArticle) == false) {
            Long artclId = copyArticle.getArtclId();
            //기사 복사[복사된 기사 Id get]
            article = copyArticle(artclId, cueId, userId);
        }

        CueSheetTemplate cueSheetTemplate = cueSheetItem.getCueSheetTemplate();


        CueSheetItem cueSheetItemEntity = copyItem(cueId, cueSheetItem, article, cueSheetTemplate, userId, spareYn);

        Long copyCueItemId = cueSheetItemEntity.getCueItemId();

        //큐시트 아이템 자막 등록
        Set<CueSheetItemCap> cueSheetItemCaps = cueSheetItem.getCueSheetItemCap();
        createItemCap(cueSheetItemCaps, copyCueItemId, userId);
        //큐시트 미디어 등록
        Set<CueSheetMedia> cueSheetMedias = cueSheetItem.getCueSheetMedia();
        createMedia(cueSheetMedias, copyCueItemId, userId, cueSheet);
        //큐시트 아이템 심볼 등록
        Set<CueSheetItemSymbol> cueSheetItemSymbols = cueSheetItem.getCueSheetItemSymbol();
        createItemSymbol(cueSheetItemSymbols, copyCueItemId);


        //신규 등록후 순서정렬
        ordUpdateDrop(cueSheetItemEntity, cueId, cueItemOrd, spareYn); //큐시트 순번 정렬


    }

    //큐시트 아이템 생성[Drag and Drop] By CueTemplateItem
    public void createCueTemplate(Long cueId, List<CueSheetItemCreateTmplDTO> cueSheetItemCreateTmplDTO, String cueItemDivCd, String spareYn, String userId) throws JsonProcessingException {

        //큐시트 아이디로 큐시트 조회 및 존재유무 확인.
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);

        for (CueSheetItemCreateTmplDTO DTO : cueSheetItemCreateTmplDTO) {

            Long cueTmpltItemId = DTO.getCueTmpltItemId();
            Integer cueItemOrd = DTO.getCueItemOrd();

            CueTmpltItem cueTmpltItem = cueTmpltItemService.findCueTmplItem(cueTmpltItemId);

            //큐시트 템플릿 아이템을 큐시트 아이템으로 복사
            CueSheetItem cueSheetItemEntity = copyTmpltItem(cueTmpltItem, cueId, userId, spareYn);

            Long copyCueItemId = cueSheetItemEntity.getCueItemId();

            //큐시트 아이템 자막 등록
            List<CueTmpltItemCap> cueTmpltItemCap = cueTmpltItem.getCueTmpltItemCap();
            copyTemplateCap(cueTmpltItemCap, copyCueItemId, userId);
            //큐시트 아이템 심볼 등록
            List<CueTmplSymbol> cueTmplSymbol = cueTmpltItem.getCueTmplSymbol();
            copyTemplateSymbol(cueTmplSymbol, copyCueItemId);
            //큐시트 미디어 등록
            List<CueTmpltMedia> cueTmpltMedia = cueTmpltItem.getCueTmpltMedia();
            copyTemplateMedia(cueTmpltMedia, copyCueItemId, userId, cueSheet);

            //신규 등록후 순서정렬
            ordUpdateDrop(cueSheetItemEntity, cueId, cueItemOrd, spareYn); //큐시트 순번 정렬
        }

        //신규 등록후 순서정렬
        //ordUpdate(cueId, copyCueItemId, cueItemOrd, spareYn);
    }

    //드레그앤드랍 큐시트 템플릿 아이템
    public CueSheetItem copyTmpltItem(CueTmpltItem cueTmpltItem, Long cueId, String userId, String spareYn){

        CueSheet cueSheet = CueSheet.builder().cueId(cueId).build();

        //큐시트 템플릿이 들어가 있을 경우 검증후 추가
        CueSheetTemplate cueSheetTemplate = cueTmpltItem.getCueSheetTemplate();
        CueSheetTemplate cueSheetTemplateEntity = new CueSheetTemplate();
        if (ObjectUtils.isEmpty(cueSheetTemplate) == false){
            Long cueTmpltId = cueSheetTemplate.getCueTmpltId();
            cueSheetTemplateEntity = cueSheetTemplateService.cueSheetTemplateFindOrFail(cueTmpltId);
        }

        CueSheetItem cueSheetItemEntity = null;

        if ("Y".equals(spareYn)) {
            cueSheetItemEntity = CueSheetItem.builder()
                    .cueItemTitl(cueTmpltItem.getCueItemTitl())
                    .cueItemTitlEn(cueTmpltItem.getCueItemTitlEn())
                    .cueItemCtt(cueTmpltItem.getCueItemCtt())
                    .cueItemOrd(cueTmpltItem.getCueItemOrd())
                    .cueItemOrdCd(cueTmpltItem.getCueItemOrdCd())
                    .cueItemTime(cueTmpltItem.getCueItemTime())
                    .cueItemFrmCd(cueTmpltItem.getCueItemFrmCd())
                    .cueItemDivCd(cueTmpltItem.getCueItemDivCd())
                    //.brdcStCd(cueTmpltItem.)
                    .lckYn(cueTmpltItem.getLckYn())
                    .delYn(cueTmpltItem.getDelYn())
                    .delDtm(cueTmpltItem.getDelDtm())
                    .lckDtm(cueTmpltItem.getLckDtm())
                    .mediaChCd(cueTmpltItem.getMediaChCd())
                    .mediaDurtn(cueTmpltItem.getMediaDurtn())
                    .inputrId(userId)
                    .spareYn(spareYn)
                    .cueSheet(cueSheet)
                    .cueSheetTemplate(cueSheetTemplateEntity)
                    .rmk(userId)
                    .build();
        }else {

            cueSheetItemEntity = CueSheetItem.builder()
                    .cueItemTitl(cueTmpltItem.getCueItemTitl())
                    .cueItemTitlEn(cueTmpltItem.getCueItemTitlEn())
                    .cueItemCtt(cueTmpltItem.getCueItemCtt())
                    .cueItemOrd(cueTmpltItem.getCueItemOrd())
                    .cueItemOrdCd(cueTmpltItem.getCueItemOrdCd())
                    .cueItemTime(cueTmpltItem.getCueItemTime())
                    .cueItemFrmCd(cueTmpltItem.getCueItemFrmCd())
                    .cueItemDivCd(cueTmpltItem.getCueItemDivCd())
                    //.brdcStCd(cueTmpltItem.)
                    .lckYn(cueTmpltItem.getLckYn())
                    .delYn(cueTmpltItem.getDelYn())
                    .delDtm(cueTmpltItem.getDelDtm())
                    .lckDtm(cueTmpltItem.getLckDtm())
                    .mediaChCd(cueTmpltItem.getMediaChCd())
                    .mediaDurtn(cueTmpltItem.getMediaDurtn())
                    .inputrId(userId)
                    .spareYn(spareYn)
                    .cueSheet(cueSheet)
                    .cueSheetTemplate(cueSheetTemplateEntity)
                    .build();

        }

        cueSheetItemRepository.save(cueSheetItemEntity);

        return cueSheetItemEntity;


    }

    //드레그앤드랍 큐시트 템플릿 미디어 복시
    public void copyTemplateMedia(List<CueTmpltMedia> cueTmpltMedia, Long copyCueItemId, String userId, CueSheet cueSheet) throws JsonProcessingException {

        CueSheetItem cueSheetItem = CueSheetItem.builder().cueItemId(copyCueItemId).build();

        //부조 정보를 가져온다 = "A, B"
        Long subrmId = cueSheet.getSubrmId();
        FacilityManage facilityManage = facilityManageService.findFacility(subrmId);
        String subrmNm = facilityManage.getFcltyDivCd();

        for (CueTmpltMedia cueSheetMedia : cueTmpltMedia) {

            CueSheetMedia cueSheetMediaEntity = CueSheetMedia.builder()
                    .mediaTypCd(cueSheetMedia.getMediaTypCd())
                    .mediaOrd(cueSheetMedia.getMediaOrd())
                    .contId(cueSheetMedia.getContId())
                    .trnsfFileNm(cueSheetMedia.getTrnsfFileNm())
                    .mediaDurtn(cueSheetMedia.getMediaDurtn())
                    .mediaMtchDtm(cueSheetMedia.getMediaMtchDtm())
                    .trnsfStCd(cueSheetMedia.getTrnsfStCd())
                    .assnStCd(cueSheetMedia.getAssnStCd())
                    .videoEdtrNm(cueSheetMedia.getVideoEdtrNm())
                    .videoEdtrId(cueSheetMedia.getVideoEdtrId())
                    .inputrId(userId)
                    .cueMediaTitl(cueSheetMedia.getCueTmpltMediaTitl())
                    .videoId(cueSheetMedia.getVideoId())
                    .trnasfVal(cueSheetMedia.getTrnasfVal())
                    .cueSheetItem(cueSheetItem)
                    .build();

            cueSheetMediaRepository.save(cueSheetMediaEntity);

            Long mediaId = cueSheetMediaEntity.getCueMediaId();
            Integer contentId = cueSheetMediaEntity.getContId();

            //추후에 T는 클라우드 콘피그 교체
            lboxService.cueMediaTransfer(mediaId, contentId, subrmNm, false, false, "T");
        }
    }

    //드레그앤드랍 큐시트 아이템 미디어 등록
    public void createMedia(Set<CueSheetMedia> cueSheetMedias, Long copyCueItemId, String userId, CueSheet cueSheet) throws JsonProcessingException {

        CueSheetItem cueSheetItem = CueSheetItem.builder().cueItemId(copyCueItemId).build();

        //부조 정보를 가져온다 = "A, B"
        Long subrmId = cueSheet.getSubrmId();
        FacilityManage facilityManage = facilityManageService.findFacility(subrmId);
        String subrmNm = facilityManage.getFcltyDivCd();

        for (CueSheetMedia cueSheetMedia : cueSheetMedias){

            CueSheetMedia cueSheetMediaEntity = CueSheetMedia.builder()
                    .mediaTypCd(cueSheetMedia.getMediaTypCd())
                    .mediaOrd(cueSheetMedia.getMediaOrd())
                    .contId(cueSheetMedia.getContId())
                    .trnsfFileNm(cueSheetMedia.getTrnsfFileNm())
                    .mediaDurtn(cueSheetMedia.getMediaDurtn())
                    .mediaMtchDtm(cueSheetMedia.getMediaMtchDtm())
                    .trnsfStCd(cueSheetMedia.getTrnsfStCd())
                    .assnStCd(cueSheetMedia.getAssnStCd())
                    .videoEdtrNm(cueSheetMedia.getVideoEdtrNm())
                    .videoEdtrId(cueSheetMedia.getVideoEdtrId())
                    .inputrId(userId)
                    .cueMediaTitl(cueSheetMedia.getCueMediaTitl())
                    .videoId(cueSheetMedia.getVideoId())
                    .trnasfVal(cueSheetMedia.getTrnasfVal())
                    .cueSheetItem(cueSheetItem)
                    .build();

            cueSheetMediaRepository.save(cueSheetMediaEntity);

            Long mediaId = cueSheetMediaEntity.getCueMediaId();
            Integer contentId = cueSheetMediaEntity.getContId();

            //추후에 T는 클라우드 콘피그 교체
            lboxService.cueMediaTransfer(mediaId, contentId, subrmNm,  false, false, "T");

        }
    }

    //드레그앤드랍 큐시트 템플릿 자막 복사
    public void copyTemplateCap(List<CueTmpltItemCap> cueSheetItemCaps, Long copyCueItemId, String userId){

        CueSheetItem cueSheetItem = CueSheetItem.builder().cueItemId(copyCueItemId).build();

        for (CueTmpltItemCap cueSheetItemCap : cueSheetItemCaps) {

          /*  CapTemplate capTemplate = new CapTemplate();
            CapTemplate copyCapTemplate = cueSheetItemCap.getCapTemplate();*/


            CueSheetItemCap cueSheetItemCapEntity = CueSheetItemCap.builder()
                    .cueItemCapDivCd(cueSheetItemCap.getCueItemCapDivCd())
                    .capCtt(cueSheetItemCap.getCapCtt())
                    .capOrd(cueSheetItemCap.getCapOrd())
                    .lnNo(cueSheetItemCap.getLnNo())
                    .capPrvwId(cueSheetItemCap.getCapPrvwId())
                    .capClassCd(cueSheetItemCap.getCapClassCd())
                    .capPrvwUrl(cueSheetItemCap.getCapPrvwUrl())
                    .colorInfo(cueSheetItemCap.getColorInfo())
                    .capRmk(cueSheetItemCap.getCapRmk())
                    //.orgCueItemCapId(cueSheetItemCap.getOrgCueItemCapId())
                    .inputrId(userId)
                    .capTemplate(cueSheetItemCap.getCapTemplate())
                    .cueSheetItem(cueSheetItem)
                    .symbol(cueSheetItemCap.getSymbol())
                    .build();

            cueSheetItemCapRepotitory.save(cueSheetItemCapEntity);
        }

    }

    //드레그앤드랍 큐시트 아이템 자막등록
    public void createItemCap(Set<CueSheetItemCap> cueSheetItemCaps, Long copyCueItemId, String userId){

        CueSheetItem cueSheetItem = CueSheetItem.builder().cueItemId(copyCueItemId).build();

        for (CueSheetItemCap cueSheetItemCap : cueSheetItemCaps){

          /*  CapTemplate capTemplate = new CapTemplate();
            CapTemplate copyCapTemplate = cueSheetItemCap.getCapTemplate();*/


            CueSheetItemCap cueSheetItemCapEntity = CueSheetItemCap.builder()
                    .cueItemCapDivCd(cueSheetItemCap.getCueItemCapDivCd())
                    .capCtt(cueSheetItemCap.getCapCtt())
                    .capOrd(cueSheetItemCap.getCapOrd())
                    .lnNo(cueSheetItemCap.getLnNo())
                    .capPrvwId(cueSheetItemCap.getCapPrvwId())
                    .capClassCd(cueSheetItemCap.getCapClassCd())
                    .capPrvwUrl(cueSheetItemCap.getCapPrvwUrl())
                    .colorInfo(cueSheetItemCap.getColorInfo())
                    .capRmk(cueSheetItemCap.getCapRmk())
                    .orgCueItemCapId(cueSheetItemCap.getOrgCueItemCapId())
                    .inputrId(userId)
                    .capTemplate(cueSheetItemCap.getCapTemplate())
                    .cueSheetItem(cueSheetItem)
                    .symbol(cueSheetItemCap.getSymbol())
                    .build();

            cueSheetItemCapRepotitory.save(cueSheetItemCapEntity);
        }

    }

    //드레그앤드랍 큐시트 아이템 등록
    public CueSheetItem copyItem(Long cueId, CueSheetItem cueSheetItem, Article article, CueSheetTemplate cueSheetTemplate, String userId, String spareYn) {

        CueSheet cueSheet = CueSheet.builder().cueId(cueId).build();

        CueSheetItem cueSheetItemEntity = null;

        if ("Y".equals(spareYn)) {
            cueSheetItemEntity = CueSheetItem.builder()
                    .cueItemTitl(cueSheetItem.getCueItemTitl())
                    .cueItemTitlEn(cueSheetItem.getCueItemTitlEn())
                    .cueItemCtt(cueSheetItem.getCueItemCtt())
                    .cueItemOrd(cueSheetItem.getCueItemOrd())
                    .cueItemOrdCd(cueSheetItem.getCueItemOrdCd())
                    .cueItemTime(cueSheetItem.getCueItemTime())
                    .cueItemFrmCd(cueSheetItem.getCueItemFrmCd())
                    .cueItemDivCd(cueSheetItem.getCueItemDivCd())
                    .brdcStCd(cueSheetItem.getBrdcStCd())
                    .brdcClk(cueSheetItem.getBrdcClk())
                    .chrgrId(cueSheetItem.getChrgrId())
                    .chrgrNm(cueSheetItem.getChrgrNm())
                    .artclCapStCd(cueSheetItem.getArtclCapStCd())
                    .cueArtclCapChgYn(cueSheetItem.getCueArtclCapChgYn())
                    .cueArtclCapChgDtm(cueSheetItem.getCueArtclCapChgDtm())
                    .cueArtclCapStCd(cueSheetItem.getCueArtclCapStCd())
                    .rmk(userId)
                    .lckYn(cueSheetItem.getLckYn())
                    .delYn(cueSheetItem.getDelYn())
                    .delDtm(cueSheetItem.getDelDtm())
                    .lckDtm(cueSheetItem.getLckDtm())
                    .cueItemTypCd(cueSheetItem.getCueItemTypCd())
                    .mediaChCd(cueSheetItem.getMediaChCd())
                    .cueItemBrdcDtm(cueSheetItem.getCueItemBrdcDtm())
                    .capChgYn(cueSheetItem.getCapChgYn())
                    .capChgDtm(cueSheetItem.getCapChgDtm())
                    .capStCd(cueSheetItem.getCapStCd())
                    .artclStCd(cueSheetItem.getArtclStCd())
                    .mediaDurtn(cueSheetItem.getMediaDurtn())
                    .newsBreakYn(cueSheetItem.getNewsBreakYn())
                    .inputrId(userId)
                    .artclTop(cueSheetItem.getArtclTop())
                    .headLn(cueSheetItem.getHeadLn())
                    .artclRef(cueSheetItem.getArtclRef())
                    .spareYn(spareYn)
                    .cueSheet(cueSheet)
                    .article(article)
                    .cueSheetTemplate(cueSheetTemplate)
                    .build();
        }else {
            cueSheetItemEntity = CueSheetItem.builder()
                    .cueItemTitl(cueSheetItem.getCueItemTitl())
                    .cueItemTitlEn(cueSheetItem.getCueItemTitlEn())
                    .cueItemCtt(cueSheetItem.getCueItemCtt())
                    .cueItemOrd(cueSheetItem.getCueItemOrd())
                    .cueItemOrdCd(cueSheetItem.getCueItemOrdCd())
                    .cueItemTime(cueSheetItem.getCueItemTime())
                    .cueItemFrmCd(cueSheetItem.getCueItemFrmCd())
                    .cueItemDivCd(cueSheetItem.getCueItemDivCd())
                    .brdcStCd(cueSheetItem.getBrdcStCd())
                    .brdcClk(cueSheetItem.getBrdcClk())
                    .chrgrId(cueSheetItem.getChrgrId())
                    .chrgrNm(cueSheetItem.getChrgrNm())
                    .artclCapStCd(cueSheetItem.getArtclCapStCd())
                    .cueArtclCapChgYn(cueSheetItem.getCueArtclCapChgYn())
                    .cueArtclCapChgDtm(cueSheetItem.getCueArtclCapChgDtm())
                    .cueArtclCapStCd(cueSheetItem.getCueArtclCapStCd())
                    .rmk(cueSheetItem.getRmk())
                    .lckYn(cueSheetItem.getLckYn())
                    .delYn(cueSheetItem.getDelYn())
                    .delDtm(cueSheetItem.getDelDtm())
                    .lckDtm(cueSheetItem.getLckDtm())
                    .cueItemTypCd(cueSheetItem.getCueItemTypCd())
                    .mediaChCd(cueSheetItem.getMediaChCd())
                    .cueItemBrdcDtm(cueSheetItem.getCueItemBrdcDtm())
                    .capChgYn(cueSheetItem.getCapChgYn())
                    .capChgDtm(cueSheetItem.getCapChgDtm())
                    .capStCd(cueSheetItem.getCapStCd())
                    .artclStCd(cueSheetItem.getArtclStCd())
                    .mediaDurtn(cueSheetItem.getMediaDurtn())
                    .newsBreakYn(cueSheetItem.getNewsBreakYn())
                    .inputrId(userId)
                    .artclTop(cueSheetItem.getArtclTop())
                    .headLn(cueSheetItem.getHeadLn())
                    .artclRef(cueSheetItem.getArtclRef())
                    .spareYn(spareYn)
                    .cueSheet(cueSheet)
                    .article(article)
                    .cueSheetTemplate(cueSheetTemplate)
                    .build();
        }

        cueSheetItemRepository.save(cueSheetItemEntity);

        return cueSheetItemEntity;
    }

    //드레그앤드랍 큐시트 템플릿 심볼 복사
    public void copyTemplateSymbol(List<CueTmplSymbol> cueTmplSymbol, Long copyCueItemId){

        CueSheetItem cueSheetItem = CueSheetItem.builder().cueItemId(copyCueItemId).build();

        for (CueTmplSymbol cueSheetItemSymbol : cueTmplSymbol) {

            Symbol symbol = cueSheetItemSymbol.getSymbol();
            Integer ord = cueSheetItemSymbol.getOrd();

            CueSheetItemSymbol cueSheetItemSymbolEntity = CueSheetItemSymbol.builder()
                    .symbol(symbol)
                    .cueSheetItem(cueSheetItem)
                    .ord(ord)
                    .build();

            cueSheetItemSymbolRepository.save(cueSheetItemSymbolEntity);
        }
    }

    //큐시트 아이템 카피 심볼 등록
    public void createItemSymbol(Set<CueSheetItemSymbol> cueSheetItemSymbols, Long copyCueItemId) {

        CueSheetItem cueSheetItem = CueSheetItem.builder().cueItemId(copyCueItemId).build();

        for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbols){

            Symbol symbol = cueSheetItemSymbol.getSymbol();
            Integer ord = cueSheetItemSymbol.getOrd();

            CueSheetItemSymbol cueSheetItemSymbolEntity = CueSheetItemSymbol.builder()
                    .symbol(symbol)
                    .cueSheetItem(cueSheetItem)
                    .ord(ord)
                    .build();

            cueSheetItemSymbolRepository.save(cueSheetItemSymbolEntity);
        }

    }

    //큐시트 아이템 생성[Drag and Drop Article]
    public void createCueItemArticle(Long cueId, Long artclId, Integer cueItemOrd, String cueItemDivCd, String spareYn, String userId) throws Exception {

        //큐시트 아이디로 큐시트 조회 및 존재유무 확인.
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);

        //기사 복사[복사된 기사 Id get]
        Article copyArtcl = copyArticle(artclId, cueId, userId);

        //예비큐시트 값 set
        if (spareYn == null && spareYn.trim().isEmpty()) {
            spareYn = "N"; //예비여부값이 안들어오면 N 값 디폴트
        }

        //큐시트아이디 큐시트엔티티로 빌드 :: 큐시트아이템에 큐시트 아이디set해주기 위해
        //CueSheet cueSheet = CueSheet.builder().cueId(cueId).build();
        //큐시트아이템create 빌드

        CueSheetItem cueSheetItem = null;

        if ("Y".equals(spareYn)) {
            cueSheetItem = CueSheetItem.builder()
                    .cueSheet(cueSheet)
                    .cueItemOrd(cueItemOrd)
                    .cueItemDivCd(cueItemDivCd)
                    .inputrId(userId)
                    .article(copyArtcl)
                    .spareYn(spareYn)//예비큐시트 값
                    .rmk(userId)
                    .build();
        }else{

            cueSheetItem = CueSheetItem.builder()
                    .cueSheet(cueSheet)
                    .cueItemOrd(cueItemOrd)
                    .cueItemDivCd(cueItemDivCd)
                    .inputrId(userId)
                    .article(copyArtcl)
                    .spareYn(spareYn)//예비큐시트 값
                    .build();

        }


        //빌드된 큐시트아이템 등록
        cueSheetItemRepository.save(cueSheetItem);

        Long cueItemId = cueSheetItem.getCueItemId();

        //큐시트아이템 순서 update
        ordUpdateDrop(cueSheetItem, cueId, cueItemOrd, spareYn);

        /************ ORDERVER UP *************/
        cueSheet = addCueVer(cueSheet); // 큐시트 버전 정보 업데이트

        //String cueItemDivCd = cueSheetItem.getCueItemDivCd();
        /************ MQ messages *************/

        cueSheetTopicService.sendCueTopicCreate(cueSheet, cueId, cueItemId, artclId, null, "Create CueSheetItem",
                spareYn, "Y", "Y");

    }

    //큐시트 방송완료 후 예비큐시트 추가수정.
    public CueSheetItemSimpleDTO updateSpareCueItem(Long cueId, Long cueItemId, int cueItemOrd, String spareYn) throws JsonProcessingException {


        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);
        String cueStCd = cueSheet.getCueStCd();

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);
        cueSheetItemDTO.setSpareYn(spareYn);

        cueSheetItemMapper.updateFromDto(cueSheetItemDTO, cueSheetItem);

        cueSheetItemRepository.save(cueSheetItem);

        //ordUpdate(cueId, cueItemId, cueItemOrd, spareYn);

        CueSheetItemSimpleDTO cueSheetItemSimpleDTO = new CueSheetItemSimpleDTO();
        cueSheetItemSimpleDTO.setCueItemId(cueItemId);

        /************ MQ messages *************/
        //기사, 템플릿 이 포함되어 있는 경우 아이디를 TOPIC전송
        Article article = cueSheetItem.getArticle();
        CueSheetTemplate cueSheetTemplate = cueSheetItem.getCueSheetTemplate();
        Long artclId = null;
        Long cueTmpltId = null;
        if (ObjectUtils.isEmpty(article) == false) {
            artclId = article.getArtclId();
        }
        if (ObjectUtils.isEmpty(cueSheetTemplate) == false) {
            cueItemId = cueSheetTemplate.getCueTmpltId();
        }

        //String spareYn = cueSheetItem.getSpareYn();

        cueSheet = addCueVer(cueSheet);

        cueSheetTopicService.sendCueTopicCreate(cueSheet, cueId, cueItemId, artclId, cueTmpltId, "Update SpareCueSheetItem",
                spareYn, "Y", "N");

        return cueSheetItemSimpleDTO;


    }

    //큐시트 아이템 생성[Drag and Drop] List
    public void createCueItemList(List<CueSheetItemCreateListDTO> cueSheetItemCreateListDTO, Long cueId, String spareYn
            , String userId) throws Exception {

        //토큰 사용자 Id(현재 로그인된 사용자 ID)
        //String userId = userAuthService.authUser.getUserId();

        //예비큐시트 값 set
        if (spareYn == null && spareYn.trim().isEmpty()) {
            spareYn = "N"; //예비여부값이 안들어오면 N 값 디폴트
        }

        for (CueSheetItemCreateListDTO createListDTO : cueSheetItemCreateListDTO) {
            Long artclId = createListDTO.getArtclId();
            int cueItemOrd = createListDTO.getCueItemOrd();
            String cueItemDivCd = createListDTO.getCueItemDivCd();

            //기사 복사[복사된 기사 Id get]
            Article copyArtcl = copyArticle(artclId, cueId, userId);

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
            //ordUpdateDrop(cueSheetItem, cueId, cueItemOrd, spareYn);
        }

    }

    //큐시트 아이템 복구(del : N ) 으로 복구&&기사도 같이 복구
    public CueSheetItemSimpleDTO cueSheetItemRestore(Long cueId, Long cueItemId, Integer cueItemOrd) throws JsonProcessingException {

        //큐시트 아이디로 큐시트 조회 및 존재유무 확인.
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);

        CueSheetItem cueSheetItem = findDeletedCueItem(cueItemId);

        String cueDivCd = cueSheetItem.getCueItemDivCd();

        //큐시트 아이템이 기사타입 인 경우 큐시트아이템에 복사된 기사도 함께 복구
        if ("cue_article".equals(cueDivCd)) {

            deletedArticleUpdate(cueSheetItem); //삭제처리된 기사 복구(del : N )

        }

        CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);
        cueSheetItemDTO.setDelYn("N");//큐시트 아이템 삭제 플레그 N으로 수정등록
        cueSheetItemDTO.setCueItemOrd(cueItemOrd); //오더값 set

        cueSheetItemMapper.updateFromDto(cueSheetItemDTO, cueSheetItem);
        cueSheetItemRepository.save(cueSheetItem);

        //삭제된 큐시트 아이템이 복구될시 포함된 큐시트 미디어 정보도 함께 복구
        cueSheetMediaRestore(cueItemId);
        //삭제된 큐시트 아이템이 복구될시 포함된 큐시트 자막 정보도 함께 복구 ( delYn = "N" )
        cueSheetItemCapRestore(cueItemId);

        String spareYn = cueSheetItem.getSpareYn();

        ordUpdate(cueId, cueItemId, cueItemOrd, spareYn); //큐시트 순번 정렬

        //addCueVer(cueId, "Restore CueSheetItem", cueSheetItemDTO); //큐시트 버전업

        CueSheetItemSimpleDTO cueSheetItemSimpleDTO = new CueSheetItemSimpleDTO();
        cueSheetItemSimpleDTO.setCueItemId(cueItemId);

        /************ ORDERVER UP *************/
        cueSheet = addCueVer(cueSheet); // 큐시트 버전 정보 업데이트

        //String cueItemDivCd = cueSheetItem.getCueItemDivCd();
        /************ MQ messages *************/

        Article article = cueSheetItem.getArticle();
        Long artclId = null;
        if (ObjectUtils.isEmpty(article) == false) {
            artclId = article.getArtclId();
        }
        //String spareYn = cueSheetItem.getSpareYn();

        cueSheetTopicService.sendCueTopicCreate(cueSheet, cueId, cueItemId, artclId, null, "Restore CueSheetItem",
                spareYn, "Y", "Y");

        return cueSheetItemSimpleDTO;
    }

    //삭제된 큐시트 아이템이 복구될시 포함된 큐시트 자막 정보도 함께 복구 ( delYn = "N" )
    public void cueSheetItemCapRestore(Long cueItemId){

        List<CueSheetItemCap> cueSheetItemCapList = cueSheetItemCapRepotitory.findDeleteCueSheetItemCapList(cueItemId);

        for (CueSheetItemCap cueSheetItemCap : cueSheetItemCapList){

            CueSheetItemCapDTO cueSheetItemCapDTO = cueSheetItemCapMapper.toDto(cueSheetItemCap);
            cueSheetItemCapDTO.setDelYn("N");

            cueSheetItemCapMapper.updateFromDto(cueSheetItemCapDTO, cueSheetItemCap);

            cueSheetItemCapRepotitory.save(cueSheetItemCap);

        }

    }

    //삭제된 큐시트 아이템이 복구될시 포함된 큐시트 미디어 정보도 함께 복구 ( delYn = "N" )
    public void cueSheetMediaRestore(Long cueItemId){

        List<CueSheetMedia> cueSheetMediaList = cueSheetMediaRepository.findDeleteCueMediaList(cueItemId);

        for (CueSheetMedia cueSheetMedia : cueSheetMediaList){

            CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaMapper.toDto(cueSheetMedia);
            cueSheetMediaDTO.setDelYn("N");

            cueSheetMediaMapper.updateFromDto(cueSheetMediaDTO, cueSheetMedia);

            cueSheetMediaRepository.save(cueSheetMedia);
        }

    }

    //방송아이콘 url 추가
    public List<CueSheetItemDTO> setSymbol(List<CueSheetItemDTO> cueSheetItemDTOList) {

        for (CueSheetItemDTO cueSheetItemDTO : cueSheetItemDTOList) { //조회된 아이템에 List

            Long cueItemId = cueSheetItemDTO.getCueItemId(); //아이템 아이디 get

            //아이템 아이디로 방송아이콘 맵핑테이블 조회
            List<CueSheetItemSymbolDTO> cueSheetItemSymbolList = cueSheetItemDTO.getCueSheetItemSymbol();

            if (ObjectUtils.isEmpty(cueSheetItemSymbolList) == false) { //조회된 방송아콘 맵핑테이블 List가 있으면 url추가후 큐시트 아이템DTO에 추가

                //List<CueSheetItemSymbolDTO> cueSheetItemSymbolDTO = cueSheetItemSymbolMapper.toDtoList(cueSheetItemSymbolList);

                for (CueSheetItemSymbolDTO getCueSheetItemSymbol : cueSheetItemSymbolList) {

                    SymbolDTO symbolDTO = getCueSheetItemSymbol.getSymbol();

                    String fileLoc = symbolDTO.getAttachFile().getFileLoc();//파일로그 get
                    String url = fileUrl + fileLoc; //url + 파일로그

                    symbolDTO.setUrl(url);//방송아이콘이 저장된 url set

                    getCueSheetItemSymbol.setSymbol(symbolDTO);//url 추가 DTO set
                }


                cueSheetItemDTO.setCueSheetItemSymbol(cueSheetItemSymbolList); //아이템에 set방송아이콘List

            }
        }

        return cueSheetItemDTOList;

    }

    //방송아이콘 url 추가
    public List<CueSheetItemSymbolDTO> createUrl(List<CueSheetItemSymbolDTO> cueSheetItemSymbolDTOList) {

        //큐시트 아이템 방송아이콘 리스트를 Url을 추가해서 리턴해줄 DTO 리스트 생성
        List<CueSheetItemSymbolDTO> returnCueSheetItemSymbolDTOList = new ArrayList<>();

        if (CollectionUtils.isEmpty(cueSheetItemSymbolDTOList) == false) {

            for (CueSheetItemSymbolDTO cueSheetItemSymbolDTO : cueSheetItemSymbolDTOList) {

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

    //큐시트 아이템 등록( 템플릿 ) : 운영참조
    public void createTemplate(List<CueSheetItemCreateDTO> cueSheetItemCreateDTOList, Long cueId, String userId) throws JsonProcessingException {

        //큐시트 아이디로 큐시트 조회 및 존재유무 확인.
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);


        //큐시트 아이디 등록
        CueSheetSimpleDTO cueSheetDTO = CueSheetSimpleDTO.builder().cueId(cueId).build();

        for (CueSheetItemCreateDTO cueSheetItemCreateDTO : cueSheetItemCreateDTOList) {
            cueSheetItemCreateDTO.setCueSheet(cueSheetDTO);
            cueSheetItemCreateDTO.setInputrId(userId);

            CueSheetItem cueSheetItem = cueSheetItemCreateMapper.toEntity(cueSheetItemCreateDTO);
            cueSheetItemRepository.save(cueSheetItem); //큐시트아이템 등록

            Long cueItemId = cueSheetItem.getCueItemId();
            Integer cueItemOrd = cueSheetItem.getCueItemOrd();

            //큐시트 아이템 자막 리스트 get
            List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList = cueSheetItemCreateDTO.getCueSheetItemCap();
            //큐시트 아이템 자막 리스트가 있으면 등록
            if (CollectionUtils.isEmpty(cueSheetItemCapDTOList) == false) {
                createCap(cueSheetItemCapDTOList, cueItemId, userId);//큐시트 아이템 자막 리스트 등록
            }

            //심볼까지 같이 불러와서 등록
            List<CueSheetItemSymbolCreateDTO> cueSheetItemSymbolList = cueSheetItemCreateDTO.getCueSheetItemSymbol();
            if (CollectionUtils.isEmpty(cueSheetItemSymbolList) == false) {
                createSymbol(cueSheetItemSymbolList, cueItemId);
            }

            //큐시트 미디어 등록
            List<CueSheetMediaCreateDTO> cueSheetMediaCreateDTOS = cueSheetItemCreateDTO.getCueSheetMedia();
            if (CollectionUtils.isEmpty(cueSheetMediaCreateDTOS) == false){
                createTemplateMedia(cueSheetMediaCreateDTOS, cueSheet, cueItemId, userId);
            }

            String spareYn = cueSheetItem.getSpareYn();
            ordUpdateDrop(cueSheetItem, cueId, cueItemOrd, spareYn); //큐시트 순번 정렬
        }
    }

    //큐시트 운영참조 미디어 등록
    public void createTemplateMedia(List<CueSheetMediaCreateDTO> cueSheetMediaCreateDTOS, CueSheet cueSheet, Long cueItemId, String userId) throws JsonProcessingException {

        CueSheetItemSimpleDTO cueSheetItemSimpleDTO = CueSheetItemSimpleDTO.builder().cueItemId(cueItemId).build();
        //부조 정보를 가져온다 = "A, B"
        Long subrmId = cueSheet.getSubrmId();
        FacilityManage facilityManage = facilityManageService.findFacility(subrmId);
        String subrmNm = facilityManage.getFcltyDivCd();

        for (CueSheetMediaCreateDTO cueSheetMediaDTO : cueSheetMediaCreateDTOS){

            cueSheetMediaDTO.setCueSheetItem(cueSheetItemSimpleDTO);
            cueSheetMediaDTO.setInputrId(userId);

            CueSheetMedia cueSheetMedia = cueSheetMediaCreateMapper.toEntity(cueSheetMediaDTO);

            cueSheetMediaRepository.save(cueSheetMedia);

            Long mediaId = cueSheetMedia.getCueMediaId();
            Integer contentId = cueSheetMedia.getContId();

            //추후에 T는 클라우드 콘피그 교체
            lboxService.cueMediaTransfer(mediaId, contentId, subrmNm, false, false, "T");
        }

    }

    //큐시트 템플릿 방송아이콘 등록
    public void createSymbol(List<CueSheetItemSymbolCreateDTO> cueSheetItemSymbolList, Long cueItemId) {

        CueSheetItemSimpleDTO cueSheetItem = CueSheetItemSimpleDTO.builder().cueItemId(cueItemId).build();

        for (CueSheetItemSymbolCreateDTO cueSheetItemSymbolDTO : cueSheetItemSymbolList) {

            cueSheetItemSymbolDTO.setCueSheetItem(cueSheetItem);

            CueSheetItemSymbol cueSheetItemSymbol = cueSheetItemSymbolCreateMapper.toEntity(cueSheetItemSymbolDTO);

            cueSheetItemSymbolRepository.save(cueSheetItemSymbol);


        }

    }

    //큐시트 아이템 자막 등록
    public void createCap(List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList, Long cueItemId, String userId) {

        //큐시트 아이템 아이디 빌드
        CueSheetItemSimpleDTO cueSheetItem = CueSheetItemSimpleDTO.builder().cueItemId(cueItemId).build();

        //큐시트 아이템 자막 리스트 등록
        for (CueSheetItemCapCreateDTO capCreateDTO : cueSheetItemCapDTOList) {

            capCreateDTO.setCueSheetItem(cueSheetItem); //큐시트 아이템 아이디 set
            capCreateDTO.setInputrId(userId);//등록자 set

            CueSheetItemCap cueSheetItemCap = cueSheetItemCapCreateMapper.toEntity(capCreateDTO);

            cueSheetItemCapRepotitory.save(cueSheetItemCap);//등록
        }
    }

    //업데이트 큐시트 아이템 자막
    public void updateCap(List<CueSheetItemCapCreateDTO> cueSheetItemCapDTOList, Long cueItemId, String userId) {

        //큐시트아이템 자막 리스트 조회 [조회조건 큐시트 아이템 아이디]
        List<CueSheetItemCap> cueSheetItemCapList = cueSheetItemCapRepotitory.findCueSheetItemCapList(cueItemId);

        //조회된 큐시트아이템 자막 리스트 삭제처리.
        for (CueSheetItemCap cueSheetItemCap : cueSheetItemCapList) {

            Long cueItemCapId = cueSheetItemCap.getCueItemCapId();

            cueSheetItemCapRepotitory.deleteById(cueItemCapId);
        }

        //큐시트 아이템 자막 재등록
        createCap(cueSheetItemCapDTOList, cueItemId, userId);
    }

    public CueSheetItem cueItemFindOrFail(Long cueItemId) {

        Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findByCueItem(cueItemId);

        if (cueSheetItem.isPresent() == false) {
            throw new ResourceNotFoundException("CueSheetItem not found. CueSheet Item Id : " + cueItemId);
        }

        return cueSheetItem.get();

    }

    //기존
    public BooleanBuilder getSearch(Long artclId, Long cueId, String delYn, String spareYn) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //dsl q쿼리 생성
        QCueSheetItem qCueSheetItem = QCueSheetItem.cueSheetItem;

        if (delYn != null && delYn.trim().isEmpty() == false) {
            booleanBuilder.and(qCueSheetItem.delYn.eq(delYn));
        } else {
            booleanBuilder.and(qCueSheetItem.delYn.eq("N"));
        }
        //쿼리 where 조건 추가.
        if (ObjectUtils.isEmpty(artclId) == false) {
            booleanBuilder.and(qCueSheetItem.article.artclId.eq(artclId));
        }

        //쿼리 where 조건 추가.
        if (ObjectUtils.isEmpty(cueId) == false) {
            booleanBuilder.and(qCueSheetItem.cueSheet.cueId.eq(cueId));
        }

        //스페어 여부
        if (spareYn != null && spareYn.trim().isEmpty() == false) {
            booleanBuilder.and(qCueSheetItem.spareYn.eq(spareYn));
        }

        return booleanBuilder;
    }

    public BooleanBuilder getSearchOrd(Long cueId, String spareYn) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        //dsl q쿼리 생성
        QCueSheetItem qCueSheetItem = QCueSheetItem.cueSheetItem;

        booleanBuilder.and(qCueSheetItem.delYn.eq("N"));
        //쿼리 where 조건 추가.
        if (ObjectUtils.isEmpty(cueId) == false) {
            booleanBuilder.and(qCueSheetItem.cueSheet.cueId.eq(cueId));
        }

        if (spareYn != null && spareYn.trim().isEmpty() == false) {
            booleanBuilder.and(qCueSheetItem.spareYn.eq(spareYn));
        }

        return booleanBuilder;
    }


    //기사시퀀스값 get
    public Integer getArticleOrd(Article article, Long artclId) {

        Long orgArtclId = article.getOrgArtclId();


        if (artclId.equals(orgArtclId)) { //원본일경우

            Optional<Integer> getMaxOrd = articleRepository.findArticleOrd(artclId);

            if (getMaxOrd.isPresent() == false) {

                return 0;
            }

            return getMaxOrd.get() + 1;
        } else {


            Optional<Integer> getMaxOrd = articleRepository.findArticleOrd(orgArtclId);

            if (getMaxOrd.isPresent() == false) {

                return 0;
            }

            return getMaxOrd.get() + 1;


        }


    }

    public Article copyArticle(Long artclId, Long cueId, String userId) throws Exception {

        /*********************/
        /* 기사를 저장하는 부분 */

        //기사 아이디로 기사 검색
        Optional<Article> getArticle = articleRepository.findArticle(artclId);
        //조회된 기사 빈값 검사.

        if (getArticle.isPresent() == false) {
            throw new ResourceNotFoundException(String.format("기사아이디에 해당하는 기사가 없습니다. {%ld}", artclId));
        }

        //큐시트 아이디로 큐시트 조회 및 존재유무 확인.
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);

        //조회된 기사 get
        Article article = getArticle.get();

        //기사 시퀀스[오리지널 기사 0 = 그밑으로 복사된 기사 + 1 증가]
        Integer maxArtclOrd = getArticleOrd(article, artclId);

        // 기사 저장하기 위한 기사복사 엔터티 생성
        Article articleEntity = getArticleEntity(article, maxArtclOrd, cueSheet);
        articleRepository.save(articleEntity); //복사된 기사 등록

        Set<ArticleCap> articleCapList = article.getArticleCap(); //기사자막 리스트 get
        Set<AnchorCap> anchorCapList = article.getAnchorCap(); //앵커자막 리스트 get

        articleService.articleActionLogCreate(articleEntity, userId); //기사 액션 로그 등록
        Long articleHistId = articleService.createArticleHist(articleEntity);//기사 이력 create

        /*********************/
        /* 기사 자막을 저장하는 부분 */

        if (CollectionUtils.isEmpty(articleCapList) == false) {
            for (ArticleCap articleCap : articleCapList) {

                ArticleCap articleCapEntity = articleCapEntityBuild(articleCap, articleEntity);

                articleCapRepository.save(articleCapEntity);

                articleService.createArticleCapHist(articleCapEntity, articleHistId); //기사자막 이력 저장.
            }
        }

        /*********************/
        /* 앵커 자막을 저장하는 부분 */

        if (CollectionUtils.isEmpty(anchorCapList) == false) {
            for (AnchorCap articleCap : anchorCapList) {

                AnchorCap anchorCapEntity = anchorCapEntityBuild(articleCap, articleEntity);

                anchorCapRepository.save(anchorCapEntity);

                articleService.createAnchorCapHist(anchorCapEntity, articleHistId); //앵커자막 이력 등록

            }
        }

        /*********************/
        /* 미디어 정보 저장 하는 부분 */

        //기사 아이디로 기사 미디어 조회
        List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaList(artclId);
        //List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaList(artclId);

        //부조 정보를 가져온다 = "A, B"
        Long subrmId = cueSheet.getSubrmId();
        FacilityManage facilityManage = facilityManageService.findFacility(subrmId);
        String subrmNm = facilityManage.getFcltyDivCd();

        //조회된 기사 미디어가 있으면 복사된 기사 아이디로 기사미디어 신규 저장.
        if (ObjectUtils.isEmpty(articleMediaList) == false) {

            for (ArticleMedia articleMedia : articleMediaList) {

                String mediaTypCd = articleMedia.getMediaTypCd();

                //빽드롭 빼고 일반영상만 매칭
                if ("media_typ_001".equals(mediaTypCd)) {
                    ArticleMedia articleMediaEntity = getArticleMediaEntity(articleMedia, articleEntity);

                    articleMediaRepository.save(articleMediaEntity);

                    Long mediaId = articleMediaEntity.getArtclMediaId();
                    Integer contentId = articleMediaEntity.getContId();

                    //추후에 T는 클라우드 콘피그 교체
                    lboxService.mediaTransfer(mediaId, contentId, subrmNm, "T", false, false);

                }
            }
        }

        /*List<ArticleActionLogDTO> articleActionLogs = articleActionLogService.findAll(artclId);

        for (ArticleActionLogDTO articleActionLogDTO : articleActionLogs){

            String message = articleActionLogDTO.getMessage();

            //메세지에 픽스가 포함된 액션로그 복사시 등록 [ 화면에 픽스된 일시, 픽스자 표시. ]
            if (message.contains("fix")) {

                ArticleActionLog articleActionLog = ArticleActionLog.builder()
                        .message(message)
                        .action(articleActionLogDTO.getAction())
                        .inputDtm(articleActionLogDTO.getInputDtm())
                        .inputrId(articleActionLogDTO.getInputrId())
                        .artclInfo(articleActionLogDTO.getArtclInfo())
                        .artclCapInfo(articleActionLogDTO.getArtclCapInfo())
                        .anchorCapInfo(articleActionLogDTO.getAnchorCapInfo())
                        .article(articleEntity) //신규생성된 기사set
                        .build();

                articleActionLogRepository.save(articleActionLog);
            }

        }*/

        return articleEntity;

    }

    // 기사 저장하기 위한 엔터티 만들기 2021-10-27
    private Article getArticleEntity(Article article, Integer maxArtclOrd, CueSheet cueSheet) {

        Program program = cueSheet.getProgram();
        String brdcPgmId = "";
        //String brdcSchdDtm = "";

        //방송프로그램이 있을경우 방송프로그램 아이디 set
        if (ObjectUtils.isEmpty(program) == false) {

            brdcPgmId = program.getBrdcPgmId();
            //brdcSchdDtm = program.
        }

        Long artclId = article.getArtclId();
        Long orgArtclId = article.getOrgArtclId();//원본기사 아이디

        String getOrgApprvDivCd = article.getApprvDivCd(); //원본 픽스구분 코드를 가져온다.
        String artclFixUser = article.getArtclFixUser(); // 원본 기사 픽스자를 가져온다
        String editorFixUser = article.getEditorFixUser(); // 원본 에디터 픽스자를 가져온다
        String newApprvDivCd = ""; //복사본에 대입해줄 픽스구분코드

        //원본 픽스구분값이 앵커픽스,데이커 픽스일 경우 article_fix, editor_fix 로변경
        //에디터 픽스자가 있을경우 에디터픽스로, 아닐경우 기사픽스로 셋팅
        if ("anchor_fix".equals(getOrgApprvDivCd) || "desk_fix".equals(getOrgApprvDivCd) || "editor_fix".equals(getOrgApprvDivCd)) {
           
                newApprvDivCd = "article_fix";

        } else {
            newApprvDivCd = getOrgApprvDivCd; //none_fix값 처리
        }

        if (artclId.equals(orgArtclId)) { //원본기사가 아이디가없고 최초 복사일시

            return Article.builder()
                    .chDivCd(article.getChDivCd())
                    .artclKindCd(article.getArtclKindCd())
                    .artclFrmCd(article.getArtclFrmCd())
                    .artclDivCd(article.getArtclDivCd())
                    .artclFldCd(article.getArtclFldCd())
                    .apprvDivCd(newApprvDivCd)//픽스구분코트
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
                    .apprvDtm(article.getApprvDtm())
                    .artclOrd(maxArtclOrd)//기사 시퀀스 +1
                    .brdcCnt(article.getBrdcCnt())
                    .orgArtclId(article.getArtclId())//원본기사 아이디set
                    .urgYn(article.getUrgYn())
                    .frnotiYn(article.getFrnotiYn())
                    .embgYn(article.getEmbgYn())
                    .embgDtm(article.getEmbgDtm())
                    .inputrNm(article.getInputrNm())
                    .notiYn(article.getNotiYn())
                    .regAppTyp(article.getRegAppTyp())
                    .brdcPgmId(brdcPgmId) //프로그램 아이디
                    .brdcSchdDtm(article.getBrdcSchdDtm())//방송시간
                    .inputrId(article.getInputrId())
                    .apprvrId(article.getApprvrId())
                    .rptrId(article.getRptrId())
                    .artclCttTime(article.getArtclCttTime())
                    .ancMentCttTime(article.getAncMentCttTime())
                    .artclExtTime(article.getArtclExtTime())
                    .videoTime(article.getVideoTime())
                    .deptCd(article.getDeptCd())
                    //.deviceCd(article.getDeviceCd())
                    .parentArtlcId(article.getArtclId())//복사한 기사 아이디 set
                    .issue(article.getIssue())
                    .cueSheet(cueSheet)//큐시트 아이디 set
                    .artclFixUser(article.getArtclFixUser())
                    //.editorFixUser(article.getEditorFixUser())
                    //.anchorFixUser(article.getAnchorFixUser())
                    //.deskFixUser(article.getDeskFixUser())
                    .artclFixDtm(article.getArtclFixDtm())
                    //.editorFixDtm(article.getEditorFixDtm())
                    //.anchorFixDtm(article.getAnchorFixDtm())
                    //.deskFixDtm(article.getDeskFixDtm())
                    .build();
        } else { //원본기사 아이디가 있을시[복사된 기사 다시 복사일시]

            return Article.builder()
                    .chDivCd(article.getChDivCd())
                    .artclKindCd(article.getArtclKindCd())
                    .artclFrmCd(article.getArtclFrmCd())
                    .artclDivCd(article.getArtclDivCd())
                    .artclFldCd(article.getArtclFldCd())
                    .apprvDivCd(newApprvDivCd) //픽스구분코트
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
                    .apprvDtm(article.getApprvDtm())
                    .artclOrd(maxArtclOrd )//기사 시퀀스 +1
                    .brdcCnt(article.getBrdcCnt())
                    .orgArtclId(article.getOrgArtclId())//원본기사 아이디set
                    .urgYn(article.getUrgYn())
                    .frnotiYn(article.getFrnotiYn())
                    .embgYn(article.getEmbgYn())
                    .embgDtm(article.getEmbgDtm())
                    .inputrNm(article.getInputrNm())
                    .notiYn(article.getNotiYn())
                    .regAppTyp(article.getRegAppTyp())
                    .brdcPgmId(article.getBrdcPgmId())
                    .brdcSchdDtm(article.getBrdcSchdDtm())
                    .inputrId(article.getInputrId())
                    .apprvrId(article.getApprvrId())
                    .rptrId(article.getRptrId())
                    .artclCttTime(article.getArtclCttTime())
                    .ancMentCttTime(article.getAncMentCttTime())
                    .artclExtTime(article.getArtclExtTime())
                    .videoTime(article.getVideoTime())
                    .deptCd(article.getDeptCd())
                    //.deviceCd(article.getDeviceCd())
                    .parentArtlcId(article.getArtclId())//복사한 기사 아이디 set
                    .issue(article.getIssue())
                    .cueSheet(cueSheet)//큐시트 아이디 set
                    .artclFixUser(article.getArtclFixUser())
                    //.editorFixUser(article.getEditorFixUser())
                    //.anchorFixUser(article.getAnchorFixUser())
                    //.deskFixUser(article.getDeskFixUser())
                    .artclFixDtm(article.getArtclFixDtm())
                    //.editorFixDtm(article.getEditorFixDtm())
                    //.anchorFixDtm(article.getAnchorFixDtm())
                    //.deskFixDtm(article.getDeskFixDtm())
                    .build();
        }
    }

    //기사자막 빌드
    public ArticleCap articleCapEntityBuild(ArticleCap getArticleCap, Article articleEntity) {
        return ArticleCap.builder()
                .capDivCd(getArticleCap.getCapDivCd())
                .lnNo(getArticleCap.getLnNo())
                .lnOrd(getArticleCap.getLnOrd())
                .capCtt(getArticleCap.getCapCtt())
                .capRmk(getArticleCap.getCapRmk())
                .article(articleEntity)
                .capTemplate(getArticleCap.getCapTemplate())
                .symbol(getArticleCap.getSymbol())
                .build();
    }

    //앵커자막 빌드
    public AnchorCap anchorCapEntityBuild(AnchorCap articleCap, Article articleEntity) {

        return AnchorCap.builder()
                .capDivCd(articleCap.getCapDivCd())
                .lnNo(articleCap.getLnNo())
                .lnOrd(articleCap.getLnOrd())
                .capCtt(articleCap.getCapCtt())
                .capRmk(articleCap.getCapRmk())
                .article(articleEntity)
                .capTemplate(articleCap.getCapTemplate())
                .symbol(articleCap.getSymbol())
                .build();

    }

    public ArticleMedia getArticleMediaEntity(ArticleMedia articleMedia, Article articleEntity) {

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
                .videoId(articleMedia.getVideoId())
                .artclMediaTitl(articleMedia.getArtclMediaTitl())
                .article(articleEntity)
                .build();
    }

    public void ordUpdate(Long cueId, Long cueItemId, Integer cueItemOrd, String spareYn) {


        CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheetItemDTO updateCueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);
        updateCueSheetItemDTO.setCueItemOrd(cueItemOrd);

        cueSheetItemMapper.updateFromDto(updateCueSheetItemDTO, cueSheetItem);
        cueSheetItemRepository.save(cueSheetItem); //큐시트아이템 등록

        //큐시트 아이템 순번 재등록
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemListSpareYn(cueId, spareYn);

        for (int i = cueSheetItemList.size() - 1; i >= 0; i--) {
            if (cueItemId.equals(cueSheetItemList.get(i).getCueItemId()) == false) {
                continue;
            }
            cueSheetItemList.remove(i);//신규 등록된 큐시트 아이템 리스트에서 삭제
        }

        cueSheetItemList.add(cueItemOrd, cueSheetItem); //신규등록하려는 큐시트 아이템 원하는 순번에 리스트 추가

        //조회된 큐시트 아이템 Ord 업데이트
        int index = 0;
        for (CueSheetItem cueSheetItems : cueSheetItemList) {

            int setDisplayCd = index + 1;
            String displayCd = Integer.toString(setDisplayCd);

            CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItems);
            cueSheetItemDTO.setCueItemOrd(index);//순번 재등록
            cueSheetItemDTO.setCueItemOrdCd(displayCd);
            CueSheetItem setCueSheetItem = cueSheetItemMapper.toEntity(cueSheetItemDTO);
            cueSheetItemRepository.save(setCueSheetItem);//순번 업데이트
            index++;//순번 + 1
        }

    }

    public void deleteOrdUpdate(Long cueId, Long cueItemId, String spareYn) {


        /*CueSheetItem cueSheetItem = cueItemFindOrFail(cueItemId);

        CueSheetItemDTO updateCueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItem);
        updateCueSheetItemDTO.setCueItemOrd(cueItemOrd);

        cueSheetItemMapper.updateFromDto(updateCueSheetItemDTO, cueSheetItem);
        cueSheetItemRepository.save(cueSheetItem); //큐시트아이템 등록*/

        //큐시트 아이템 순번 재등록
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemListSpareYn(cueId, spareYn);

        for (int i = cueSheetItemList.size() - 1; i >= 0; i--) {
            if (!cueItemId.equals(cueSheetItemList.get(i).getCueItemId())) {
                continue;
            }
            cueSheetItemList.remove(i);//신규 등록된 큐시트 아이템 리스트에서 삭제
        }

        //cueSheetItemList.add(cueItemOrd, cueSheetItem); //신규등록하려는 큐시트 아이템 원하는 순번에 리스트 추가

        //조회된 큐시트 아이템 Ord 업데이트
        int index = 0;
        for (CueSheetItem cueSheetItems : cueSheetItemList) {

            int setDisplayCd = index + 1;
            String displayCd = Integer.toString(setDisplayCd);

            CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItems);
            cueSheetItemDTO.setCueItemOrd(index);//순번 재등록
            cueSheetItemDTO.setCueItemOrdCd(displayCd);
            CueSheetItem setCueSheetItem = cueSheetItemMapper.toEntity(cueSheetItemDTO);
            cueSheetItemRepository.save(setCueSheetItem);//순번 업데이트
            index++;//순번 + 1
        }

    }

    //큐시트 버전 카운트 증가
    public CueSheet addCueVer(CueSheet cueSheet) {

        /*Optional<CueSheet> optionalCueSheet = cueSheetRepository.findByCue(cueId);

        if (optionalCueSheet.isPresent() == false){
            throw new ResourceNotFoundException("큐시트를 찾을 수 없습니다. 큐시트 아이디 : "+cueId);
        }

        CueSheet cueSheet = optionalCueSheet.get();*/

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);
        cueSheetDTO.setCueVer(cueSheet.getCueVer() + 1); //큐시트 버전 + 1

        cueSheetMapper.updateFromDto(cueSheetDTO, cueSheet); //큐시트 버전+1 된 정보를 엔티티정보에 업데이트

        cueSheetRepository.save(cueSheet); //큐시트 버전업 수정

        return cueSheet;
        //sendCueTopic(cueSheet, eventCd, object);

    }

    //큐시트 순서변경 버전 카운트 증가
    public CueSheet addCueOrdVer(Long cueId) {

        Optional<CueSheet> optionalCueSheet = cueSheetRepository.findByCue(cueId);

        if (optionalCueSheet.isPresent() == false) {
            throw new ResourceNotFoundException("큐시트를 찾을 수 없습니다. 큐시트 아이디 : " + cueId);
        }

        CueSheet cueSheet = optionalCueSheet.get();

        CueSheetDTO cueSheetDTO = cueSheetMapper.toDto(cueSheet);
        cueSheetDTO.setCueOderVer(cueSheet.getCueOderVer() + 1); //큐시트 버전 + 1

        cueSheetMapper.updateFromDto(cueSheetDTO, cueSheet); //큐시트 버전+1 된 정보를 엔티티정보에 업데이트

        cueSheetRepository.save(cueSheet); //큐시트 버전업 수정

        return cueSheet;
        //sendCueTopic(cueSheet, eventCd, object);

    }

    //삭제된 큐시트 아이템 조회
    public CueSheetItem findDeletedCueItem(Long cueItemId) {

        Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findDeletedCueItem(cueItemId);

        if (cueSheetItem.isPresent() == false) {
            throw new ResourceNotFoundException("삭제된 큐시트 아이템을 찾을 수 없습니다. 큐시트 아이템 아이디" + cueItemId);
        }

        return cueSheetItem.get();

    }

    //삭제처리된 기사 복구(del : N )
    public void deletedArticleUpdate(CueSheetItem cueSheetItem) {

        Article article = cueSheetItem.getArticle(); //조회된 큐시트아이템에 포함된 기사를 가져온다.

        ArticleDTO articleDTO = articleMapper.toDto(article);
        articleDTO.setDelYn("N"); //기사 삭제 플레그 N으로 업데이트

        articleMapper.updateFromDto(articleDTO, article);

        articleRepository.save(article); //기사 삭제 플레그 N으로 수정등록
    }

   /* //큐시트 토픽 메세지 전송
    public void sendCueTopicCreate(CueSheet cueSheet, Long cueId, Long cueItemId, Long artclId, Long cueTmpltId, String eventId,
                                   String spareYn, String prompterFlag, String videoTakerFlag) throws JsonProcessingException {

        //토픽메세지 ArticleTopicDTO Json으로 변환후 send
        TakerCueSheetTopicDTO takerCueSheetTopicDTO = new TakerCueSheetTopicDTO();
        //모델부분은 안넣어줘도 될꺼같음.
        takerCueSheetTopicDTO.setEvent_id(eventId);
        takerCueSheetTopicDTO.setCue_id(cueId);
        takerCueSheetTopicDTO.setCue_ver(cueSheet.getCueVer());
        takerCueSheetTopicDTO.setCue_oder_ver(cueSheet.getCueOderVer());
        takerCueSheetTopicDTO.setCue_item_id(cueItemId); //변경된 내용 추가
        takerCueSheetTopicDTO.setArtcl_id(artclId);
        takerCueSheetTopicDTO.setCue_tmplt_id(cueTmpltId);
        takerCueSheetTopicDTO.setSpare_yn(spareYn);
        takerCueSheetTopicDTO.setPrompter(prompterFlag);
        takerCueSheetTopicDTO.setVideo_taker(videoTakerFlag);
        String json = marshallingJsonHelper.MarshallingJson(takerCueSheetTopicDTO);

        //interface에 큐메세지 전송
        topicSendService.topicInterface(json);


        CueSheetWebTopicDTO cueSheetWebTopicDTO = new CueSheetWebTopicDTO();
        cueSheetWebTopicDTO.setEventId(eventId);
        cueSheetWebTopicDTO.setCueId(cueId);
        cueSheetWebTopicDTO.setCueItemId(cueItemId);
        cueSheetWebTopicDTO.setArtclId(artclId);
        cueSheetWebTopicDTO.setCueVer(cueSheet.getCueVer());
        cueSheetWebTopicDTO.setCueOderVer(cueSheet.getCueOderVer());
        cueSheetWebTopicDTO.setSpareYn(spareYn);
        String webJson = marshallingJsonHelper.MarshallingJson(cueSheetWebTopicDTO);
        //web에 큐메세지 전송
        topicSendService.topicWeb(webJson);

    }*/

    //큐시트아이템 순서 update
    public void ordUpdateDrop(CueSheetItem cueSheetItem, Long cueId, Integer cueItemOrd, String spareYn) {

        // 쿼리문 만들기
        BooleanBuilder booleanBuilder = getSearchOrd(cueId, spareYn);

        //큐시트 아이템 순번 재등록
        List<CueSheetItem> cueSheetItemList = (List<CueSheetItem>) cueSheetItemRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "cueItemOrd"));
        if (cueSheetItemList == null)
            return;

        //ClearCueSheetList(cueSheetItemList);

        Long cueSheetItemId = cueSheetItem.getCueItemId();
        for (int i = cueSheetItemList.size() - 1; i >= 0; i--) { //out of index때문에 역순으로 검사.
            if (cueSheetItemId.equals(cueSheetItemList.get(i).getCueItemId()) == false) {
                continue;
            }
            cueSheetItemList.remove(i);//신규 등록된 큐시트 아이템 리스트에서 삭제
        }

        cueSheetItemList.add(cueItemOrd, cueSheetItem); //신규등록하려는 큐시트 아이템 원하는 순번에 리스트 추가

        //조회된 큐시트 아이템 Ord 업데이트
        int index = 0;
        for (CueSheetItem cueSheetItems : cueSheetItemList) {

            int setDisplayCd = index + 1;
            String displayCd = Integer.toString(setDisplayCd);

            CueSheetItemDTO cueSheetItemDTO = cueSheetItemMapper.toDto(cueSheetItems);
            cueSheetItemDTO.setCueItemOrd(index);//순번 재등록
            cueSheetItemDTO.setCueItemOrdCd(displayCd);
            CueSheetItem setCueSheetItem = cueSheetItemMapper.toEntity(cueSheetItemDTO);
            cueSheetItemRepository.save(setCueSheetItem);//순번 업데이트
            index++;//순번 + 1
        }
    }


}


/*
    //큐시트 아이템 순서변경
    public void ordCdUpdate(Long cueId, String spareYn, List<CueSheetItemOrdUpdateDTO> cueSheetItemOrdUpdateDTOList) throws JsonProcessingException {

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
        } else {
            throw new ResourceNotFoundException("조회된 큐시트아이템과 업데이트할 큐시트아이템 값이 맞지 않습니다.");
        }

        */
/************ ORDERVER UP *************//*

        CueSheet cueSheet = addCueOrdVer(cueId);

        */
/************ MQ messages *************//*

        sendCueTopicCreate(cueSheet, cueId, null, null, null, "UpdateOrd CueSheet cueSheetId : " + cueId,
                spareYn, "Y", "Y");

    }
*/
