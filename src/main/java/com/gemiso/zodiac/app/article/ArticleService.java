package com.gemiso.zodiac.app.article;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemiso.zodiac.app.anchorCap.AnchorCap;
import com.gemiso.zodiac.app.anchorCap.AnchorCapRepository;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapCreateDTO;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapSimpleDTO;
import com.gemiso.zodiac.app.anchorCap.mapper.AnchorCapCreateMapper;
import com.gemiso.zodiac.app.anchorCap.mapper.AnchorCapSimpleMapper;
import com.gemiso.zodiac.app.anchorCapHist.AnchorCapHist;
import com.gemiso.zodiac.app.anchorCapHist.AnchorCapHistRepository;
import com.gemiso.zodiac.app.anchorCapHist.dto.AnchorCapHistCreateDTO;
import com.gemiso.zodiac.app.anchorCapHist.mapper.AnchorCapHistCreateMapper;
import com.gemiso.zodiac.app.article.dto.*;
import com.gemiso.zodiac.app.article.mapper.ArticleCreateMapper;
import com.gemiso.zodiac.app.article.mapper.ArticleLockMapper;
import com.gemiso.zodiac.app.article.mapper.ArticleMapper;
import com.gemiso.zodiac.app.article.mapper.ArticleUpdateMapper;
import com.gemiso.zodiac.app.articleActionLog.ArticleActionLog;
import com.gemiso.zodiac.app.articleActionLog.ArticleActionLogRepository;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleCap.ArticleCapRepository;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapCreateDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapSimpleDTO;
import com.gemiso.zodiac.app.articleCap.mapper.ArticleCapCreateMapper;
import com.gemiso.zodiac.app.articleCap.mapper.ArticleCapSimpleMapper;
import com.gemiso.zodiac.app.articleCapHist.ArticleCapHist;
import com.gemiso.zodiac.app.articleCapHist.ArticleCapHistRepository;
import com.gemiso.zodiac.app.articleCapHist.dto.ArticleCapHistCreateDTO;
import com.gemiso.zodiac.app.articleCapHist.mapper.ArticleCapHistCreateMapper;
import com.gemiso.zodiac.app.articleHist.ArticleHist;
import com.gemiso.zodiac.app.articleHist.ArticleHistRepository;
import com.gemiso.zodiac.app.articleHist.dto.ArticleHistSimpleDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaSimpleDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaSimpleMapper;
import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderSimpleDTO;
import com.gemiso.zodiac.app.articleOrder.mapper.ArticleOrderSimpleMapper;
import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.issue.dto.IssueDTO;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.app.user.*;
import com.gemiso.zodiac.app.userGroupAuth.UserGroupAuth;
import com.gemiso.zodiac.app.userGroupAuth.UserGroupAuthRepository;
import com.gemiso.zodiac.app.userGroupUser.UserGroupUser;
import com.gemiso.zodiac.app.userGroupUser.UserGroupUserRepository;
import com.gemiso.zodiac.core.enumeration.*;
import com.gemiso.zodiac.core.helper.PageHelper;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.core.service.ProcessArticleFix;
import com.gemiso.zodiac.core.service.UserAuthChkService;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.PasswordFailedException;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.gemiso.zodiac.exception.UserFailException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleService {


    @Value("${files.url-key}")
    private String fileUrl;

    private final ArticleRepository articleRepository;
    private final ArticleCapRepository articleCapRepository;
    private final ArticleHistRepository articleHistRepository;
    private final CueSheetItemRepository cueSheetItemRepository;
    private final UserGroupUserRepository userGroupUserRepository;
    private final UserGroupAuthRepository userGroupAuthRepository;
    private final AnchorCapRepository anchorCapRepository;
    private final ArticleCapHistRepository articleCapHistRepository;
    private final AnchorCapHistRepository anchorCapHistRepository;
    private final ArticleActionLogRepository articleActionLogRepository;

    private final ArticleMapper articleMapper;
    private final ArticleCreateMapper articleCreateMapper;
    private final ArticleCapSimpleMapper articleCapSimpleMapper;
    private final ArticleMediaSimpleMapper articleMediaSimpleMapper;
    private final ArticleOrderSimpleMapper articleOrderSimpleMapper;
    private final ArticleUpdateMapper articleUpdateMapper;
    private final ArticleLockMapper articleLockMapper;
    private final AnchorCapSimpleMapper anchorCapSimpleMapper;
    private final ArticleCapCreateMapper articleCapCreateMapper;
    private final AnchorCapCreateMapper anchorCapCreateMapper;
    private final ArticleCapHistCreateMapper articleCapHistCreateMapper;
    private final AnchorCapHistCreateMapper anchorCapHistCreateMapper;

    private final UserAuthService userAuthService;
    private final UserAuthChkService userAuthChkService;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    //기사 목록조회
    public PageResultDTO<ArticleDTO, Article> findAll(Date sdate, Date edate, Date rcvDt, String rptrId, String inputrId, String brdcPgmId,
                                                      String artclDivCd, String artclTypCd, String searchDivCd, String searchWord,
                                                      Integer page, Integer limit, String apprvDivCd, String deptCd, String artclCateCd, String artclTypDtlCd) {

        /*page = Optional.ofNullable(page).orElse(0);
        limit = Optional.ofNullable(limit).orElse(50);

        Pageable pageable = PageRequest.of(page, limit, Sort.by("artclId","inputDtm").descending());

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, rcvDt, rptrId, brdcPgmId, artclDivCd, artclTypCd, searchDivCd, searchWord);

        List<Article> confirmList = (List<Article>) articleRepository.findAll(booleanBuilder,pageable);

        List<ArticleDTO> articleDTOList = articleMapper.toDtoList(confirmList);

        return articleDTOList;*/

        PageHelper pageHelper = new PageHelper(page, limit);
        /*page = Optional.ofNullable(page).orElse(0);
        limit = Optional.ofNullable(limit).orElse(50);*/

        //Pageable pageable = PageRequest.of(page, limit, Sort.by("artclId","inputDtm").descending());
        Pageable pageable = pageHelper.getArticlePageInfo();

        //검색조건생성 [where생성]
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, rcvDt, rptrId, inputrId, brdcPgmId, artclDivCd, artclTypCd,
                searchDivCd, searchWord, apprvDivCd, deptCd, artclCateCd, artclTypDtlCd);

        //전체조회[page type]
        Page<Article> result = articleRepository.findAll(booleanBuilder, pageable);

        Function<Article, ArticleDTO> fn = (entity -> articleMapper.toDto(entity));

        return new PageResultDTO<ArticleDTO, Article>(result, fn);
    }

    //기사 목록조회[이슈 기사]
    public PageResultDTO<ArticleDTO, Article> findAllIsuue(Date sdate, Date edate, String issuKwd, String artclDivCd, String artclTypCd, String artclTypDtlCd,
                                                           String artclCateCd, String deptCd, String inputrId,
                                                           String brdcPgmId, Long orgArtclId, String delYn,
                                                           String searchword, Integer page, Integer limit, String apprvDivCd) {

        //페이지 셋팅 page, limit null일시 page = 1 limit = 50 디폴트 셋팅
        PageHelper pageHelper = new PageHelper(page, limit);
        Pageable pageable = pageHelper.getArticlePageInfo();

        BooleanBuilder booleanBuilder = getSearchIssue(sdate, edate, issuKwd, artclDivCd, artclTypCd,
                artclTypDtlCd, artclCateCd, deptCd, inputrId, brdcPgmId, orgArtclId, delYn, searchword, apprvDivCd);

        //전체조회[page type]
        Page<Article> result = articleRepository.findAll(booleanBuilder, pageable);

        Function<Article, ArticleDTO> fn = (entity -> articleMapper.toDto(entity));

        return new PageResultDTO<ArticleDTO, Article>(result, fn);

    }

    // 큐시트에서 기사 목록 조회
    public PageResultDTO<ArticleDTO, Article> findCue(Date sdate, Date edate, String searchWord, Long cueId, Integer page, Integer limit) {

        //페이지 셋팅
        PageHelper pageHelper = new PageHelper(page, limit);
        Pageable pageable = pageHelper.getArticlePageInfo();

        BooleanBuilder booleanBuilder = getSearchCue(sdate, edate, searchWord);

        //전체조회[page type]
        Page<Article> result = articleRepository.findAll(booleanBuilder, pageable);

        Function<Article, ArticleDTO> fn = (entity -> articleMapper.toDto(entity));

        return new PageResultDTO<ArticleDTO, Article>(result, fn);
    }

    //큐시트 기사 목록조회시 큐시트에 포함되어 있는 기사 제거.
    public PageResultDTO<ArticleDTO, Article> confirmArticleList(PageResultDTO<ArticleDTO, Article> pageList, Long cueId) {

        //현재 큐시트에 포함된 큐시트아이템을 조회
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId);

        List<ArticleDTO> confirmList = pageList.getDtoList();

        //큐시트 아이템으로 포함된 기사 조회정보에서 삭제.
        if (ObjectUtils.isEmpty(cueSheetItemList) == false) {
            for (CueSheetItem cueSheetItem : cueSheetItemList) {
                if (ObjectUtils.isEmpty(cueSheetItem.getArticle()) == false) { //기사 아이디가 있으면 조회된 기사리트와 검사하여 포함된 기사 삭제
                    Long cueArticleId = cueSheetItem.getArticle().getArtclId(); //큐시트 아이템으로 포함되어 있는 기사아이디 get
                    for (int i = confirmList.size() - 1; i >= 0; i--) {
                        Long artclId = confirmList.get(i).getArtclId();
                        if (cueArticleId.equals(artclId)) {
                            confirmList.remove(i);
                        }
                    }
                }
            }
        }
        pageList.setDtoList(confirmList);

        return pageList;

    }

    //기사 상세정보 조회
    public ArticleDTO find(Long artclId) {

        Article article = articleFindOrFail(artclId);

        ArticleDTO articleDTO = articleMapper.toDto(article);

        //기사정보를 불러와 맵퍼스트럭트 사용시 스텍오버플로우에러[기사에 포함된 리스트에 기사정보포함되어이써 문제 발생] 때문에 따로 DTO변환.
        //List<ArticleHistSimpleDTO> articleHistDTOList = articleHistSimpleMapper.toDtoList(article.getArticleHist());//기사이력정보 DTO변환
        List<ArticleCapSimpleDTO> articleCapDTOList = articleCapSimpleMapper.toDtoList(article.getArticleCap()); //기사자막정보 DTO변환
        List<ArticleMediaSimpleDTO> articleMediaSimpleDTOList = articleMediaSimpleMapper.toDtoList(article.getArticleMedia()); //기사미디어정보 DTO변환
        List<ArticleOrderSimpleDTO> articleOrderSimpleDTOList = articleOrderSimpleMapper.toDtoList(article.getArticleOrder()); //기사의뢰정보 DTO변환
        List<AnchorCapSimpleDTO> anchorCapSimpleDTOList = anchorCapSimpleMapper.toDtoList(article.getAnchorCap());//앵커기사정보 DTO 변환


        //방송아이콘 이미지 Url 추가. 기사자막 방송아이콘 url set
        List<ArticleCapSimpleDTO> setArticleCapDTOList = setUrlArticleCap(articleCapDTOList);
        //방송아이콘 이미지 Url 추가. 앵커자막 방송아이콘 url set
        List<AnchorCapSimpleDTO> setAnchorCapDTOList = setUrlAnchorCap(anchorCapSimpleDTOList);

        //기사이력, 자막, 미디어, 의뢰 정보 set
        //articleDTO.setArticleHistDTO(articleHistDTOList);
        articleDTO.setArticleCap(setArticleCapDTOList);
        articleDTO.setAnchorCap(setAnchorCapDTOList);
        articleDTO.setArticleMedia(articleMediaSimpleDTOList);
        articleDTO.setArticleOrder(articleOrderSimpleDTOList);


        return articleDTO;

    }

    // 기사등록[기사 이력, 자막]
    public Long create(ArticleCreateDTO articleCreateDTO) throws Exception {

        String userId = userAuthService.authUser.getUserId();

        //PD가 기사 작성시 기사픽스상태로 등록된다.
        if (userAuthChkService.authChk(AuthEnum.PD.getAuth())) {
            articleCreateDTO.setApprvDivCd(FixEnum.ARTICLE_FIX.getFixeum(FixEnum.ARTICLE_FIX));
        }

        articleCreateDTO.setInputrId(userId); //등록자 아이디 추가.
        articleCreateDTO.setArtclOrd(0);//기사 순번 등록(0)set
        articleCreateDTO.setApprvDivCd(FixEnum.FIX_NONE.getFixeum(FixEnum.FIX_NONE));//기사픽스 상태 None set[null 상태가면 fix 구분 및 셋팅시 문제.]

        Article article = articleCreateMapper.toEntity(articleCreateDTO);
        articleRepository.save(article);

        articleActionLogCreate(article, userId);//기사 액션 로그 등록

        //기사 이력 create
        Long articleHistId = createArticleHist(article);

        Long articleId = article.getArtclId();//기사 자막등록 및 리턴시켜줄 기사아이디

        //기사자막, 앵커자막 create
        List<ArticleCapCreateDTO> articleCapDTOS = articleCreateDTO.getArticleCap();
        List<AnchorCapCreateDTO> anchorCapDTOS = articleCreateDTO.getAnchorCap();
        createArticleCap(articleCapDTOS, articleId, articleHistId);//기사자막 create
        createAnchorCap(anchorCapDTOS, articleId, articleHistId);//앵커자막 create.


        return articleId;
    }

    //기사 액션로그 등록
    public void articleActionLogCreate(Article article, String userId) throws Exception {

        List<ArticleCap> articleCapList = article.getArticleCap();
        List<AnchorCap> anchorCapList = article.getAnchorCap();
        article.setArticleCap(null);
        article.setAnchorCap(null);

        //기사 액션로그 빌드
        ArticleActionLog articleActionLog = ArticleActionLog.builder()
                .article(article)
                .message(ActionMesg.articleC.getActionMesg(ActionMesg.articleC))
                .action(ActionEnum.CREATE.getAction(ActionEnum.CREATE))
                .inputrId(userId)
                .artclCapInfo(articleCapEntityToJson(articleCapList))
                .anchorCapInfo(anchorCapEntityToJson(anchorCapList))
                .artclInfo(articleEntityToJson(article)) //Json으로 기사내용저장
                .build();
        //기사 액션로그 등록
        articleActionLogRepository.save(articleActionLog);

    }

    //기사 액션로그 엔티티 Json변환
    public String articleEntityToJson(Article article) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String jsonInString = mapper.writeValueAsString(article);

        return jsonInString;
    }

    //기사자막 액션로그 엔티티 Json변환
    public String articleCapEntityToJson(List<ArticleCap> articleCapList) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String jsonInString = mapper.writeValueAsString(articleCapList);


        return jsonInString;
    }

    //앵커자막 액션로그 엔티티 Json변환
    public String anchorCapEntityToJson(List<AnchorCap> anchorCapList) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String jsonInString = mapper.writeValueAsString(anchorCapList);


        return jsonInString;
    }

    //기사 수정
    public void update(ArticleUpdateDTO articleUpdateDTO, Long artclId) throws Exception {

        Article article = articleFindOrFail(artclId);

        IssueDTO issueDTO = articleUpdateDTO.getIssue(); //이슈 업데이트 경우 이슈는 PK값으로 되어있기 때문에 삭제후 등록.
        if (ObjectUtils.isEmpty(issueDTO)) {
            article.setIssue(null);
        }

        String userId = userAuthService.authUser.getUserId();
        articleUpdateDTO.setUpdtrId(userId);

        articleUpdateMapper.updateFromDto(articleUpdateDTO, article);

        articleRepository.save(article);

        //기사 로그 등록.
        articleActionLogUpdate(article, articleUpdateDTO, userId);

        //기사이력 등록.
        Long articleHistId = updateArticleHist(article);

        //기사 자막 Update
        articleCapUpdate(article, articleUpdateDTO, articleHistId);
        anchorCapUpdate(article, articleUpdateDTO, articleHistId);

    }

    //기사 수정 로그
    public void articleActionLogUpdate(Article article, ArticleUpdateDTO articleUpdateDTO, String userId) throws Exception {

        ArticleActionLog articleActionLog = new ArticleActionLog(); //기사액션로그 저장할 엔티티

        String orgTitle = article.getArtclTitl(); //등록되어 있던 기사 제목
        String newTitle = articleUpdateDTO.getArtclTitl(); //신규 기사 제목

        List<ArticleCap> articleCapList = article.getArticleCap();//기사로그에 등록할 기사자막 리스트를 기사에서 가져온다.
        List<AnchorCap> anchorCapList = article.getAnchorCap();//기사로그에 등록할 앵커자막 리스트를 기사에서 가져온다.
        article.setArticleCap(null);//기사에서 기사자막삭제
        article.setAnchorCap(null);//기사에서 앵커자막삭제

        if (Objects.equals(orgTitle, newTitle) == false) { // 기사제목이 바뀌었을 경우 기사액션로그 등록
            //기사로그 빌드
            articleActionLog = ArticleActionLog.builder()
                    .article(article)
                    .message(ActionMesg.articleTM.getActionMesg(ActionMesg.articleTM))
                    .action(ActionEnum.UPDATE.getAction(ActionEnum.UPDATE))
                    .inputrId(userId)
                    .artclCapInfo(articleCapEntityToJson(articleCapList))
                    .anchorCapInfo(anchorCapEntityToJson(anchorCapList))
                    .artclInfo(articleEntityToJson(article)) //Json으로 기사내용저장
                    .build();

            //기사로그 저장
            articleActionLogRepository.save(articleActionLog);
        }

        String orgEnglishTile = article.getArtclTitlEn(); //원본 기사 영어 제목
        String newEnglishTile = articleUpdateDTO.getArtclTitlEn(); // 수정기사 영어제목

        if (Objects.equals(orgEnglishTile, newEnglishTile) == false) { //영어제목이 바뀐경우 기사액션로그 업데이트
            //기사로그 빌드
            articleActionLog = ArticleActionLog.builder()
                    .article(article)
                    .message(ActionMesg.articleTEM.getActionMesg(ActionMesg.articleTEM))
                    .action(ActionEnum.UPDATE.getAction(ActionEnum.UPDATE))
                    .inputrId(userId)
                    .artclCapInfo(articleCapEntityToJson(articleCapList))
                    .anchorCapInfo(anchorCapEntityToJson(anchorCapList))
                    .artclInfo(articleEntityToJson(article)) //Json으로 기사내용저장
                    .build();

            //기사로그 저장
            articleActionLogRepository.save(articleActionLog);
        }

        String orgContents = article.getArtclCtt(); //원본 기사 내용
        String newContents = articleUpdateDTO.getArtclCtt(); // 수정기사 내용

        if (Objects.equals(orgContents, newContents) == false) { //기사 내용이 바뀐경우 기사액션로그 업데이트
            //기사로그 빌드
            articleActionLog = ArticleActionLog.builder()
                    .article(article)
                    .message(ActionMesg.articleCM.getActionMesg(ActionMesg.articleCM))
                    .action(ActionEnum.UPDATE.getAction(ActionEnum.UPDATE))
                    .inputrId(userId)
                    .artclCapInfo(articleCapEntityToJson(articleCapList))
                    .anchorCapInfo(anchorCapEntityToJson(anchorCapList))
                    .artclInfo(articleEntityToJson(article)) //Json으로 기사내용저장
                    .build();

            //기사로그 저장
            articleActionLogRepository.save(articleActionLog);
        }

        String orgAnchorMent = article.getAncMentCtt(); //원본기사 앵커 맨트 
        String newAnchorMent = articleUpdateDTO.getAncMentCtt(); //수정기사 앵커 맨트

        if (Objects.equals(orgAnchorMent, newAnchorMent) == false) { // 앵커맨트 내용이 바뀐경우 기사액션로그 업데이트
            //기사로그 빌드
            articleActionLog = ArticleActionLog.builder()
                    .article(article)
                    .message(ActionMesg.anchorMM.getActionMesg(ActionMesg.anchorMM))
                    .action(ActionEnum.UPDATE.getAction(ActionEnum.UPDATE))
                    .inputrId(userId)
                    .artclCapInfo(articleCapEntityToJson(articleCapList))
                    .anchorCapInfo(anchorCapEntityToJson(anchorCapList))
                    .artclInfo(articleEntityToJson(article)) //Json으로 기사내용저장
                    .build();

            //기사로그 저장
            articleActionLogRepository.save(articleActionLog);
        }
        //기사제목, 영어제목, 기사내용, 앵커맨트 가 수정된게 아니면 일반 업데이트 로그 등록.
        if (Objects.equals(orgAnchorMent, newAnchorMent) && Objects.equals(orgContents, newContents)
                && Objects.equals(orgEnglishTile, newEnglishTile) && Objects.equals(orgTitle, newTitle)) {
            //기사로그 빌드
            articleActionLog = ArticleActionLog.builder()
                    .article(article)
                    .message(ActionMesg.articleU.getActionMesg(ActionMesg.articleU))
                    .action(ActionEnum.UPDATE.getAction(ActionEnum.UPDATE))
                    .inputrId(userId)
                    .artclCapInfo(articleCapEntityToJson(articleCapList))
                    .anchorCapInfo(anchorCapEntityToJson(anchorCapList))
                    .artclInfo(articleEntityToJson(article)) //Json으로 기사내용저장
                    .build();

            //기사로그 저장
            articleActionLogRepository.save(articleActionLog);
        }

    }

    //기사 삭제
    public void delete(Long artclId) throws Exception {

        Article article = articleFindOrFail(artclId);

        //큐시트아이템에 포함되어있고 삭제가 되어있지 않은 기사면 삭제를 못한다.
        Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findArticleCue(artclId);
        if (cueSheetItem.isPresent()) {
            throw new ResourceNotFoundException("큐시트에 포함된 기사 입니다. 기사 아이디 : " + artclId);
        }

        ArticleDTO articleDTO = articleMapper.toDto(article);

        //삭제정보 등록
        articleDTO.setDelDtm(new Date());
        String userId = userAuthService.authUser.getUserId();
        articleDTO.setDelrId(userId);
        articleDTO.setDelYn("Y");

        articleMapper.updateFromDto(articleDTO, article);

        articleRepository.save(article);

        //기사 액션 로그 등록
        articleActionLogDelete(article, userId);
    }

    //큐시트 아이템 삭제시, 포함된 기사(복사본) 삭제
    public void deleteCueItem(Long artclId) throws Exception {

        Article article = articleFindOrFail(artclId);

        //큐시트아이템에 포함되어있고 삭제가 되어있지 않은 기사면 삭제를 못한다.
       /* Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findArticleCue(artclId);
        if (cueSheetItem.isPresent()){
            throw new ResourceNotFoundException("큐시트에 포함된 기사 입니다. 기사 아이디 : " + artclId);
        }*/

        ArticleDTO articleDTO = articleMapper.toDto(article);

        //삭제정보 등록
        articleDTO.setDelDtm(new Date());
        String userId = userAuthService.authUser.getUserId();
        articleDTO.setDelrId(userId);
        articleDTO.setDelYn("Y");

        articleMapper.updateFromDto(articleDTO, article);

        articleRepository.save(article);

        //기사 액션 로그 등록
        articleActionLogDelete(article, userId);
    }

    // 기사 삭제 액션로그 등록
    public void articleActionLogDelete(Article article, String userId) throws Exception {

        List<ArticleCap> articleCapList = article.getArticleCap();//기사로그에 등록할 기사자막 리스트를 기사에서 가져온다.
        List<AnchorCap> anchorCapList = article.getAnchorCap();//기사로그에 등록할 앵커자막 리스트를 기사에서 가져온다.
        article.setArticleCap(null);//기사에서 기사자막삭제
        article.setAnchorCap(null);//기사에서 앵커자막삭제

        //기사로그 빌드
        ArticleActionLog articleActionLog = ArticleActionLog.builder()
                .article(article)
                .message(ActionMesg.articleD.getActionMesg(ActionMesg.articleD))
                .action(ActionEnum.DELETE.getAction(ActionEnum.DELETE))
                .inputrId(userId)
                .artclCapInfo(articleCapEntityToJson(articleCapList))
                .anchorCapInfo(anchorCapEntityToJson(anchorCapList))
                .artclInfo(articleEntityToJson(article)) //Json으로 기사내용저장
                .build();

        //기사로그 저장
        articleActionLogRepository.save(articleActionLog);

    }

    //앵커자막 방송아이콘 url set
    public List<AnchorCapSimpleDTO> setUrlAnchorCap(List<AnchorCapSimpleDTO> anchorCapSimpleDTOList) {

        //방송아이콘 이미지 Url 추가.
        List<AnchorCapSimpleDTO> setArticleCapDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(anchorCapSimpleDTOList) == false) {
            for (AnchorCapSimpleDTO anchorCapSimpleDTO : anchorCapSimpleDTOList) {

                SymbolDTO symbolDTO = new SymbolDTO();

                symbolDTO = anchorCapSimpleDTO.getSymbol();

                if (ObjectUtils.isEmpty(symbolDTO) == false) {
                    String fileLoc = anchorCapSimpleDTO.getSymbol().getAttachFile().getFileLoc();
                    String url = fileUrl + fileLoc; //url + 파일로그

                    symbolDTO.setUrl(url);

                    anchorCapSimpleDTO.setSymbol(symbolDTO);//방송아이콘에 url set
                    setArticleCapDTOList.add(anchorCapSimpleDTO); //앵커자막에 url추가된 방송아이콘 set
                }
            }
        }

        return setArticleCapDTOList;
    }

    //기사자막 방송아이콘 url set
    public List<ArticleCapSimpleDTO> setUrlArticleCap(List<ArticleCapSimpleDTO> articleCapDTOList) { //기사자막 방송아이콘 url set

        //방송아이콘 이미지 Url 추가.
        List<ArticleCapSimpleDTO> setArticleCapDTOList = new ArrayList<>();

        if (ObjectUtils.isEmpty(articleCapDTOList) == false) {
            for (ArticleCapSimpleDTO articleCapSimpleDTO : articleCapDTOList) {

                SymbolDTO symbolDTO = new SymbolDTO();

                symbolDTO = articleCapSimpleDTO.getSymbol();

                if (ObjectUtils.isEmpty(symbolDTO) == false) {
                    String fileLoc = articleCapSimpleDTO.getSymbol().getAttachFile().getFileLoc();
                    String url = fileUrl + fileLoc; //url + 파일로그

                    symbolDTO.setUrl(url);

                    articleCapSimpleDTO.setSymbol(symbolDTO);//방송아이콘에 url set
                    setArticleCapDTOList.add(articleCapSimpleDTO); //기사자막에 url추가된 방송아이콘 set
                }
            }
        }

        return setArticleCapDTOList;
    }

    //앵커자막 등록
    public void createAnchorCap(List<AnchorCapCreateDTO> anchorCapDTOS, Long articleId, Long articleHistId) { //앵커자막 create.

        if (ObjectUtils.isEmpty(anchorCapDTOS) == false) {
            for (AnchorCapCreateDTO anchorCapCreateDTO : anchorCapDTOS) { //앵커자막 리스트 단건씩 저장.

                AnchorCap anchorCap = anchorCapCreateMapper.toEntity(anchorCapCreateDTO);

                Long getCapTemplateId = anchorCapCreateDTO.getCapTemplateId();//앵커자막에 추가할 템플릿아이디.
                if (ObjectUtils.isEmpty(getCapTemplateId) == false) { //등록할 템플릿이 있을경우.

                    CapTemplate capTemplate = CapTemplate.builder().capTmpltId(getCapTemplateId).build();//등록할 템플릿아이디 엔티티 빌드.
                    anchorCap.setCapTemplate(capTemplate);//템플릿아이디 엔티티set.
                }
                String getSymbolId = anchorCapCreateDTO.getSymbolId();//앵커자막에 추가할 방송아이콘 아이디.
                if (StringUtils.isEmpty(getSymbolId) == false) {

                    Symbol symbol = Symbol.builder().symbolId(getSymbolId).build();//등록할 방송아이콘 아이디 엔티티 빌드.
                    anchorCap.setSymbol(symbol);// 방송아이콘아이디 엔티티set.
                }
                Article article = Article.builder().artclId(articleId).build();//자막에 들어갈 기사 아이디.
                anchorCap.setArticle(article);//기사아이디 엔티티set.

                anchorCapRepository.save(anchorCap);//저장.

                createAnchorCapHist(anchorCap, articleHistId); //앵커자막 이력 등록
            }
        }

    }

    //기사자막 등록
    public void createArticleCap(List<ArticleCapCreateDTO> articleCapDTOS, Long articleId, Long articleHistId) {//기사자막 create.

        if (ObjectUtils.isEmpty(articleCapDTOS) == false) {
            for (ArticleCapCreateDTO articleCapDTO : articleCapDTOS) { //기사자막 리스트 단건씩 저장.

                ArticleCap articleCap = articleCapCreateMapper.toEntity(articleCapDTO); //자막 엔티티 빌드 맵퍼.

                Long getCapTemplateId = articleCapDTO.getCapTmpltId();//기사자막에 추가할 템플릿아이디.
                if (ObjectUtils.isEmpty(getCapTemplateId) == false) {

                    CapTemplate capTemplate = CapTemplate.builder().capTmpltId(getCapTemplateId).build();//등록할 템플릿아이디 엔티티 빌드.
                    articleCap.setCapTemplate(capTemplate); //템플릿아이디 엔티티set.
                }
                String getSymbolId = articleCapDTO.getSymbolId();//기사자막에 추가할 방송아이콘 아이디.
                if (StringUtils.isEmpty(getSymbolId) == false) {

                    Symbol symbol = Symbol.builder().symbolId(getSymbolId).build();//등록할 방송아이콘 아이디 엔티티 빌드.
                    articleCap.setSymbol(symbol); // 방송아이콘아이디 엔티티set.
                }

                Article article = Article.builder().artclId(articleId).build();//자막에 들어갈 기사 아이디
                articleCap.setArticle(article); //기사아이디 엔티티set.

                articleCapRepository.save(articleCap); //저장.

                createArticleCapHist(articleCap, articleHistId); //기사자막 이력 저장.
            }
        }
    }

    //기사자막 이력 등록
    public void createArticleCapHist(ArticleCap articleCap, Long articleHistId) {

        //기사자막이력에 등록할 기사이력 아이디 빌드
        ArticleHistSimpleDTO articleHistSimpleDTO = ArticleHistSimpleDTO.builder().artclHistId(articleHistId).build();

        Long capTmpltId = null;
        CapTemplate capTemplate = articleCap.getCapTemplate();
        if (ObjectUtils.isEmpty(capTemplate) == false) {
            capTmpltId = capTemplate.getCapTmpltId();
        }
        String symbolId = "";
        Symbol symbol = articleCap.getSymbol();
        if (ObjectUtils.isEmpty(symbol) == false) {
            symbolId = symbol.getSymbolId();
        }

        //기사자막 이력 등록DTO 빌드
        ArticleCapHistCreateDTO articleCapHistCreateDTO = ArticleCapHistCreateDTO.builder()
                .articleHist(articleHistSimpleDTO)
                .lnNo(articleCap.getLnNo())
                .capTmpltId(capTmpltId)
                .capCtt(articleCap.getCapCtt())
                .capRmk(articleCap.getCapRmk())
                .symbolId(symbolId)
                .capDivCd(articleCap.getCapDivCd())
                .build();

        //디비에 등록하기위해 엔티티 변환
        ArticleCapHist articleCapHist = articleCapHistCreateMapper.toEntity(articleCapHistCreateDTO);

        articleCapHistRepository.save(articleCapHist);
    }

    //앵커자막 이력 등록
    public void createAnchorCapHist(AnchorCap anchorCap, Long articleHistId) {

        //기사자막이력에 등록할 기사이력 아이디 빌드
        ArticleHistSimpleDTO articleHistSimpleDTO = ArticleHistSimpleDTO.builder().artclHistId(articleHistId).build();

        Long capTmpltId = null;
        CapTemplate capTemplate = anchorCap.getCapTemplate();
        if (ObjectUtils.isEmpty(capTemplate) == false) {
            capTmpltId = capTemplate.getCapTmpltId();
        }
        String symbolId = "";
        Symbol symbol = anchorCap.getSymbol();
        if (ObjectUtils.isEmpty(symbol) == false) {
            symbolId = symbol.getSymbolId();
        }

        //앵커자막 이력 등록DTO 빌드
        AnchorCapHistCreateDTO anchorCapHistCreateDTO = AnchorCapHistCreateDTO.builder()
                .articleHist(articleHistSimpleDTO)
                .lnNo(anchorCap.getLnNo())
                .capTmpltId(capTmpltId)
                .capCtt(anchorCap.getCapCtt())
                .capRmk(anchorCap.getCapRmk())
                .symbolId(symbolId)
                .capDivCd(anchorCap.getCapDivCd())
                .build();
        //디비에 등록하기위해 엔티티 변환
        AnchorCapHist anchorCapHist = anchorCapHistCreateMapper.toEntity(anchorCapHistCreateDTO);

        anchorCapHistRepository.save(anchorCapHist); //등록
    }

    //기사 오더락 체크[다른 사용자가 기사 수정중일 경우 수정 불가.]
    public Boolean chkOrderLock(Long artclId) {

        Article article = articleFindOrFail(artclId); //해당 기사 조회.

        String lckYn = article.getLckYn(); //기사잠금 여부가 Y일 경우 return true[다른 사용자가 기사 잠금 중.]
        String lckrId = article.getLckrId();
        String userId = userAuthService.authUser.getUserId();

        if (lckYn.equals("Y") && userId.equals(lckrId) == false) {
            return true;
        }

        return false;

    }

    //기사자막 수정.
    private void articleCapUpdate(Article article, ArticleUpdateDTO articleUpdateDTO, Long articleHistId) {

        Long articleId = article.getArtclId();//수정할 기사자막 등록시 등록할 기사 아이디
        List<ArticleCap> articleCapList = articleCapRepository.findArticleCap(articleId);

        for (ArticleCap articleCap : articleCapList) {
            Long artclCapId = articleCap.getArtclCapId();
            articleCapRepository.deleteById(artclCapId);
        }

        /*        List<ArticleCap> articleCapList = article.getArticleCap(); //원본기사 자막 get
        if (ObjectUtils.isEmpty(articleCapList) == false) { //기존에 등록되어 있던 기사자막 삭제
            for (ArticleCap articleCap : articleCapList) {
                Long artclCapId = articleCap.getArtclCapId();
                articleCapRepository.deleteById(artclCapId);
            }
        }*/
        List<ArticleCapCreateDTO> capSimpleDTOList = articleUpdateDTO.getArticleCap(); //update로 들어온 기사 등록
        createArticleCap(capSimpleDTOList, articleId, articleHistId); //기사자막 등록


    }

    //앵커자막 수정.
    private void anchorCapUpdate(Article article, ArticleUpdateDTO articleUpdateDTO, Long articleHistId) {

        Long articleId = article.getArtclId();//수정할 기사자막 등록시 등록할 기사 아이디
        List<AnchorCap> anchorCaps = anchorCapRepository.findAnchorCapList(articleId);

        for (AnchorCap anchorCap : anchorCaps) {
            Long anchorCapId = anchorCap.getAnchorCapId();
            anchorCapRepository.deleteById(anchorCapId);
        }

        List<AnchorCapCreateDTO> anchorCapCreateDTOList = articleUpdateDTO.getAnchorCap();
        createAnchorCap(anchorCapCreateDTOList, articleId, articleHistId);//앵커자막 등록

    }

    // 기사 잠금
    public void articleLock(Long artclId, ArticleLockDTO articleLockDTO) {

        Article article = articleFindOrFail(artclId);

        String lockYn = article.getLckYn();//잠금여부값 get
        String userId = userAuthService.authUser.getUserId();

        if (lockYn.equals("Y")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN); // 잠금 사용자가 다르다.
        }

        if (articleLockDTO.getLckYn().equals("Y")) {
            articleLockDTO.setLckDtm(new Date());
            // 토큰 인증된 사용자 아이디를 입력자로 등록
            articleLockDTO.setLckrId(userId);
        } else {
            article.setLckDtm(null);
            article.setLckrId(null);
        }

        articleLockMapper.updateFromDto(articleLockDTO, article);

        articleRepository.save(article);


    }

    //기사 잠금 해제
    public void articleUnlock(Long artclId, ArticleLockDTO articleLockDTO) {

        Article article = articleFindOrFail(artclId); //기사아이디로 기사정보조회 및 기사유무 확인.

        if (articleLockDTO.getLckYn().equals("N")) {
            article.setLckDtm(null);
            article.setLckrId(null);
        }

        articleLockMapper.updateFromDto(articleLockDTO, article);
        articleRepository.save(article);

    }

    //기사 정보조회 존재여부 확인
    public Article articleFindOrFail(Long artclId) {

        Optional<Article> article = articleRepository.findArticle(artclId);

        if (!article.isPresent()) {
            throw new ResourceNotFoundException("기사 아이디가 없습니다. article Id  : " + artclId);
        }

        return article.get();

    }

    //기사 목록조회시 조건 빌드[일반 목록조회 ]
    public BooleanBuilder getSearch(Date sdate, Date edate, Date rcvDt, String rptrId, String inputrId, String brdcPgmId,
                                    String artclDivCd, String artclTypCd, String searchDivCd, String searchWord,
                                    String apprvDivCd, String deptCd, String artclCateCd, String artclTypDtlCd) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticle qArticle = QArticle.article;
        QUser qUser = QUser.user;

        //삭제가 되지 않은 기사 조회
        booleanBuilder.and(qArticle.delYn.eq("N"));

        //등록날짜 기준으로 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qArticle.inputDtm.between(sdate, edate)
                    .or(qArticle.embgDtm.between(sdate, edate)
                            .or(qArticle.brdcSchdDtm.between(sdate, edate))));
        }
        //수신 일자 기준으로 조회
        if (ObjectUtils.isEmpty(rcvDt) == false) {

            //rcvDt(수신일자)검색을 위해 +1 days
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rcvDt);
            calendar.add(Calendar.DATE, 1);
            Date rcvDtTomerrow = calendar.getTime();

            booleanBuilder.and(qArticle.inputDtm.between(rcvDt, rcvDtTomerrow));
        }
        //기자 아이디로 조회
        if (rptrId != null && rptrId.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.rptrId.eq(rptrId));
        }
        //등록자 아이디로 조회
        if (inputrId != null && inputrId.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.inputrId.eq(inputrId));
        }
        //방송 프로그램 아이디로 조회
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.brdcPgmId.eq(brdcPgmId));
        }
        //기사 구분 코드로 조회
        if (artclDivCd != null && artclDivCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.artclDivCd.eq(artclDivCd));
        }
        //기사 타입 코드로 조회
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.artclTypCd.eq(artclTypCd));
        }
        //검색어로 조회
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            //검색구분코드 01 일때 기사 제목으로 검색
            if (searchDivCd.equals("01")) {
                booleanBuilder.and(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
            }
            //검색구분코드 02 일때 기자이름으로 검색
            if (searchDivCd.equals("02")) {
                booleanBuilder.and(qArticle.rptrId.eq(String.valueOf(qUser.userNm.contains(searchWord))));
            }

        }
        //픽스구분코드
        if (apprvDivCd != null && apprvDivCd.trim().isEmpty() == false){
            booleanBuilder.and(qArticle.apprvDivCd.eq(apprvDivCd));
        }
        //부서코드
        if (deptCd != null && deptCd.trim().isEmpty() == false){
            booleanBuilder.and(qArticle.deptCd.eq(deptCd));
        }
        //기사카테고리코드
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false){
            booleanBuilder.and(qArticle.artclCateCd.eq(artclCateCd));
        }
        //기사유형상세코드
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false){
            booleanBuilder.and(qArticle.artclCateCd.eq(artclTypDtlCd));
        }

        /*//기사 타입 코드로 조회
        if (!StringUtils.isEmpty(issuId)) {
            booleanBuilder.and(qArticle.issue.issuId.eq(issuId));
        }*/


        return booleanBuilder;
    }

    //기사 목록조회 조회조건 빌드[이슈 기사]
    public BooleanBuilder getSearchIssue(Date sdate, Date edate, String issuKwd, String artclDivCd, String artclTypCd,
                                         String artclTypDtlCd, String artclCateCd, String deptCd, String inputrId,
                                         String brdcPgmId, Long orgArtclId, String delYn, String searchword, String apprvDivCd) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticle qArticle = QArticle.article;

        //이슈 검색일 조건
        if (ObjectUtils.isEmpty(sdate) && ObjectUtils.isEmpty(edate)) {
            booleanBuilder.and(qArticle.issue.inputDtm.between(sdate, edate));
        }
        //이슈 키워드 검색
        if (issuKwd != null && issuKwd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.issue.issuKwd.eq(issuKwd));
        }
        //기사 구분코드[이슈기사 검색이기때문에 무조건 article_issue 들어와야함함]
        if (artclDivCd != null && artclDivCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.artclDivCd.eq(artclDivCd));
        }
        //검색조건 = 기사 유형 코드
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.artclTypCd.eq(artclTypCd));
        }
        //검색조건 = 기상 유형 상세 코드
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.artclTypDtlCd.eq(artclTypDtlCd));
        }
        //검색조건 = 기사 카테고리 코드
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.artclCateCd.eq(artclCateCd));
        }
        //검색조건 = 부서 코드
        if (deptCd != null && deptCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.deptCd.eq(deptCd));
        }
        //검색조건 = 부서 코드
        if (inputrId != null && inputrId.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.inputrId.eq(inputrId));
        }
        //검색조건 = 방송 프로그램 아이디
        if (ObjectUtils.isEmpty(brdcPgmId) == false) {
            booleanBuilder.and(qArticle.brdcPgmId.eq(brdcPgmId));
        }
        //검색조건 = 원본 기사 아이디
        if (ObjectUtils.isEmpty(orgArtclId) == false) {
            booleanBuilder.and(qArticle.orgArtclId.eq(orgArtclId));
        }
        //검색조건 = 삭제 여부
        if (delYn != null && delYn.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.delYn.eq(delYn));
        } else {
            booleanBuilder.and(qArticle.delYn.eq("N")); //삭제여부값 안들어 올시 디폴트 'N'
        }
        //검색조건 = 검색어[이슈 제목]
        if (searchword != null && searchword.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.issue.issuKwd.contains(searchword));
        }

        //픽스구분코드
        if (apprvDivCd != null && apprvDivCd.trim().isEmpty() == false){
            booleanBuilder.and(qArticle.apprvDivCd.eq(apprvDivCd));
        }

        return booleanBuilder;
    }

    //큐시트 기사자막 조회 조건
    public BooleanBuilder getSearchCue(Date sdate, Date edate, String searchWord) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticle qArticle = QArticle.article;

        booleanBuilder.and(qArticle.delYn.eq("N"));

        //등록날짜 기준으로 조회
        if (!StringUtils.isEmpty(sdate) && !StringUtils.isEmpty(edate)) {
            booleanBuilder.and(qArticle.inputDtm.between(sdate, edate));
        }

        if (!StringUtils.isEmpty(searchWord)) {
            booleanBuilder.and(qArticle.artclTitl.contains(searchWord));
        }

        return booleanBuilder;
    }

    //기사등록 이력 등록
    public Long createArticleHist(Article article) {

        ArticleHist articleHist = ArticleHist.builder()
                .article(article)
                .chDivCd(article.getChDivCd())
                .artclTitl(article.getArtclTitl())
                .artclTitlEn(article.getArtclTitlEn())
                .artclCtt(article.getArtclCtt())
                .ancMentCtt(article.getAncMentCtt())
                .artclOrd(article.getArtclOrd())
                .orgArtclId(article.getOrgArtclId())
                .inputDtm(new Date())
                .ver(0) //수정! 버전 설정?
                .build();

        articleHistRepository.save(articleHist);

        return articleHist.getArtclHistId();
    }

    //기사수정 이력 등록
    public Long updateArticleHist(Article article) {

        ArticleHist articleHist = ArticleHist.builder()
                .article(article)
                .chDivCd(article.getChDivCd())
                .artclTitl(article.getArtclTitl())
                .artclTitlEn(article.getArtclTitlEn())
                .artclCtt(article.getArtclCtt())
                .ancMentCtt(article.getAncMentCtt())
                .artclOrd(article.getArtclOrd())
                .orgArtclId(article.getOrgArtclId())
                .inputDtm(new Date())
                .ver(0) //수정! 버전 설정?
                .build();

        articleHistRepository.save(articleHist);

        return articleHist.getArtclHistId();
    }

    //기사 픽스
    public void vaildFixStaus(Long artclId, String apprvDivCd) throws Exception {

        //기사 조회.
        Article article = articleFindOrFail(artclId);

        String orgApprvDivcd = article.getApprvDivCd(); //조회된 기사픽스 구분코드 get
        // 조회된 기사픽스 구분코드와 새로들어온 구분코드가 같으면 return
        //에러를 내지않고 유지하기 위해.
        if (apprvDivCd.equals(orgApprvDivcd)) {
            return;
        }

        // 사용자 정보
        String userId = userAuthService.authUser.getUserId();

        //현재접속자 그룹정보를 그룹이넘 리스트로 불러온다.
        List<String> fixAuthList = getAppAuth(userId);

        //ProcessArticleFix에 fix구분할 date값 set
        ProcessArticleFix paf = new ProcessArticleFix();
        paf.setArticle(article);
        paf.setApproveCode(apprvDivCd);
        //유저 Id
        if (paf.getFixStatus(userId, fixAuthList) == false) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        ArticleDTO articleDTO = articleMapper.toDto(article);

        if ("editorfix".equals(apprvDivCd)) {//에디터 픽스일 경우 픽스한 에디터 set
            articleDTO.setEditorId(userId);
        }
        articleDTO.setApprvDivCd(apprvDivCd); //픽스 구분코드 변경.
        articleDTO.setApprvrId(userId);//픽스 승인자 변경.
        articleDTO.setApprvDtm(new Date());

        articleMapper.updateFromDto(articleDTO, article);
        articleRepository.save(article);

        //기사로그 등록.
        articleActionLogfix(apprvDivCd, orgApprvDivcd, userId, article);
    }

    //기사 액션 로그 등록
    public void articleActionLogfix(String apprvDivCd, String orgApprvDivcd, String userId, Article article) throws Exception {

        List<ArticleCap> articleCapList = article.getArticleCap();//기사로그에 등록할 기사자막 리스트를 기사에서 가져온다.
        List<AnchorCap> anchorCapList = article.getAnchorCap();//기사로그에 등록할 앵커자막 리스트를 기사에서 가져온다.
        article.setArticleCap(null);//기사에서 기사자막삭제
        article.setAnchorCap(null);//기사에서 앵커자막삭제

        //기사 액션로그 빌드
        ArticleActionLog articleActionLog = ArticleActionLog.builder()
                .article(article)
                .message(ActionMesg.fixM.getActionMesg(ActionMesg.fixM) + " " + "'" + orgApprvDivcd + "'" + " to " + "'" + apprvDivCd + "'")
                .action(ActionEnum.UPDATE.getAction(ActionEnum.UPDATE))
                .inputrId(userId)
                .artclCapInfo(articleCapEntityToJson(articleCapList))
                .anchorCapInfo(anchorCapEntityToJson(anchorCapList))
                .artclInfo(articleEntityToJson(article)) //Json으로 기사내용저장
                .build();
        //기사 액션로그 등록
        articleActionLogRepository.save(articleActionLog);
    }

    //권한확인
    public List<String> getAppAuth(String userId) {

        // 사용자에 대한 그룹 정보
        List<UserGroupUser> userGroupUserList = userGroupUserRepository.findByUserId(userId);

        List<String> appAuthList = new ArrayList<>();//리턴할 권한 List
        Long[] appAuthArr = new Long[userGroupUserList.size()]; //inquery로 조회할 유저그룹아이디 LongArray

        for (int i = 0; i < userGroupUserList.size(); i++) { //등록된 사용자 그룹에 포함한 권한을 모두 불러온다.
            Long groupId = userGroupUserList.get(i).getUserGroup().getUserGrpId();
            appAuthArr[i] = groupId;
        }
        List<UserGroupAuth> findUserGroupAuthList = userGroupAuthRepository.findByUserGrpIdArr(appAuthArr);

        for (UserGroupAuth userGroupAuth : findUserGroupAuthList) {

            String appAuthCD = userGroupAuth.getAppAuth().getAppAuthCd();
            if (appAuthList.contains(appAuthCD) == false) {
                appAuthList.add(appAuthCD);
            }
        }

        //권한 리스트 리턴
        return appAuthList;
    }

    //사용자 확인
    public void confirmUser(ArticleDeleteConfirmDTO articleDeleteConfirmDTO, Long artclId) {

        String pwd = articleDeleteConfirmDTO.getPassword();//파라미터로 들어온 비밀번호
        String userId = userAuthService.authUser.getUserId(); //토큰에서 유저 아이디를 가져온다.
        User userEntity = userService.userFindOrFail(userId); //사용자 아이디로 사용자 유무 확인 및 사용자 정보조회.

        if (passwordEncoder.matches(pwd, userEntity.getPwd()) == false) { //현재 들어온 비밀번호와 등록되어 있는 비밀번호 확인
            throw new PasswordFailedException("Password failed.");
        }

        /*String pssword = passwordEncoder.*/

        Article article = articleFindOrFail(artclId);//기사조회 및 존재유무 확인.
        String inputr = article.getInputrId();

        if (inputr.equals(userId) == false) { //기사입력자 확인, 본인기사만 삭제 가능.
            if (userAuthChkService.authChk(FixAuth.ADMIN.name())) {
                throw new UserFailException("기사를 입력한 사용자가 아닙니다. 기사 입력자 아이디 : " + inputr);
            }
        }
        if (userAuthChkService.authChk(FixAuth.ADMIN.name())) {//관리자 권한 확인. 본인기사가 아니더라도 관리자면 삭제가능
            if (inputr.equals(userId) == false) {
                throw new UserFailException("관리자 권한 이거나 기사를 입력한 사용자만 기사 삭제가 가능 합니다.");
            }
        }

    }

    //기사 잠금여부 및 권한 확인.
    public ArticleAuthConfirmDTO articleConfirm(Long artclId) {

        Article article = articleFindOrFail(artclId); //기사아이디로 기사정보조회 및 기사유무 확인.

        String orgApprDivCd = article.getApprvDivCd(); //현재 기사의 픽스 값을 get
        String orgLckYn = article.getLckYn(); //잠금 여부값 확인
        String inputr = article.getInputrId(); //기사 작성자 get
        String lckrId = article.getLckrId();// 잠금 사용자 확인

        // 사용자 정보
        String userId = userAuthService.authUser.getUserId();//토큰에서 유저 아이디를 가져온다.

        if ("Y".equals(orgLckYn) && userId.equals(lckrId) == false) { //이미 lckYn 값이 Y이면 잠긴상태이므로 리턴 true로 예외처리로 넘어간다.
            ArticleAuthConfirmDTO articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                    .artclId(article.getArtclId())
                    .lckYn(article.getLckYn())
                    .lckDtm(article.getLckDtm())
                    .lckrId(article.getLckrId())
                    .msg("다른 사용자가 수정중 입니다.")
                    .authCode("Locked article")
                    .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    .build();

            return articleAuthConfirmDTO;
        }


        //현재접속자 그룹정보를 그룹이넘 리스트로 불러온다.
        List<String> fixAuthList = getAppAuth(userId);

        //픽스 권한 리스트로 들어온 파라미터 중 최상위 권한 get
        AuthEnum fixAuth = AuthEnum.certity(fixAuthList);
        //권한이 PD인경우 기사 수정이 불가.
        if (fixAuth.equals(AuthEnum.PD)){

            ArticleAuthConfirmDTO articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                    .artclId(article.getArtclId())
                    .msg("PD 권한은 기사를 수정 할 수 없습니다.")
                    .authCode("cannot_be_modified")
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .build();

            return articleAuthConfirmDTO;//작상자만 기사를 수정가능
        }

        //ProcessArticleFix에 fix구분할 date값 set
        ProcessArticleFix paf = new ProcessArticleFix();
        paf.setArticle(article);


        if (FixEnum.FIX_NONE.getFixeum(FixEnum.FIX_NONE).equals(orgApprDivCd)) { //픽스 권한이 none일경우

            if (inputr.equals(userId) == false) {

                ArticleAuthConfirmDTO articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                        .artclId(article.getArtclId())
                        .msg("기사 입력자와 일치하지 않습니다. 기사 입력자 아이디 : " + inputr)
                        .authCode("not_match_userId")
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .build();

                return articleAuthConfirmDTO;//작상자만 기사를 수정가능
            }

            //fix none 상태일 경우 article_fix할 권한이상이 있는경우만 수정가능.
            paf.setApproveCode(FixEnum.ARTICLE_FIX.getFixeum(FixEnum.ARTICLE_FIX));

            if (paf.getFixStatus(userId, fixAuthList) == false) { //권한 확인후 false이면 에러 403포비든.
                ArticleAuthConfirmDTO articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                        .artclId(article.getArtclId())
                        .msg("기사 수정 권한이 없습니다.")
                        .authCode("incorrect_auth_articlefix")
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .build();

                return articleAuthConfirmDTO;
            }
            return null;
        }

        if (FixEnum.ARTICLE_FIX.getFixeum(FixEnum.ARTICLE_FIX).equals(orgApprDivCd)) { //픽스 권한이 articlefix일 경우
            // article fix 상태일 경우 에디터 픽스 권한이상이 있는경우만 수정가능.
            paf.setApproveCode(FixEnum.EDITOR_FIX.getFixeum(FixEnum.EDITOR_FIX));

            if (paf.getFixStatus(userId, fixAuthList) == false) { //권한 확인후 false이면 에러 403포비든.
                ArticleAuthConfirmDTO articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                        .artclId(article.getArtclId())
                        .msg("기자 픽스 상태입니다. 기사 수정 권한을 확인하십시오.")
                        .authCode("incorrect_auth_editorfix")
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .build();

                return articleAuthConfirmDTO;
            }
            return null;
        }

        if (FixEnum.EDITOR_FIX.getFixeum(FixEnum.EDITOR_FIX).equals(orgApprDivCd)) {//픽스 권한이 editorfix 경우

            // article fix 상태일 경우 할 ANCHOR_FIX 권한이상이 있는경우만 수정가능.
            paf.setApproveCode(FixEnum.ANCHOR_FIX.getFixeum(FixEnum.ANCHOR_FIX));

            if (paf.getFixStatus(userId, fixAuthList) == false) { //권한 확인후 false이면 에러 403포비든.
                ArticleAuthConfirmDTO articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                        .artclId(article.getArtclId())
                        .msg("애디터 픽스 상태입니다. 기사 수정 권한을 확인하십시오.")
                        .authCode("incorrect_auth_anchorfix")
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .build();

                return articleAuthConfirmDTO;
            }
            return null;
        }

        if (FixEnum.ANCHOR_FIX.getFixeum(FixEnum.ANCHOR_FIX).equals(orgApprDivCd)) {//픽스 권한이 anchorfix 경우

            // article fix 상태일 경우 할 DESK_FIX 권한이상이 있는경우만 수정가능.
            paf.setApproveCode(FixEnum.DESK_FIX.getFixeum(FixEnum.DESK_FIX));

            if (paf.getFixStatus(userId, fixAuthList) == false) { //권한 확인후 false이면 에러 403포비든.
                ArticleAuthConfirmDTO articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                        .artclId(article.getArtclId())
                        .msg("앵커 픽스 상태입니다. 기사 수정 권한을 확인하십시오.")
                        .authCode("incorrect_auth_deskfix")
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .build();

                return articleAuthConfirmDTO;
            }
            return null;
        }

        if (FixEnum.DESK_FIX.getFixeum(FixEnum.DESK_FIX).equals(orgApprDivCd)) {//픽스 권한이 deskfix 경우

            if (paf.getFixStatus(userId, fixAuthList) == false) { //권한 확인후 false이면 에러 403포비든.
                ArticleAuthConfirmDTO articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                        .artclId(article.getArtclId())
                        .msg("데스커 픽스 상태에서는 수정할 수 없습니다.")
                        .authCode("cannot_be_modified")
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .build();

                return articleAuthConfirmDTO;
                //throw new ResponseStatusException(HttpStatus.CONFLICT);

            }
            return null;
        }

        return null;
    }

    //기사 수정중 일시 수정중인 사용자 빌드후  리스폰스
    public ArticleAuthConfirmDTO errorArticleAuthConfirm(Article article) {

        ArticleAuthConfirmDTO articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                .artclId(article.getArtclId())
                .lckYn(article.getLckYn())
                .lckDtm(article.getLckDtm())
                .lckrId(article.getLckrId())
                .msg("다른 사용자가 수정중 입니다.")
                .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                .build();

        return articleAuthConfirmDTO;

    }

}


// artcl_di_cd[기사구분코드가] 이슈로 들어왔을 경우 해당이슈로  작성된 기사들 출력.
/*    public PageResultDTO<ArticleDTO, Article> findAllIssue(Long issuId){

        PageHelper pageHelper = new PageHelper(null, null);
        *//*page = Optional.ofNullable(page).orElse(0);
        limit = Optional.ofNullable(limit).orElse(50);*//*

        //Pageable pageable = PageRequest.of(page, limit, Sort.by("artclId","inputDtm").descending());
        Pageable pageable = pageHelper.getArticlePageInfo();

        BooleanBuilder booleanBuilder = getSearch(null, null, null, null, null,
                null, null, null, null, issuId);

        Page<Article> result = articleRepository.findAll(booleanBuilder,pageable);

        Function<Article, ArticleDTO> fn = (entity -> articleMapper.toDto(entity));

        return new PageResultDTO<ArticleDTO, Article>(result, fn);

    }*/

