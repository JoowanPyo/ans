package com.gemiso.zodiac.app.article;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.articleTag.ArticleTag;
import com.gemiso.zodiac.app.articleTag.ArticleTagRepository;
import com.gemiso.zodiac.app.articleTag.dto.ArticleTagDTO;
import com.gemiso.zodiac.app.articleTag.mapper.ArticleTagMapper;
import com.gemiso.zodiac.app.anchorCap.AnchorCap;
import com.gemiso.zodiac.app.anchorCap.AnchorCapRepository;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapCreateDTO;
import com.gemiso.zodiac.app.anchorCap.dto.AnchorCapSimpleDTO;
import com.gemiso.zodiac.app.anchorCap.mapper.AnchorCapCreateMapper;
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
import com.gemiso.zodiac.app.articleCapHist.ArticleCapHist;
import com.gemiso.zodiac.app.articleCapHist.ArticleCapHistRepository;
import com.gemiso.zodiac.app.articleCapHist.dto.ArticleCapHistCreateDTO;
import com.gemiso.zodiac.app.articleCapHist.mapper.ArticleCapHistCreateMapper;
import com.gemiso.zodiac.app.articleHist.ArticleHist;
import com.gemiso.zodiac.app.articleHist.ArticleHistRepository;
import com.gemiso.zodiac.app.articleHist.dto.ArticleHistSimpleDTO;
import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.articleMedia.ArticleMediaRepository;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaCreateDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaSimpleDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaCreateMapper;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaMapper;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaSimpleMapper;
import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetRepository;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.elasticsearch.ElasticSearchArticleRepository;
import com.gemiso.zodiac.app.elasticsearch.ElasticSearchArticleService;
import com.gemiso.zodiac.app.elasticsearch.articleDTO.ElasticSearchArticleDTO;
import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasticSearchArticle;
import com.gemiso.zodiac.app.elasticsearch.mapper.ElasticSearchArticleMapper;
import com.gemiso.zodiac.app.facilityManage.FacilityManageService;
import com.gemiso.zodiac.app.facilityManage.dto.FacilityManageDTO;
import com.gemiso.zodiac.app.issue.Issue;
import com.gemiso.zodiac.app.issue.IssueService;
import com.gemiso.zodiac.app.issue.dto.IssueDTO;
import com.gemiso.zodiac.app.lbox.LboxService;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.app.tag.Tag;
import com.gemiso.zodiac.app.user.QUser;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.UserService;
import com.gemiso.zodiac.app.userGroupAuth.UserGroupAuth;
import com.gemiso.zodiac.app.userGroupAuth.UserGroupAuthRepository;
import com.gemiso.zodiac.app.userGroupUser.UserGroupUser;
import com.gemiso.zodiac.app.userGroupUser.UserGroupUserRepository;
import com.gemiso.zodiac.core.enumeration.ActionEnum;
import com.gemiso.zodiac.core.enumeration.ActionMesg;
import com.gemiso.zodiac.core.enumeration.AuthEnum;
import com.gemiso.zodiac.core.enumeration.FixEnum;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.helper.EncodingHelper;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.helper.PageHelper;
import com.gemiso.zodiac.core.page.PageResultDTO;
import com.gemiso.zodiac.core.service.ProcessArticleFix;
import com.gemiso.zodiac.core.service.UserAuthChkService;
import com.gemiso.zodiac.core.topic.ArticleTopicService;
import com.gemiso.zodiac.core.topic.CueSheetTopicService;
import com.gemiso.zodiac.core.topic.InterfaceTopicService;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleService {


    @Value("${files.url-key}")
    private String fileUrl;

    @Value("${password.salt.key:saltKey}")
    private String saltKey;

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
    private final ArticleMediaRepository articleMediaRepository;
    private final CueSheetRepository cueSheetRepository;
    private final ArticleTagRepository articleTagRepository;
    private final ElasticSearchArticleRepository elasticSearchArticleRepository;
    //private final CodeRepository codeRepository;
    //private final UserRepository userRepository;
    //private final DeptsRepository deptsRepository;

    private final ArticleMapper articleMapper;
    private final ArticleCreateMapper articleCreateMapper;
    private final ArticleUpdateMapper articleUpdateMapper;
    private final ArticleLockMapper articleLockMapper;
    private final ArticleCapCreateMapper articleCapCreateMapper;
    private final AnchorCapCreateMapper anchorCapCreateMapper;
    private final ArticleCapHistCreateMapper articleCapHistCreateMapper;
    private final AnchorCapHistCreateMapper anchorCapHistCreateMapper;
    private final ArticleMediaMapper articleMediaMapper;
    private final ArticleMediaSimpleMapper articleMediaSimpleMapper;
    private final ArticleMediaCreateMapper articleMediaCreateMapper;
    private final ArticleTagMapper articleTagMapper;
    private final ElasticSearchArticleMapper elasticSearchArticleMapper;

    //private final UserAuthService userAuthService;
    private final UserAuthChkService userAuthChkService;
    private final UserService userService;
    private final IssueService issueService;
    private final FacilityManageService facilityManageService;
    private final ElasticSearchArticleService elasticSearchArticleService;
    private final OrgArticleIdCreateService orgArticleIdCreateService;

    private final PasswordEncoder passwordEncoder;

    private final MarshallingJsonHelper marshallingJsonHelper;
    private final DateChangeHelper dateChangeHelper;

    //private final TopicSendService topicSendService;
    private final ArticleTopicService articleTopicService;
    private final CueSheetTopicService cueSheetTopicService;
    private final InterfaceTopicService interfaceTopicService;
    private final LboxService lboxService;

    //기사 목록조회
    public PageResultDTO<ArticleDTO, Article> findAll(Date sdate, Date edate, Date rcvDt, String rptrId, String inputrId, String brdcPgmId,
                                                      String artclDivCd, String artclTypCd, String searchDivCd, String searchWord,
                                                      Integer page, Integer limit, List<String> apprvDivCdList, Long deptCd,
                                                      String artclCateCd, String artclTypDtlCd, String delYn, Long artclId, String copyYn,
                                                      Long orgArtclId) {

        //페이지 셋팅 page, limit null일시 page = 1 limit = 50 디폴트 셋팅
        PageHelper pageHelper = new PageHelper(page, limit);
        Pageable pageable = pageHelper.getArticlePageInfo();

        //전체조회[page type]
        Page<Article> result = articleRepository.findByArticleList(sdate, edate, rcvDt, rptrId, inputrId, brdcPgmId, artclDivCd, artclTypCd,
                searchDivCd, searchWord, apprvDivCdList, deptCd, artclCateCd, artclTypDtlCd, delYn, artclId, copyYn, orgArtclId, pageable);


        Function<Article, ArticleDTO> fn = (entity -> articleMapper.toDto(entity));


        return new PageResultDTO<ArticleDTO, Article>(result, fn);
    }

    //기사 목록조회
    public PageResultDTO<ArticleDTO, Article> findAllElasticPush(Date sdate, Date edate,Integer page, Integer limit) {

        //페이지 셋팅 page, limit null일시 page = 1 limit = 50 디폴트 셋팅
        PageHelper pageHelper = new PageHelper(page, limit);
        Pageable pageable = pageHelper.getArticlePageInfo();

        //전체조회[page type]
        Page<Article> result = articleRepository.findByArticleListElastic(sdate, edate, pageable);


        Function<Article, ArticleDTO> fn = (entity -> articleMapper.toDto(entity));


        return new PageResultDTO<ArticleDTO, Article>(result, fn);
    }

    //기사 목록조회 [엘라스틱서치]
    public PageResultDTO<ElasticSearchArticleDTO, ElasticSearchArticle> findAllElasticsearch(
            Date sdate, Date edate, String rptrId, String inputrId, String brdcPgmId,
            String artclDivCd, String artclTypCd, String searchDivCd, String searchWord,
            Integer page, Integer limit, List<String> apprvDivCdList, Long deptCd,
            String artclCateCd, String artclTypDtlCd, String delYn, Long artclId, String copyYn,
            Long orgArtclId, Long cueId) throws Exception {

        //페이지 셋팅 page, limit null일시 page = 1 limit = 50 디폴트 셋팅
        PageHelper pageHelper = new PageHelper(page, limit);
        Pageable pageable = pageHelper.getArticlePageInfo();

        /*BooleanBuilder booleanBuilder = getSearch( sdate,  edate,  rcvDt,  rptrId,  inputrId,  brdcPgmId,
                 artclDivCd,  artclTypCd,  searchDivCd,  searchWord,
                 apprvDivCdList,  deptCd,  artclCateCd,  artclTypDtlCd,
                 delYn,  artclId,  copyYn,  orgArtclId);*/

        //전체조회[page type]
        Page<ElasticSearchArticle> result = elasticSearchArticleRepository.findByElasticSearchArticleList(sdate, edate, rptrId, inputrId, brdcPgmId,
                artclDivCd, artclTypCd, searchDivCd, searchWord,
                apprvDivCdList, deptCd, artclCateCd, artclTypDtlCd,
                delYn, artclId, copyYn, orgArtclId, cueId, pageable);


        Function<ElasticSearchArticle, ElasticSearchArticleDTO> fn = (entity -> elasticSearchArticleMapper.toDto(entity));


        return new PageResultDTO<ElasticSearchArticleDTO, ElasticSearchArticle>(result, fn);
    }

    //엘라스틱서치 lock데이터 추가
    public PageResultDTO<ElasticSearchArticleDTO, ElasticSearchArticle> lockInfoAdd(PageResultDTO<ElasticSearchArticleDTO, ElasticSearchArticle> result){


        List<ElasticSearchArticleDTO> articleDTOList = result.getDtoList();

        List<Long> artclIds = new ArrayList<>();

        for (ElasticSearchArticleDTO articleDTO : articleDTOList){

            Long artclId = articleDTO.getArtclId();

            artclIds.add(artclId);
        }

        List<ArticleElasticLockInfoDTO> articleList = articleRepository.findLockArticleListElastic(artclIds);

        for (ElasticSearchArticleDTO searchArticleDTO : articleDTOList){

            Long artclId = searchArticleDTO.getArtclId();

            for (ArticleElasticLockInfoDTO dbarticle : articleList){

                Long dbArtclId = dbarticle.getArtclId();

                if (artclId.equals(dbArtclId)){

                    searchArticleDTO.setLckYn(dbarticle.getLckYn());
                    searchArticleDTO.setLckDtm(dbarticle.getLckDtm());
                    searchArticleDTO.setLckrId(dbarticle.getLckrId());
                    searchArticleDTO.setLckrNm(dbarticle.getLckrNm());

                }
            }
        }

        result.setDtoList(articleDTOList);

        return result;

    }

    public PageResultDTO<ElasticSearchArticleDTO, ElasticSearchArticle> findAllElasticsearchCue(Date sdate, Date edate, String searchWord, Long cueId,
                                                                                                String brdcPgmId, String artclTypCd, String artclTypDtlCd,
                                                                                                String copyYn, Long deptCd, Long orgArtclId, String rptrId,
                                                                                                Integer page, Integer limit) throws Exception {

        //페이지 셋팅 page, limit null일시 page = 1 limit = 50 디폴트 셋팅
        PageHelper pageHelper = new PageHelper(page, limit);
        Pageable pageable = pageHelper.getArticlePageInfo();

        //전체조회[page type]
        Page<ElasticSearchArticle> result = elasticSearchArticleRepository.findByElasticSearchArticleListCue(sdate, edate, searchWord, cueId,
                brdcPgmId, artclTypCd, artclTypDtlCd, copyYn, deptCd, orgArtclId, rptrId, pageable);


        Function<ElasticSearchArticle, ElasticSearchArticleDTO> fn = (entity -> elasticSearchArticleMapper.toDto(entity));


        return new PageResultDTO<ElasticSearchArticleDTO, ElasticSearchArticle>(result, fn);


    }

    //기사 목록조회[이슈 기사]
    public PageResultDTO<ArticleDTO, Article> findAllIsuue(Date sdate, Date edate, String issuKwd, String artclDivCd, String artclTypCd, String artclTypDtlCd,
                                                           String artclCateCd, Long deptCd, String inputrId,
                                                           String brdcPgmId, Long orgArtclId, String delYn,
                                                           String searchDivCd, String searchWord, Integer page, Integer limit, List<String> apprvDivCdList) {

        //페이지 셋팅 page, limit null일시 page = 1 limit = 50 디폴트 셋팅
        PageHelper pageHelper = new PageHelper(page, limit);
        Pageable pageable = pageHelper.getArticlePageInfo();

       /* BooleanBuilder booleanBuilder = getSearchIssue(sdate, edate, issuKwd, artclDivCd, artclTypCd,
                artclTypDtlCd, artclCateCd, deptCd, inputrId, brdcPgmId, orgArtclId, delYn, searchDivCd, searchWord, apprvDivCdList);*/

        //전체조회[page type]
        Page<Article> result = articleRepository.findByArticleIssue(sdate, edate, issuKwd, artclDivCd, artclTypCd,
                artclTypDtlCd, artclCateCd, deptCd, inputrId, brdcPgmId, orgArtclId, delYn, searchDivCd, searchWord,
                apprvDivCdList, pageable);

        Function<Article, ArticleDTO> fn = (entity -> articleMapper.toDto(entity));

        return new PageResultDTO<ArticleDTO, Article>(result, fn);


    }

    // 큐시트에서 기사 목록 조회
    public PageResultDTO<ArticleDTO, Article> findCue(Date sdate, Date edate, String searchWord, Long cueId,
                                                      String brdcPgmId, String artclTypCd, String artclTypDtlCd,
                                                      String copyYn, Long deptCd, Long orgArtclId, Integer page, Integer limit) {

        //페이지 셋팅 page, limit null일시 page = 1 limit = 50 디폴트 셋팅
        PageHelper pageHelper = new PageHelper(page, limit);
        Pageable pageable = pageHelper.getArticlePageInfo();

        BooleanBuilder booleanBuilder = getSearchCue(sdate, edate, searchWord, cueId);

        //전체조회[page type]
        Page<Article> result = articleRepository.findByArticleCue(sdate, edate, searchWord, cueId,
                brdcPgmId, artclTypCd, artclTypDtlCd, copyYn, deptCd, orgArtclId, pageable);

        Function<Article, ArticleDTO> fn = (entity -> articleMapper.toDto(entity));

        return new PageResultDTO<ArticleDTO, Article>(result, fn);

    }

    //기사 상세정보 조회
    public ArticleDTO findDeleteArticle(Long artclId) {

        Optional<Article> articleEntity = articleRepository.findDeleteArticle(artclId);

        if (articleEntity.isPresent() == false){
            throw new ResourceNotFoundException("삭제된 기사 아이디가 없습니다. 기사 아이디  : " + artclId);
        }

        Article article = articleEntity.get();

        ArticleDTO articleDTO = articleMapper.toDto(article);

        List<ArticleMedia> articleMedia = articleMediaRepository.findDeleteArticleMediaList(artclId);
        List<ArticleMediaSimpleDTO> articleMediaDTOList = articleMediaSimpleMapper.toDtoList(articleMedia);
        List<ArticleTag> articleTagList = articleTagRepository.findArticleTag(artclId);
        List<ArticleTagDTO> articleTagDTOList = articleTagMapper.toDtoList(articleTagList);


        //방송아이콘 이미지 Url 추가. 기사자막 방송아이콘 url set
        List<ArticleCapSimpleDTO> setArticleCapDTOList = setUrlArticleCap(articleDTO.getArticleCap());
        //방송아이콘 이미지 Url 추가. 앵커자막 방송아이콘 url set
        List<AnchorCapSimpleDTO> setAnchorCapDTOList = setUrlAnchorCap(articleDTO.getAnchorCap());
        articleDTO.setArticleCap(setArticleCapDTOList);
        articleDTO.setAnchorCap(setAnchorCapDTOList);
        articleDTO.setArticleMedia(articleMediaDTOList);
        articleDTO.setArticleTag(articleTagDTOList);


        return articleDTO;

    }

    //기사 상세정보 조회
    public ArticleDTO find(Long artclId) {

        Article article = articleFindOrFail(artclId);

        ArticleDTO articleDTO = articleMapper.toDto(article);

        List<ArticleMedia> articleMedia = articleMediaRepository.findArticleMediaList(artclId);
        List<ArticleMediaSimpleDTO> articleMediaDTOList = articleMediaSimpleMapper.toDtoList(articleMedia);
        List<ArticleTag> articleTagList = articleTagRepository.findArticleTag(artclId);
        List<ArticleTagDTO> articleTagDTOList = articleTagMapper.toDtoList(articleTagList);


        //방송아이콘 이미지 Url 추가. 기사자막 방송아이콘 url set
        List<ArticleCapSimpleDTO> setArticleCapDTOList = setUrlArticleCap(articleDTO.getArticleCap());
        //방송아이콘 이미지 Url 추가. 앵커자막 방송아이콘 url set
        List<AnchorCapSimpleDTO> setAnchorCapDTOList = setUrlAnchorCap(articleDTO.getAnchorCap());
        articleDTO.setArticleCap(setArticleCapDTOList);
        articleDTO.setAnchorCap(setAnchorCapDTOList);
        articleDTO.setArticleMedia(articleMediaDTOList);
        articleDTO.setArticleTag(articleTagDTOList);


        return articleDTO;

    }

    // 기사등록[기사 이력, 자막]
    public Article create(ArticleCreateDTO articleCreateDTO, String userId) throws Exception {

        articleCreateDTO.setInputrId(userId); //등록자 아이디 추가.
        articleCreateDTO.setArtclOrd(0);//기사 순번 등록(0)set
        articleCreateDTO.setApprvDivCd(FixEnum.FIX_NONE.getFixeum(FixEnum.FIX_NONE));//기사픽스 상태 None set[null 상태가면 fix 구분 및 셋팅시 문제.]

        //PD나 앵커가 기사 작성시 기사픽스상태로 등록된다.
        /*if (userAuthChkService.authChk(AuthEnum.AnchorFix.getAuth()) == false) { //수정.
            articleCreateDTO.setApprvDivCd(FixEnum.ARTICLE_FIX.getFixeum(FixEnum.ARTICLE_FIX));
            articleCreateDTO.setArtclFixUser(userId);
            articleCreateDTO.setArtclFixDtm(new Date());
        }*/

        Article article = articleCreateMapper.toEntity(articleCreateDTO);
        articleRepository.save(article);
        //원본일 경우 orgArticleId에 articleId와 같은 값을 입력 시켜줘야 한다.
        Long articleId = article.getArtclId();//기사 자막등록 및 리턴시켜줄 기사아이디
        Long orgArticleId = orgArticleIdCreateService.orgArticleIdCreate();
        article.setOrgArtclId(orgArticleId);
        articleRepository.save(article);

        articleActionLogCreate(article, userId);//기사 액션 로그 등록

        //기사 이력 create
        Long articleHistId = createArticleHist(article);


        //기사자막, 앵커자막 create
        List<ArticleCapCreateDTO> articleCapDTOS = articleCreateDTO.getArticleCap();
        List<AnchorCapCreateDTO> anchorCapDTOS = articleCreateDTO.getAnchorCap();
        createArticleCap(articleCapDTOS, articleId, articleHistId);//기사자막 create
        createAnchorCap(anchorCapDTOS, articleId, articleHistId);//앵커자막 create.


        //send topic
        articleTopicService.articleCreateSendTopic(articleId);
        /*ArticleTopicDTO articleTopicDTO = new ArticleTopicDTO();
        articleTopicDTO.setEventId("AC");
        //이부분은 안보내줘도 될듯
        articleTopicDTO.setArtclId(articleId);
        String json = marshallingJsonHelper.MarshallingJson(articleTopicDTO);
        //String json = marshallingJson(articleTopicDTO);
        //System.out.println(json);

        topicSendService.topicWeb(json);*/

        return article;
    }

    //기사 Extra Time 수정
    public Article updateExtraTime(ArticleExtraTimeUpdateDTO articleExtraTimeUpdateDTO, Long artclId, String userId){

        Article article = articleFindOrFail(artclId);

        article.setArtclExtTime(articleExtraTimeUpdateDTO.getArtclExtTime());

        articleRepository.save(article);

        return article;
    }

    //기사 수정
    public Article update(ArticleUpdateDTO articleUpdateDTO, Long artclId, String userId) throws Exception {


        Article article = articleFindOrFail(artclId);

        IssueDTO issueDTO = articleUpdateDTO.getIssue(); //이슈 업데이트 경우 이슈는 PK값으로 되어있기 때문에 삭제후 등록.
        if (ObjectUtils.isEmpty(issueDTO) == false) {

            Long issuId = issueDTO.getIssuId(); //업데이트 할 이슈아이디 get

            Issue issue = issueService.issueFindOrFail(issuId); //수정으로 들어온 이슈가 있는지 검증.

            article.setIssue(null);
        }

        String embgYn = articleUpdateDTO.getEmbgYn();

        if ("N".equals(embgYn)) { //엠바고 플레그가 N일경우 엠바고DTM null
            article.setEmbgDtm(null);
        }

        articleUpdateDTO.setUpdtrId(userId);

        articleUpdateMapper.updateFromDto(articleUpdateDTO, article);

        articleRepository.save(article);

        //원본기사이고 fix가 fix_none일경우 copy된 기사들도 업데이트
        updateCopyArticle(article, userId, articleUpdateDTO);

        //기사 로그 등록.
        articleActionLogUpdate(article, articleUpdateDTO, userId);

        //기사이력 등록.
        Long articleHistId = updateArticleHist(article);


        //기사 자막 Update
        articleCapUpdate(article, articleUpdateDTO, articleHistId);
        anchorCapUpdate(article, articleUpdateDTO, articleHistId);

        //MQ메세지 전송
        articleTopicService.articleTopic("Aarticle Update", artclId);

        CueSheet getCueSheet = article.getCueSheet();

        if (ObjectUtils.isEmpty(getCueSheet) == false) {

            Long cueId = getCueSheet.getCueId();

            CueSheet cueSheet = findArticleCue(cueId);

            CueSheetItem cueSheetItem = findArticleCueItem(artclId);

            Long cueItemId = cueSheetItem.getCueItemId();
            String spareYn = cueSheetItem.getSpareYn();

            cueSheetTopicService.sendCueTopicCreate(cueSheet, cueId, cueItemId, artclId, null, "Update CueSheetItem-Article",
                    spareYn, "Y", "N");

        }
        /*sendTopic("Aarticle Update", artclId);*/


        return article;
    }

    public CueSheetItem findArticleCueItem(Long artclId) {

        Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findByCueItemArticle(artclId);

        if (cueSheetItem.isPresent() == false) {
            throw new ResourceNotFoundException("큐시트 아이템을 찾을 수 없습니다. 기사 아아디 : " + artclId);
        }

        return cueSheetItem.get();
    }

    public CueSheet findArticleCue(Long cueId) {

        Optional<CueSheet> cueSheet = cueSheetRepository.findByCue(cueId);

        if (cueSheet.isPresent() == false) {
            throw new ResourceNotFoundException("큐시트를 찾을 수 없습니다. 큐시트 아이디 : " + cueId);
        }

        return cueSheet.get();

    }


    //MQ메세지 전송
    /*public void sendTopic(String eventId, Long artclId) throws JsonProcessingException {

        //토픽메세지 ArticleTopicDTO Json으로 변환후 send
        ArticleTopicDTO articleTopicDTO = new ArticleTopicDTO();
        articleTopicDTO.setEventId(eventId);
        articleTopicDTO.setArtclId(artclId);
        String json = marshallingJsonHelper.MarshallingJson(articleTopicDTO);

        topicSendService.topicWeb(json);

    }*/

    //원본 기사일 경우, fix_none 상태에서 수정할 시, fix_none상태의 사본기사 내용도 수정된다.
    public void updateCopyArticle(Article article, String userId, ArticleUpdateDTO articleUpdateDTO) throws Exception {

        //복사된 기사인 경우
        Long articleId = article.getArtclId();
        Long orgArtclId = article.getOrgArtclId();
        String apprvDivCd = article.getApprvDivCd();

        Integer articleOrd = article.getArtclOrd();
        //원본기사인경우.
        if (articleOrd == 0) {
            //원본기사가 fix_none인경우
            if ("fix_none".equals(apprvDivCd)) {

                List<Article> copyArticleList = articleRepository.findCopyArticle(orgArtclId);

                for (Article copyArticle : copyArticleList) {

                    //원본기사일 경우 빠져나감
                    Integer getArticleOrder = copyArticle.getArtclOrd();
                    if (getArticleOrder == 0) {
                        continue;
                    }

                    //사본기사가 픽스구분이 fix_none인경우
                    String copyApprvDivCd = copyArticle.getApprvDivCd();
                    if ("fix_none".equals(copyApprvDivCd)) {

                        // 수정할 기사 빌드 후 업데이트 save
                        Article updateCopyArticle = copyArticleBuild(copyArticle, article);

                        //기사 로그 등록.
                        copyArticleActionLogUpdate(updateCopyArticle, article, userId);
                        //기사이력 등록.
                        Long articleHistId = updateArticleHist(updateCopyArticle);

                        //기사 미디어 update
                        //copyarticleMediaUpdate(updateCopyArticle, articleId, articleHistId, userId);

                        //기사 자막 Update
                        copyArticleCapUpdate(updateCopyArticle, articleUpdateDTO, articleHistId);
                        copyAnchorCapUpdate(updateCopyArticle, articleUpdateDTO, articleHistId);

                        /* 기사 테그를 저장하는 부분 */
                        copyArticleTag(updateCopyArticle, article);

                        //엘라스틱서치 등록
                       elasticSearchArticleService.elasticPush(updateCopyArticle);

                        Long artclId = updateCopyArticle.getArtclId();
                        //MQ메세지 전송
                        articleTopicService.articleTopic("CopyAarticle Update", artclId);
                        //sendTopic("CopyAarticle Update", artclId);
                    }
                }

            }
        }

    }

    /* 기사 테그를 저장하는 부분 */
    public void copyArticleTag(Article updateCopyArticle, Article orgArticle){

        Long newArtclId = updateCopyArticle.getArtclId();
        List<ArticleTag> orgArticleTagList = articleTagRepository.findArticleTag(newArtclId);

        //기존에 있던 테그 삭제후 재등록
        if (CollectionUtils.isEmpty(orgArticleTagList) == false){
            
            for (ArticleTag articleTag : orgArticleTagList){

                Long id = articleTag.getId();

                articleTagRepository.deleteById(id);
            }
        }


        Long artclId = orgArticle.getArtclId();

        List<ArticleTag> articleTagList = articleTagRepository.findArticleTag(artclId);

        for (ArticleTag articleTag : articleTagList){

            Tag tag = articleTag.getTag();

            ArticleTag articleTagEntity = ArticleTag.builder()
                    .article(updateCopyArticle)
                    .tag(tag)
                    .build();

            articleTagRepository.save(articleTagEntity);

        }
    }

    //기사 미디어 update
    public void copyarticleMediaUpdate(Article updateArticle, Long orgArticleId, Long articleHistId, String userId) throws Exception {

        Long artclId = updateArticle.getArtclId();

        List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaList(artclId);

        for (ArticleMedia articleMedia : articleMediaList) {

            Long artclMediaId = articleMedia.getArtclMediaId();

            String mediaTypCd = articleMedia.getMediaTypCd();

            if ("media_typ_001".equals(mediaTypCd)) {
                articleMediaRepository.deleteById(artclMediaId);
            }

        }

        List<ArticleMedia> articleMedias = articleMediaRepository.findArticleMediaList(orgArticleId);

        //기사 매칭후 전송할 부조값 구하기
        CueSheet cueSheet = updateArticle.getCueSheet();
        String subrmNm = "";
        if (ObjectUtils.isEmpty(cueSheet) == false) {

            Long cueId = cueSheet.getCueId();

            Optional<CueSheet> getCueSheet = cueSheetRepository.findByCue(cueId);

            if (getCueSheet.isPresent()) {

                CueSheet cueSheetEntity = getCueSheet.get();

                Long subrmId = cueSheetEntity.getSubrmId();

                FacilityManageDTO facilityManage = facilityManageService.find(subrmId);

                subrmNm = facilityManage.getFcltyDivCd();

            }

        }

        ArticleSimpleDTO articleSimpleDTO = ArticleSimpleDTO.builder().artclId(artclId).build();

        for (ArticleMedia setArticleMedia : articleMedias) {

            String mediaTypCd = setArticleMedia.getMediaTypCd();

            if ("media_typ_001".equals(mediaTypCd)) {

                ArticleMediaCreateDTO articleMediaCreateDTO = ArticleMediaCreateDTO.builder()
                        .mediaTypCd(setArticleMedia.getMediaTypCd())
                        .mediaOrd(setArticleMedia.getMediaOrd())
                        .contId(setArticleMedia.getContId())
                        .trnsfFileNm(setArticleMedia.getTrnsfFileNm())
                        .mediaDurtn(setArticleMedia.getMediaDurtn())
                        .mediaMtchDtm(setArticleMedia.getMediaMtchDtm())
                        .trnsfStCd(setArticleMedia.getTrnsfStCd())
                        .assnStCd(setArticleMedia.getAssnStCd())
                        .assnStCd(setArticleMedia.getAssnStCd())
                        .videoEdtrNm(setArticleMedia.getVideoEdtrNm())
                        .inputrId(userId)
                        .videoEdtrId(setArticleMedia.getVideoEdtrId())
                        .artclMediaTitl(setArticleMedia.getArtclMediaTitl())
                        .videoId(setArticleMedia.getVideoId())
                        .trnasfVal(setArticleMedia.getTrnasfVal())
                        .article(articleSimpleDTO)
                        .build();

                ArticleMedia articleMedia = articleMediaCreateMapper.toEntity(articleMediaCreateDTO);

                articleMediaRepository.save(articleMedia);

                Long mediaId = articleMedia.getArtclMediaId();
                Integer contentId = articleMedia.getContId();

                //추후에 T는 클라우드 콘피그 교체
                //부조전송
                if (subrmNm != null && subrmNm.trim().isEmpty() == false) {
                    lboxService.mediaTransfer(mediaId, contentId, subrmNm, "T", false, false);
                }
            }
        }

    }

    // 수정할 기사 빌드 후 업데이트 save
    public Article copyArticleBuild(Article copyArticle, Article article) {

        Issue getIssue = article.getIssue();
        //CueSheet cueSheet = article.getCueSheet();

        copyArticle.setChDivCd(article.getChDivCd());
        copyArticle.setArtclKindCd(article.getArtclKindCd());
        copyArticle.setArtclFrmCd(article.getArtclFrmCd());
        copyArticle.setArtclDivCd(article.getArtclDivCd());
        copyArticle.setArtclFldCd(article.getArtclFldCd());
        copyArticle.setApprvDivCd(article.getApprvDivCd());
        copyArticle.setPrdDivCd(article.getPrdDivCd());
        copyArticle.setArtclTypCd(article.getArtclTypCd());
        copyArticle.setArtclTypDtlCd(article.getArtclTypDtlCd());
        copyArticle.setArtclCateCd(article.getArtclCateCd());
        copyArticle.setArtclTitl(article.getArtclTitl());
        copyArticle.setArtclTitlEn(article.getArtclTitlEn());
        copyArticle.setArtclCtt(article.getArtclCtt());
        copyArticle.setAncMentCtt(article.getAncMentCtt());
        copyArticle.setUserGrpId(article.getUserGrpId());
        copyArticle.setArtclReqdSecDivYn(article.getArtclReqdSecDivYn());
        copyArticle.setArtclReqdSec(article.getArtclReqdSec());
        //copyArticle.setLckYn();
        //copyArticle.setLckDtm();
        copyArticle.setApprvDtm(article.getApprvDtm());
        //copyArticle.setArtclOrd();
        copyArticle.setBrdcCnt(article.getBrdcCnt());
        copyArticle.setUrgYn(article.getUrgYn());
        copyArticle.setFrnotiYn(article.getFrnotiYn());
        copyArticle.setEmbgYn(article.getEmbgYn());
        copyArticle.setEmbgDtm(article.getEmbgDtm());
        //copyArticle.setDelDtm();
        //copyArticle.setDelYn();
        copyArticle.setNotiYn(article.getNotiYn());
        copyArticle.setRegAppTyp(article.getRegAppTyp());
        //copyArticle.setBrdcPgmId(article.getBrdcPgmId());
        copyArticle.setBrdcSchdDtm(article.getBrdcSchdDtm());
        //copyArticle.setInputrId();
        //copyArticle.setUpdtrId();
        //copyArticle.setDelrId();
        copyArticle.setApprvrId(article.getApprvrId());
        //copyArticle.setLckrId();
        copyArticle.setRptrId(article.getRptrId());
        copyArticle.setArtclCttTime(article.getArtclCttTime());
        copyArticle.setAncMentCttTime(article.getAncMentCttTime());
        copyArticle.setArtclExtTime(article.getArtclExtTime());
        copyArticle.setVideoTime(article.getVideoTime());
        copyArticle.setDeptCd(article.getDeptCd());
        //copyArticle.setDeviceCd(article.getDeviceCd());
        //copyArticle.setParentArtlcId();
        copyArticle.setMemo(article.getMemo());
        copyArticle.setEditorId(article.getEditorId());
        copyArticle.setArtclFixUser(article.getArtclFixUser());
        copyArticle.setEditorFixUser(article.getEditorFixUser());
        copyArticle.setAnchorFixUser(article.getAnchorFixUser());
        copyArticle.setDeskFixUser(article.getDeskFixUser());
        copyArticle.setArtclFixDtm(article.getArtclFixDtm());
        copyArticle.setEditorFixDtm(article.getEditorFixDtm());
        copyArticle.setAnchorFixDtm(article.getAnchorFixDtm());
        copyArticle.setDeskFixDtm(article.getDeskFixDtm());
        copyArticle.setIssue(getIssue);
        //copyArticle.setCueSheet(cueSheet);

        articleRepository.save(copyArticle);

        return copyArticle;
    }

    // 수정할 기사 빌드 후 업데이트 save
    public Article fixCopyArticleBuild(Article copyArticle, Article article, String userId) {

        Issue getIssue = article.getIssue();
        //CueSheet cueSheet = article.getCueSheet();

        copyArticle.setChDivCd(article.getChDivCd());
        copyArticle.setArtclKindCd(article.getArtclKindCd());
        copyArticle.setArtclFrmCd(article.getArtclFrmCd());
        copyArticle.setArtclDivCd(article.getArtclDivCd());
        copyArticle.setArtclFldCd(article.getArtclFldCd());
        copyArticle.setApprvDivCd(FixEnum.ARTICLE_FIX.getFixeum(FixEnum.ARTICLE_FIX));
        copyArticle.setPrdDivCd(article.getPrdDivCd());
        copyArticle.setArtclTypCd(article.getArtclTypCd());
        copyArticle.setArtclTypDtlCd(article.getArtclTypDtlCd());
        copyArticle.setArtclCateCd(article.getArtclCateCd());
        copyArticle.setArtclTitl(article.getArtclTitl());
        copyArticle.setArtclTitlEn(article.getArtclTitlEn());
        copyArticle.setArtclCtt(article.getArtclCtt());
        copyArticle.setAncMentCtt(article.getAncMentCtt());
        copyArticle.setUserGrpId(article.getUserGrpId());
        copyArticle.setArtclReqdSecDivYn(article.getArtclReqdSecDivYn());
        copyArticle.setArtclReqdSec(article.getArtclReqdSec());
        //copyArticle.setLckYn();
        //copyArticle.setLckDtm();
        copyArticle.setApprvDtm(article.getApprvDtm());
        //copyArticle.setArtclOrd();
        copyArticle.setBrdcCnt(article.getBrdcCnt());
        copyArticle.setUrgYn(article.getUrgYn());
        copyArticle.setFrnotiYn(article.getFrnotiYn());
        copyArticle.setEmbgYn(article.getEmbgYn());
        copyArticle.setEmbgDtm(article.getEmbgDtm());
        //copyArticle.setDelDtm();
        //copyArticle.setDelYn();
        copyArticle.setNotiYn(article.getNotiYn());
        copyArticle.setRegAppTyp(article.getRegAppTyp());
        //copyArticle.setBrdcPgmId(article.getBrdcPgmId());
        copyArticle.setBrdcSchdDtm(article.getBrdcSchdDtm());
        //copyArticle.setInputrId();
        //copyArticle.setUpdtrId();
        //copyArticle.setDelrId();
        copyArticle.setApprvrId(article.getApprvrId());
        //copyArticle.setLckrId();
        copyArticle.setRptrId(article.getRptrId());
        copyArticle.setArtclCttTime(article.getArtclCttTime());
        copyArticle.setAncMentCttTime(article.getAncMentCttTime());
        copyArticle.setArtclExtTime(article.getArtclExtTime());
        copyArticle.setVideoTime(article.getVideoTime());
        copyArticle.setDeptCd(article.getDeptCd());
        //copyArticle.setDeviceCd(article.getDeviceCd());
        //copyArticle.setParentArtlcId();
        copyArticle.setMemo(article.getMemo());
        //copyArticle.setEditorId(article.getEditorId());
        copyArticle.setArtclFixUser(userId);
        //copyArticle.setEditorFixUser(article.getEditorFixUser());
        //copyArticle.setAnchorFixUser(article.getAnchorFixUser());
        //copyArticle.setDeskFixUser(article.getDeskFixUser());
        copyArticle.setArtclFixDtm(new Date());
        //copyArticle.setEditorFixDtm(article.getEditorFixDtm());
        //copyArticle.setAnchorFixDtm(article.getAnchorFixDtm());
        //copyArticle.setDeskFixDtm(article.getDeskFixDtm());
        copyArticle.setIssue(getIssue);
        //copyArticle.setCueSheet(cueSheet);

        articleRepository.save(copyArticle);

        return copyArticle;
    }

    //기사 삭제
    public Article delete(Long artclId, String userId) throws Exception {


        Article article = articleFindOrFail(artclId);

        //큐시트아이템에 포함되어있고 삭제가 되어있지 않은 기사면 삭제를 못한다.
        Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findArticleCue(artclId);
        if (cueSheetItem.isPresent()) {
            throw new ResourceNotFoundException("큐시트에 포함된 기사 입니다. 기사 아이디 : " + artclId);
        }

        ArticleDTO articleDTO = articleMapper.toDto(article);

        //삭제정보 등록
        articleDTO.setDelDtm(new Date());
        articleDTO.setDelrId(userId);
        articleDTO.setDelYn("Y");

        articleMapper.updateFromDto(articleDTO, article);

        articleRepository.save(article);

        //기사가 삭제될때 포함된 미디어정보도 같이 삭제처리 ( delYn = "Y")
        Set<ArticleMedia> articleMedia = article.getArticleMedia();
        deleteArticleMedia(articleMedia, userId);

        //기사 액션 로그 등록
        articleActionLogDelete(article, userId);

        //기사이력 등록.
        updateArticleHist(article);

        //MQ메세지 전송
        articleTopicService.articleTopic("Aarticle Delete", artclId);

        return article;
        //토픽메세지 ArticleTopicDTO Json으로 변환후 send
       /* ArticleTopicDTO articleTopicDTO = new ArticleTopicDTO();
        articleTopicDTO.setEventId("AD");
        articleTopicDTO.setArtclId(artclId);
        //모델부분은 안넣어줘도 될꺼같음.
        articleTopicDTO.setArticle(articleDTO);
        String json = marshallingJsonHelper.MarshallingJson(articleTopicDTO);

        Long orgArticleId = article.getOrgArtclId();

        //복사된 기사(큐시트에 포함된 기사)인 경우 interface쪽에도 큐메세지 send
        *//*if (ObjectUtils.isEmpty(orgArticleId) == false){
            topicService.topicInterface(json);
        }*//*
        topicSendService.topicWeb(json);*/
    }

    //기사가 삭제될때 포함된 미디어정보도 같이 삭제처리 ( delYn = "Y")
    public void deleteArticleMedia(Set<ArticleMedia> articleMediaSet, String userId) {

        for (ArticleMedia articleMedia : articleMediaSet) {

            ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);
            articleMediaDTO.setDelYn("Y");
            articleMediaDTO.setDelrId(userId);
            articleMediaDTO.setDelDtm(new Date());

            articleMediaMapper.updateFromDto(articleMediaDTO, articleMedia);

            articleMediaRepository.save(articleMedia);

        }
    }

    //큐시트 기사 목록조회시 큐시트에 포함되어 있는 기사 제거.
    public PageResultDTO<ElasticSearchArticleDTO, ElasticSearchArticle> confirmArticleListElastic(
            PageResultDTO<ElasticSearchArticleDTO, ElasticSearchArticle> pageList, Long cueId) {

        //현재 큐시트에 포함된 큐시트아이템을 조회
        List<CueSheetItem> cueSheetItemList = cueSheetItemRepository.findByCueItemList(cueId);

        List<ElasticSearchArticleDTO> confirmList = pageList.getDtoList();

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

    //기사 액션로그 등록
    public void articleActionLogCreate(Article article, String userId) throws Exception {

        Set<ArticleCap> articleCapList = article.getArticleCap();
        Set<AnchorCap> anchorCapList = article.getAnchorCap();
        article.setArticleCap(null);
        article.setAnchorCap(null);

        //기사 액션로그 빌드
        ArticleActionLog articleActionLog = ArticleActionLog.builder()
                .article(article)
                .message(ActionMesg.articleC.getActionMesg(ActionMesg.articleC))
                .action(ActionEnum.CREATE.getAction(ActionEnum.CREATE))
                .inputrId(userId)
                .artclCapInfo(marshallingJsonHelper.MarshallingJson(articleCapList))
                .anchorCapInfo(marshallingJsonHelper.MarshallingJson(anchorCapList))
                .artclInfo(marshallingJsonHelper.MarshallingJson(article)) //Json으로 기사내용저장
                .build();
        //기사 액션로그 등록
        articleActionLogRepository.save(articleActionLog);

    }

    //기사 액션로그 등록
    public void articleActionLogLock(Article article, String userId, String locMessage, String locAction) throws Exception {

        //List<ArticleCap> articleCapList = article.getArticleCap();
        //List<AnchorCap> anchorCapList = article.getAnchorCap();

        /*CueSheet cueSheet = article.getCueSheet();
        Long cueId = null;
        if (ObjectUtils.isEmpty(cueSheet) == false){
            cueId = cueSheet.getCueId();
        }else {

        }*/

        article.setArticleCap(null);
        article.setAnchorCap(null);
        //article.setArticleMedia(null);
        //article.setArticleTag(null);
        //article.setArticleHist(null);
        //article.setArticleOrder(null);

        //기사 액션로그 빌드
        ArticleActionLog articleActionLog = ArticleActionLog.builder()
                .article(article)
                .message(locMessage)
                .action(locAction)
                .inputrId(userId)
                //.artclCapInfo(marshallingJsonHelper.MarshallingJson(articleCapList))
                //.anchorCapInfo(marshallingJsonHelper.MarshallingJson(anchorCapList))
                .artclInfo(marshallingJsonHelper.MarshallingJson(article)) //Json으로 기사내용저장
                .build();
        //기사 액션로그 등록
        articleActionLogRepository.save(articleActionLog);

    }

    //기사 수정 로그
    public void copyArticleActionLogUpdate(Article article, Article updateArticle, String userId) throws Exception {

        Set<ArticleCap> articleCapList = article.getArticleCap();//기사로그에 등록할 기사자막 리스트를 기사에서 가져온다.
        Set<AnchorCap> anchorCapList = article.getAnchorCap();//기사로그에 등록할 앵커자막 리스트를 기사에서 가져온다.
        article.setArticleCap(null);//기사에서 기사자막삭제
        article.setAnchorCap(null);//기사에서 앵커자막삭제

        String orgAnchorMent = article.getAncMentCtt(); //원본기사 앵커 맨트
        String newAnchorMent = updateArticle.getAncMentCtt(); //수정기사 앵커 맨트

        // 앵커맨트 내용이 바뀐경우 기사액션로그 업데이트
        if (Objects.equals(orgAnchorMent, newAnchorMent) == false) {

            //기사로그 저장
            buildArticleActionLog(ActionMesg.anchorMM.getActionMesg(ActionMesg.anchorMM), userId, article,
                    articleCapList, anchorCapList);

            return;
        }

        String orgContents = article.getArtclCtt(); //원본 기사 내용
        String newContents = updateArticle.getArtclCtt(); // 수정기사 내용

        //기사 내용이 바뀐경우 기사액션로그 업데이트
        if (Objects.equals(orgContents, newContents) == false) {

            //기사로그 저장
            buildArticleActionLog(ActionMesg.articleCM.getActionMesg(ActionMesg.articleCM), userId, article,
                    articleCapList, anchorCapList);

            return;
        }

        String orgTitle = article.getArtclTitl(); //등록되어 있던 기사 제목
        String newTitle = updateArticle.getArtclTitl(); //신규 기사 제목

        // 기사제목이 바뀌었을 경우 기사액션로그 등록
        if (Objects.equals(orgTitle, newTitle) == false) {

            //기사로그 저장
            buildArticleActionLog(ActionMesg.articleTM.getActionMesg(ActionMesg.articleTM), userId, article,
                    articleCapList, anchorCapList);

            return;
        }

        String orgEnglishTile = article.getArtclTitlEn(); //원본 기사 영어 제목
        String newEnglishTile = updateArticle.getArtclTitlEn(); // 수정기사 영어제목

        //영어제목이 바뀐경우 기사액션로그 업데이트
        if (Objects.equals(orgEnglishTile, newEnglishTile) == false) {

            //기사로그 저장
            buildArticleActionLog(ActionMesg.articleTEM.getActionMesg(ActionMesg.articleTEM), userId, article,
                    articleCapList, anchorCapList);

            return;
        }

        //기사제목, 영어제목, 기사내용, 앵커맨트 가 수정된게 아니면 일반 업데이트 로그 등록.
        if (Objects.equals(orgAnchorMent, newAnchorMent) && Objects.equals(orgContents, newContents)
                && Objects.equals(orgEnglishTile, newEnglishTile) && Objects.equals(orgTitle, newTitle)) {

            //기사로그 저장
            buildArticleActionLog(ActionMesg.articleU.getActionMesg(ActionMesg.articleU), userId, article,
                    articleCapList, anchorCapList);

            return;
        }

    }

    //기사 수정 로그
    public void articleActionLogUpdate(Article article, ArticleUpdateDTO articleUpdateDTO, String userId) throws Exception {

        Set<ArticleCap> articleCapList = article.getArticleCap();//기사로그에 등록할 기사자막 리스트를 기사에서 가져온다.
        Set<AnchorCap> anchorCapList = article.getAnchorCap();//기사로그에 등록할 앵커자막 리스트를 기사에서 가져온다.
        article.setArticleCap(null);//기사에서 기사자막삭제
        article.setAnchorCap(null);//기사에서 앵커자막삭제

        String orgAnchorMent = Optional.ofNullable(article.getAncMentCtt()).orElse(""); //원본기사 앵커 맨트
        String newAnchorMent = Optional.ofNullable(articleUpdateDTO.getAncMentCtt()).orElse(""); //수정기사 앵커 맨트


        // 앵커맨트 내용이 바뀐경우 기사액션로그 업데이트
        if (Objects.equals(orgAnchorMent, newAnchorMent) == false) {

            //기사로그 저장
            buildArticleActionLog(ActionMesg.anchorMM.getActionMesg(ActionMesg.anchorMM), userId, article,
                    articleCapList, anchorCapList);

            return;
        }

        String orgContents = Optional.ofNullable(article.getArtclCtt()).orElse(""); //원본 기사 내용
        String newContents = Optional.ofNullable(articleUpdateDTO.getArtclCtt()).orElse(""); // 수정기사 내용

        //기사 내용이 바뀐경우 기사액션로그 업데이트
        if (Objects.equals(orgContents, newContents) == false) {

            //기사로그 저장
            buildArticleActionLog(ActionMesg.articleCM.getActionMesg(ActionMesg.articleCM), userId, article,
                    articleCapList, anchorCapList);

            return;
        }

        String orgTitle = Optional.ofNullable(article.getArtclTitl()).orElse(""); //등록되어 있던 기사 제목
        String newTitle = Optional.ofNullable(articleUpdateDTO.getArtclTitl()).orElse(""); //신규 기사 제목

        // 기사제목이 바뀌었을 경우 기사액션로그 등록
        if (Objects.equals(orgTitle, newTitle) == false) {

            //기사로그 저장
            buildArticleActionLog(ActionMesg.articleTM.getActionMesg(ActionMesg.articleTM), userId, article,
                    articleCapList, anchorCapList);

            return;
        }

        String orgEnglishTile = Optional.ofNullable(article.getArtclTitlEn()).orElse(""); //원본 기사 영어 제목
        String newEnglishTile = Optional.ofNullable(articleUpdateDTO.getArtclTitlEn()).orElse(""); // 수정기사 영어제목

        //영어제목이 바뀐경우 기사액션로그 업데이트
        if (Objects.equals(orgEnglishTile, newEnglishTile) == false) {

            //기사로그 저장
            buildArticleActionLog(ActionMesg.articleTEM.getActionMesg(ActionMesg.articleTEM), userId, article,
                    articleCapList, anchorCapList);

            return;
        }

        //기사제목, 영어제목, 기사내용, 앵커맨트 가 수정된게 아니면 일반 업데이트 로그 등록.
        if (Objects.equals(orgAnchorMent, newAnchorMent) && Objects.equals(orgContents, newContents)
                && Objects.equals(orgEnglishTile, newEnglishTile) && Objects.equals(orgTitle, newTitle)) {

            //기사로그 저장
            buildArticleActionLog(ActionMesg.articleU.getActionMesg(ActionMesg.articleU), userId, article,
                    articleCapList, anchorCapList);

            return;
        }

    }

    //기사로그 저장
    public void buildArticleActionLog(String actionLog, String userId, Article article,
                                      Set<ArticleCap> articleCapList, Set<AnchorCap> anchorCapList) throws JsonProcessingException {

        ArticleActionLog articleActionLog = new ArticleActionLog(); //기사액션로그 저장할 엔티티

        //기사로그 빌드
        articleActionLog = ArticleActionLog.builder()
                .article(article)
                .message(actionLog)
                .action(ActionEnum.UPDATE.getAction(ActionEnum.UPDATE))
                .inputrId(userId)
                .artclCapInfo(marshallingJsonHelper.MarshallingJson(articleCapList))
                .anchorCapInfo(marshallingJsonHelper.MarshallingJson(anchorCapList))
                .artclInfo(marshallingJsonHelper.MarshallingJson(article)) //Json으로 기사내용저장
                .build();

        //기사로그 저장
        articleActionLogRepository.save(articleActionLog);

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
        //String userId = userAuthService.authUser.getUserId();
        //articleDTO.setDelrId(userId);
        articleDTO.setDelYn("Y");

        articleMapper.updateFromDto(articleDTO, article);

        articleRepository.save(article);

        //기사 액션 로그 등록
        //articleActionLogDelete(article, userId);
    }

    // 기사 삭제 액션로그 등록
    public void articleActionLogDelete(Article article, String userId) throws Exception {

        Set<ArticleCap> articleCapList = article.getArticleCap();//기사로그에 등록할 기사자막 리스트를 기사에서 가져온다.
        Set<AnchorCap> anchorCapList = article.getAnchorCap();//기사로그에 등록할 앵커자막 리스트를 기사에서 가져온다.
        article.setArticleCap(null);//기사에서 기사자막삭제
        article.setAnchorCap(null);//기사에서 앵커자막삭제

        //기사로그 빌드
        ArticleActionLog articleActionLog = ArticleActionLog.builder()
                .article(article)
                .message(ActionMesg.articleD.getActionMesg(ActionMesg.articleD))
                .action(ActionEnum.DELETE.getAction(ActionEnum.DELETE))
                .inputrId(userId)
                .artclCapInfo(marshallingJsonHelper.MarshallingJson(articleCapList))
                .anchorCapInfo(marshallingJsonHelper.MarshallingJson(anchorCapList))
                .artclInfo(marshallingJsonHelper.MarshallingJson(article)) //Json으로 기사내용저장
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
                if (getSymbolId != null && getSymbolId.trim().isEmpty() == false) {

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
                if (getSymbolId != null && getSymbolId.trim().isEmpty() == false) {

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
                .lnOrd(articleCap.getLnOrd())
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
                .lnOrd(anchorCap.getLnOrd())
                .build();
        //디비에 등록하기위해 엔티티 변환
        AnchorCapHist anchorCapHist = anchorCapHistCreateMapper.toEntity(anchorCapHistCreateDTO);

        anchorCapHistRepository.save(anchorCapHist); //등록
    }

    //기사 오더락 체크[다른 사용자가 기사 수정중일 경우 수정 불가.]
    public ArticleAuthConfirmDTO chkOrderLock(Long artclId, String userId) {

        ArticleAuthConfirmDTO articleAuthConfirmDTO = null;

        Article article = articleFindOrFail(artclId); //해당 기사 조회.

        String lckYn = article.getLckYn(); //기사잠금 여부가 Y일 경우 return true[다른 사용자가 기사 잠금 중.]
        String lckrId = article.getLckrId();
        //String userId = userAuthService.authUser.getUserId();

        /*if (lckYn.equals("Y") && userId.equals(lckrId) == false) {
            return true;
        }*/

        if (lckYn.equals("Y") && userId.equals(lckrId) == false) {
            articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                    .artclId(article.getArtclId())
                    .lckYn(article.getLckYn())
                    .lckDtm(article.getLckDtm())
                    .lckrId(article.getLckrId())
                    .lckrNm(article.getLckrNm())
                    .msg("다른 사용자가 수정중 입니다.")
                    .authCode("Locked article")
                    .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    .build();

            return articleAuthConfirmDTO;
        }

        return articleAuthConfirmDTO;

    }

    //복사된 기사자막 수정.
    private void copyArticleCapUpdate(Article updateArticle, ArticleUpdateDTO articleUpdateDTO, Long articleHistId) {

        Long articleId = updateArticle.getArtclId();//수정할 기사자막 등록시 등록할 기사 아이디
        List<ArticleCap> articleCapList = articleCapRepository.findArticleCap(articleId);

        for (ArticleCap articleCap : articleCapList) {
            Long artclCapId = articleCap.getArtclCapId();
            articleCapRepository.deleteById(artclCapId);
        }


        List<ArticleCapCreateDTO> capSimpleList = articleUpdateDTO.getArticleCap(); //update로 들어온 기사 등록

        createArticleCap(capSimpleList, articleId, articleHistId);

        /*log.info("org cap list : "+ capSimpleList.toString());
        List<ArticleCapCreateDTO> capSimpleDTOList = new ArrayList<>();
        for (ArticleCapCreateDTO articleCap : capSimpleList) {

            Long capTemplateId = articleCap.getCapTmpltId();
            Long capTmpltId = null;
            if (ObjectUtils.isEmpty(capTemplateId) == false) {
                capTmpltId = capTemplateId;
            }
            Symbol symbol = articleCap.getSymbol();
            String symbolId = "";
            if (ObjectUtils.isEmpty(symbol) == false) {
                symbolId = symbol.getSymbolId();
            }

            ArticleCapCreateDTO articleCapCreateDTO = ArticleCapCreateDTO.builder()
                    .capDivCd(articleCap.getCapDivCd())
                    .lnNo(articleCap.getLnNo())
                    .capCtt(articleCap.getCapCtt())
                    .capRmk(articleCap.getCapRmk())
                    .lnOrd(articleCap.getLnOrd())
                    .articleId(articleId)
                    .capTmpltId(capTmpltId)
                    .symbolId(symbolId)
                    .build();

            capSimpleDTOList.add(articleCapCreateDTO);
        }
        createArticleCap(capSimpleDTOList, articleId, articleHistId); //기사자막 등록*/


    }

    //복사된 기사자막 수정.
    private void fixCopyArticleCapUpdate(Article updateArticle, Long orgArticleId, Long articleHistId) {

        Long articleId = updateArticle.getArtclId();//수정할 기사자막 등록시 등록할 기사 아이디
        List<ArticleCap> articleCapList = articleCapRepository.findArticleCap(articleId);

        for (ArticleCap articleCap : articleCapList) {
            Long artclCapId = articleCap.getArtclCapId();
            articleCapRepository.deleteById(artclCapId);
        }


        List<ArticleCap> capSimpleList = articleCapRepository.findArticleCap(orgArticleId); //update로 들어온 기사 등록

        log.info("org cap list : " + capSimpleList.toString());
        List<ArticleCapCreateDTO> capSimpleDTOList = new ArrayList<>();
        for (ArticleCap articleCap : capSimpleList) {

            CapTemplate capTemplate = articleCap.getCapTemplate();
            Long capTmpltId = null;
            if (ObjectUtils.isEmpty(capTemplate) == false) {
                capTmpltId = capTemplate.getCapTmpltId();
            }
            Symbol symbol = articleCap.getSymbol();
            String symbolId = "";
            if (ObjectUtils.isEmpty(symbol) == false) {
                symbolId = symbol.getSymbolId();
            }

            ArticleCapCreateDTO articleCapCreateDTO = ArticleCapCreateDTO.builder()
                    .capDivCd(articleCap.getCapDivCd())
                    .lnNo(articleCap.getLnNo())
                    .capCtt(articleCap.getCapCtt())
                    .capRmk(articleCap.getCapRmk())
                    .lnOrd(articleCap.getLnOrd())
                    .articleId(articleId)
                    .capTmpltId(capTmpltId)
                    .symbolId(symbolId)
                    .build();

            capSimpleDTOList.add(articleCapCreateDTO);
        }
        createArticleCap(capSimpleDTOList, articleId, articleHistId); //기사자막 등록


    }

    //복사된 기사자막 수정.
    private void articleCapUpdate(Article article, ArticleUpdateDTO articleUpdateDTO, Long articleHistId) {

        List<ArticleCapCreateDTO> capSimpleDTOList = articleUpdateDTO.getArticleCap(); //update로 들어온 기사 등록


        Long articleId = article.getArtclId();//수정할 기사자막 등록시 등록할 기사 아이디
        List<ArticleCap> articleCapList = articleCapRepository.findArticleCap(articleId);

        for (ArticleCap articleCap : articleCapList) {
            Long artclCapId = articleCap.getArtclCapId();
            articleCapRepository.deleteById(artclCapId);
        }

        createArticleCap(capSimpleDTOList, articleId, articleHistId); //기사자막 등록


    }

    //복사된 앵커자막 수정 fix.
    private void copyAnchorCapUpdate(Article updateArticle, ArticleUpdateDTO articleUpdateDTO, Long articleHistId) {

        Long articleId = updateArticle.getArtclId();//수정할 기사자막 등록시 등록할 기사 아이디
        List<AnchorCap> anchorCaps = anchorCapRepository.findAnchorCapList(articleId);

        for (AnchorCap anchorCap : anchorCaps) {
            Long anchorCapId = anchorCap.getAnchorCapId();
            anchorCapRepository.deleteById(anchorCapId);
        }

        // Long orgArticleId = article.getArtclId();

        List<AnchorCapCreateDTO> capSimpleList = articleUpdateDTO.getAnchorCap(); //update로 들어온 기사 등록
        createAnchorCap(capSimpleList, articleId, articleHistId);
       /* List<AnchorCapCreateDTO> capSimpleDTOList = new ArrayList<>();
        for (AnchorCapCreateDTO anchorCap : capSimpleList) {

            Long capTemplate = anchorCap.getCapTemplate();
            Long capTmpltId = null;
            if (ObjectUtils.isEmpty(capTemplate) == false) {
                capTmpltId = capTemplate.getCapTmpltId();
            }
            Symbol symbol = anchorCap.getSymbol();
            String symbolId = "";
            if (ObjectUtils.isEmpty(symbol) == false) {
                symbolId = symbol.getSymbolId();
            }

            AnchorCapCreateDTO articleCapCreateDTO = AnchorCapCreateDTO.builder()
                    .capDivCd(anchorCap.getCapDivCd())
                    .lnNo(anchorCap.getLnNo())
                    .capCtt(anchorCap.getCapCtt())
                    .capRmk(anchorCap.getCapRmk())
                    .lnOrd(anchorCap.getLnOrd())
                    .articleId(articleId)
                    .capTemplateId(capTmpltId)
                    .symbolId(symbolId)
                    .build();

            capSimpleDTOList.add(articleCapCreateDTO);
        }

        createAnchorCap(capSimpleDTOList, articleId, articleHistId);//앵커자막 등록*/

    }

    //복사된 앵커자막 수정 fix.
    private void fixCopyAnchorCapUpdate(Article updateArticle, Long orgArticleId, Long articleHistId) {

        Long articleId = updateArticle.getArtclId();//수정할 기사자막 등록시 등록할 기사 아이디
        List<AnchorCap> anchorCaps = anchorCapRepository.findAnchorCapList(articleId);

        for (AnchorCap anchorCap : anchorCaps) {
            Long anchorCapId = anchorCap.getAnchorCapId();
            anchorCapRepository.deleteById(anchorCapId);
        }

        // Long orgArticleId = article.getArtclId();

        List<AnchorCap> capSimpleList = anchorCapRepository.findAnchorCapList(orgArticleId); //update로 들어온 기사 등록
        List<AnchorCapCreateDTO> capSimpleDTOList = new ArrayList<>();
        for (AnchorCap anchorCap : capSimpleList) {

            CapTemplate capTemplate = anchorCap.getCapTemplate();
            Long capTmpltId = null;
            if (ObjectUtils.isEmpty(capTemplate) == false) {
                capTmpltId = capTemplate.getCapTmpltId();
            }
            Symbol symbol = anchorCap.getSymbol();
            String symbolId = "";
            if (ObjectUtils.isEmpty(symbol) == false) {
                symbolId = symbol.getSymbolId();
            }

            AnchorCapCreateDTO articleCapCreateDTO = AnchorCapCreateDTO.builder()
                    .capDivCd(anchorCap.getCapDivCd())
                    .lnNo(anchorCap.getLnNo())
                    .capCtt(anchorCap.getCapCtt())
                    .capRmk(anchorCap.getCapRmk())
                    .lnOrd(anchorCap.getLnOrd())
                    .articleId(articleId)
                    .capTemplateId(capTmpltId)
                    .symbolId(symbolId)
                    .build();

            capSimpleDTOList.add(articleCapCreateDTO);
        }

        createAnchorCap(capSimpleDTOList, articleId, articleHistId);//앵커자막 등록

    }

    //앵커자막 수정.
    private void anchorCapUpdate(Article article, ArticleUpdateDTO articleUpdateDTO, Long articleHistId) {

        List<AnchorCapCreateDTO> anchorCapCreateDTOList = articleUpdateDTO.getAnchorCap();


        Long articleId = article.getArtclId();//수정할 기사자막 등록시 등록할 기사 아이디
        List<AnchorCap> anchorCaps = anchorCapRepository.findAnchorCapList(articleId);

        for (AnchorCap anchorCap : anchorCaps) {
            Long anchorCapId = anchorCap.getAnchorCapId();
            anchorCapRepository.deleteById(anchorCapId);
        }

        createAnchorCap(anchorCapCreateDTOList, articleId, articleHistId);//앵커자막 등록

    }

    // 기사 잠금
    public ArticleAuthConfirmDTO articleLock(Long artclId, ArticleLockDTO articleLockDTO, String userId) throws Exception {

        ArticleAuthConfirmDTO articleAuthConfirmDTO = null;

        //Lock모드로 조회
        Optional<Article> articleLockEntity = articleRepository.findLockArticle(artclId);

        if (articleLockEntity.isPresent() == false) {

            articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                    .artclId(artclId)
                    .msg("삭제되었거나 존재하지 않는 기사입니다.")
                    .authCode("Not find article")
                    .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    .build();
            return articleAuthConfirmDTO;
            //throw new ResourceNotFoundException("기사를 찾을 수 없습니다. 기사 아아디 : "+artclId);
        }

        Article article = articleLockEntity.get();

        String lockYn = article.getLckYn();//잠금여부값 get
        String lckrId = article.getLckrId();

        if ("Y".equals(lockYn) && userId.equals(lckrId) == false) {
            articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                    .artclId(article.getArtclId())
                    .lckYn(article.getLckYn())
                    .lckDtm(article.getLckDtm())
                    .lckrId(article.getLckrId())
                    .lckrNm(article.getLckrNm())
                    .msg("다른 사용자가 수정중 입니다.")
                    .authCode("Locked article")
                    .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    .build();

            return articleAuthConfirmDTO;
            //throw new ResponseStatusException(HttpStatus.FORBIDDEN); // 잠금 사용자가 다르다.
        }

        String locMessage = "";
        String locAction = "";

        if (articleLockDTO.getLckYn().equals("Y")) {
            articleLockDTO.setLckDtm(new Date());
            // 토큰 인증된 사용자 아이디를 입력자로 등록
            articleLockDTO.setLckrId(userId);

            locMessage = ActionMesg.articleLOCK.getActionMesg(ActionMesg.articleLOCK);
            locAction = ActionEnum.LOCK.getAction(ActionEnum.LOCK);
        } else {
            article.setLckDtm(null);
            article.setLckrId(null);

            locMessage = ActionMesg.articleUNLOCK.getActionMesg(ActionMesg.articleUNLOCK);
            locAction = ActionEnum.UNLOCK.getAction(ActionEnum.UNLOCK);
        }

        articleLockMapper.updateFromDto(articleLockDTO, article);

        articleRepository.save(article);


        articleActionLogLock(article, userId, locMessage, locAction);//기사 액션 로그 등록

        //기사 이력 create
        //Long articleHistId = createArticleHist(article);

        User user = userService.userFindOrFail(userId);
        String userNm = user.getUserNm();

        Date lckDtm = article.getLckDtm();
        String lckYn = article.getLckYn();
        String lckrNm = article.getLckrNm();

        //MQ메세지 전송
        articleTopicService.articleLockTopic("Article Lock", artclId, " Lock User Name : " + userNm + " ( " + userId + " )",
                lckDtm, userId, userNm);

        //토픽메세지 ArticleTopicDTO Json으로 변환후 send
        /*ArticleTopicDTO articleTopicDTO = new ArticleTopicDTO();
        articleTopicDTO.setEventId("Article Lock");
        articleTopicDTO.setArtclId(artclId);
        //모델부분은 안넣어줘도 될꺼같음.
        articleTopicDTO.setArticle(articleLockDTO);
        String json = marshallingJsonHelper.MarshallingJson(articleTopicDTO);*/

        Long orgArticleId = article.getOrgArtclId();

        if (artclId.equals(orgArticleId) == false) {
            //topicSendService.topicInterface(json);
            interfaceTopicService.articleLock("Article Lock", artclId);
        }
        //topicSendService.topicWeb(json);

        log.info("Article Lock : UserId : " + userId + "Lock Value : " + lockYn);

        return articleAuthConfirmDTO;
    }

    //기사 잠금 해제
    public void articleUnlock(Long artclId, ArticleLockDTO articleLockDTO, String userId) throws Exception {

        Article article = articleFindOrFail(artclId); //기사아이디로 기사정보조회 및 기사유무 확인.

        User user = userService.userFindOrFail(userId);
        String userNm = user.getUserNm();

        String locMessage = "";
        String locAction = "";

        if (articleLockDTO.getLckYn().equals("N")) {
            article.setLckDtm(null);
            article.setLckrId(null);

            locMessage = ActionMesg.articleForcedLock.getActionMesg(ActionMesg.articleForcedLock);
            locAction = ActionEnum.FORCEDLOCK.getAction(ActionEnum.FORCEDLOCK);
        }

        articleLockMapper.updateFromDto(articleLockDTO, article);
        articleRepository.save(article);

        articleActionLogLock(article, userId, locMessage, locAction);//기사 액션 로그 등록

        Date lckDtm = article.getLckDtm();
        String lckYn = article.getLckYn();
        String lckrId = article.getLckrId();
        String lckrNm = article.getLckrNm();

        //MQ메세지 전송
        articleTopicService.articleLockTopic("Article UnLock", artclId, " Unlock User Name : " + userNm + " ( " + userId + " )",
                lckDtm, userId, userNm);

    }

    //기사 정보조회 존재여부 확인
    public Article articleFindOrFail(Long artclId) {

        Optional<Article> article = articleRepository.findArticle(artclId);

        if (!article.isPresent()) {
            throw new ResourceNotFoundException("기사 아이디가 없습니다. article Id  : " + artclId);
        }

        return article.get();

    }

    //기사 정보조회 존재여부 확인
    public Article articleFindOrFailCueItem(Long artclId) {

        Optional<Article> article = articleRepository.findArticle(artclId);

        Article emptyArticle = null;
        if (article.isPresent() == false) {
            return emptyArticle;
        }

        return article.get();

    }

    //기사 목록조회시 조건 빌드[일반 목록조회 ]
    public BooleanBuilder getSearch(Date sdate, Date edate, Date rcvDt, String rptrId, String inputrId, String brdcPgmId,
                                    String artclDivCd, String artclTypCd, String searchDivCd, String searchWord,
                                    List<String> apprvDivCdList, Long deptCd, String artclCateCd, String artclTypDtlCd,
                                    String delYn, Long artclId, String copyYn, Long orgArtclId) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticle qArticle = QArticle.article;
        QUser qUser = QUser.user;

        //삭제가 되지 않은 기사 조회
        //booleanBuilder.and(qArticle.delYn.eq("N"));

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
        //검색조건 = 삭제 여부
        if (delYn != null && delYn.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.delYn.eq(delYn));
        } else {
            booleanBuilder.and(qArticle.delYn.eq("N")); //삭제여부값 안들어 올시 디폴트 'N'
        }
        //검색어로 조회
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            //검색구분코드 01 일때 기사 제목으로 검색
            if ("01".equals(searchDivCd)) {
                booleanBuilder.and(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
            }
            //검색구분코드 02 일때 기자이름으로 검색
            else if ("02".equals(searchDivCd)) {
                booleanBuilder.and(qArticle.rptrId.eq(String.valueOf(qUser.userNm.contains(searchWord))));
            }
            //검색구분코드 안들어왔을 경우
            else if (searchDivCd == null || searchDivCd.trim().isEmpty()) {
                booleanBuilder.and(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
            }

        }

        //픽스구분코드[여러개의 or조건으로 가능]
        if (CollectionUtils.isEmpty(apprvDivCdList) == false) {
            booleanBuilder.and(qArticle.apprvDivCd.in(apprvDivCdList));
        }

        //부서코드
        if (deptCd != 0 && deptCd != null) {
            booleanBuilder.and(qArticle.deptCd.eq(deptCd));
        }
        //기사카테고리코드
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.artclCateCd.eq(artclCateCd));
        }
        //기사유형상세코드
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.artclTypDtlCd.eq(artclTypDtlCd));
        }
        //기사 아이디
        if (ObjectUtils.isEmpty(artclId) == false) {
            booleanBuilder.and(qArticle.artclId.eq(artclId));
        }
        //원본 기사 및 복사된 기사 검색조건
        if (copyYn != null && copyYn.trim().isEmpty() == false) {

            if ("N".equals(copyYn)) {
                booleanBuilder.and(qArticle.artclOrd.eq(0));
            } else {
                booleanBuilder.and(qArticle.artclOrd.ne(0));
            }
        }

        if (ObjectUtils.isEmpty(orgArtclId) == false) {
            booleanBuilder.and(qArticle.orgArtclId.eq(orgArtclId));
        }

        //PAQueryFactory queryFactory = new JPAQueryFactory(em);

        /*//기사 타입 코드로 조회
        if (!StringUtils.isEmpty(issuId)) {
            booleanBuilder.and(qArticle.issue.issuId.eq(issuId));
        }*/


        return booleanBuilder;
    }

    //기사 목록조회 조회조건 빌드[이슈 기사]
    public BooleanBuilder getSearchIssue(Date sdate, Date edate, String issuKwd, String artclDivCd, String artclTypCd,
                                         String artclTypDtlCd, String artclCateCd, String deptCd, String inputrId,
                                         String brdcPgmId, Long orgArtclId, String delYn, String searchDivCd, String searchWord, List<String> apprvDivCdList) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticle qArticle = QArticle.article;
        QUser qUser = QUser.user;

        //이슈 검색일 조건
        if (ObjectUtils.isEmpty(sdate) && ObjectUtils.isEmpty(edate)) {
            booleanBuilder.and(qArticle.issue.issuDtm.between(sdate, edate));
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
        //if (deptCd != null && deptCd.trim().isEmpty() == false) {
        //    booleanBuilder.and(qArticle.deptCd.eq(deptCd));
        //}
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

        //픽스구분코드[여러개의 or조건으로 가능]
        if (CollectionUtils.isEmpty(apprvDivCdList) == false) {

            int listSize = apprvDivCdList.size(); //리스트 길이에 맞춰 or조건 set

            if (listSize == 1) {
                booleanBuilder.and(qArticle.apprvDivCd.eq(apprvDivCdList.get(0)));
            } else if (listSize == 2) {
                booleanBuilder.and(qArticle.apprvDivCd.eq(apprvDivCdList.get(0)).or(qArticle.apprvDivCd.eq(apprvDivCdList.get(1))));
            } else if (listSize == 3) {
                booleanBuilder.and(qArticle.apprvDivCd.eq(apprvDivCdList.get(0)).or(qArticle.apprvDivCd.eq(apprvDivCdList.get(1)))
                        .or(qArticle.apprvDivCd.eq(apprvDivCdList.get(2))));
            } else if (listSize == 4) {
                booleanBuilder.and(qArticle.apprvDivCd.eq(apprvDivCdList.get(0)).or(qArticle.apprvDivCd.eq(apprvDivCdList.get(1)))
                        .or(qArticle.apprvDivCd.eq(apprvDivCdList.get(2))).or(qArticle.apprvDivCd.eq(apprvDivCdList.get(3))));
            } else if (listSize == 5) {
                booleanBuilder.and(qArticle.apprvDivCd.eq(apprvDivCdList.get(0)).or(qArticle.apprvDivCd.eq(apprvDivCdList.get(1)))
                        .or(qArticle.apprvDivCd.eq(apprvDivCdList.get(2))).or(qArticle.apprvDivCd.eq(apprvDivCdList.get(3)))
                        .or(qArticle.apprvDivCd.eq(apprvDivCdList.get(4))));
            }

        }

        return booleanBuilder;
    }

    //큐시트 기사자막 조회 조건
    public BooleanBuilder getSearchCue(Date sdate, Date edate, String searchWord, Long cueId) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticle qArticle = QArticle.article;

        booleanBuilder.and(qArticle.delYn.eq("N"));

        //등록날짜 기준으로 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qArticle.inputDtm.between(sdate, edate));
        }

        //기사제목 기준으로 조회
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            booleanBuilder.and(qArticle.artclTitl.contains(searchWord));
        }

        //큐시트 아이디로 조회
        /*if (ObjectUtils.isEmpty(cueId) == false){
            booleanBuilder.and(qArticle.cueId.eq(cueId));
        }*/

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

        Long artclId = article.getArtclId();

        //기사 아이디로 기사 히스트로 조회
        int articleHistVer = articleHistRepository.findArticleHistByArticle(artclId);

        ArticleHist getArticleHist = new ArticleHist();

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
                .ver(articleHistVer + 1) //수정! 버전 설정?
                .build();

        articleHistRepository.save(articleHist);

        return articleHist.getArtclHistId();
    }

    //기사 픽스
    public Article vaildFixStaus(Long artclId, String apprvDivCd, String userId) throws Exception {

        //기사 조회.
        Article article = articleFindOrFail(artclId);

        String lckrId = article.getLckrId();
        String lckrNm = article.getLckrNm();
        String lckYn = article.getLckYn();

        if ("Y".equals(lckYn)) {

            if (userId.equals(lckrId) == false) {
                throw new ResourceNotFoundException(" 다른 사용자가 수정중 입니다.  수정중인 사용자 : " + lckrNm + " ( " + lckrId + " )");
            }

        }

        String orgApprvDivcd = article.getApprvDivCd(); //조회된 기사픽스 구분코드 get
        // 조회된 기사픽스 구분코드와 새로들어온 구분코드가 같으면 return
        //에러를 내지않고 유지하기 위해.
        if (apprvDivCd.equals(orgApprvDivcd)) {
            return null;
        }

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

        //픽스단계대로 픽스자, 픽스일시 set[원본이 픽스가 된경우, 복사본이 픽스가 안됫을 경우 복사본도 같이 픽스]
        article = fixUserInfoSet(apprvDivCd, orgApprvDivcd, article, userId);

        //ArticleDTO articleDTO = articleMapper.toDto(article);

        apprvDivCd = confirmApprvDivCd(apprvDivCd, article);

        article.setApprvDivCd(apprvDivCd); //픽스 구분코드 변경.
        //articleDTO.setApprvrId(userId);//픽스 승인자 변경.
        article.setApprvDtm(new Date());

        //articleMapper.updateFromDto(articleDTO, article);
        articleRepository.save(article);

        //기사로그 등록.
        articleActionLogfix(apprvDivCd, orgApprvDivcd, userId, article);


        return article;
    }

    //픽스 단계 맞추기
    public String confirmApprvDivCd(String apprvDivCd, Article article) {

        String artclFixUser = article.getArtclFixUser();
        String editorFixUser = article.getEditorFixUser();
        String anchorFixUser = article.getAnchorFixUser();
        String deskFixUser = article.getDeskFixUser();


        if ("article_fix".equals(apprvDivCd)) {
            if (artclFixUser == null || artclFixUser.trim().isEmpty()) {
                return "fix_none";
            } else {
                return "article_fix";
            }

        } else if ("editor_fix".equals(apprvDivCd)) {

            if (editorFixUser == null || editorFixUser.trim().isEmpty()) {

                if (artclFixUser == null || artclFixUser.trim().isEmpty()) {
                    return "fix_none";
                } else {
                    return "article_fix";
                }

            } else {
                return "editor_fix";
            }

        } else if ("anchor_fix".equals(apprvDivCd)) {

            if (anchorFixUser == null || anchorFixUser.trim().isEmpty()) {

                if (editorFixUser == null || editorFixUser.trim().isEmpty()) {

                    if (artclFixUser == null || artclFixUser.trim().isEmpty()) {
                        return "fix_none";
                    } else {
                        return "article_fix";
                    }

                } else {
                    return "editor_fix";
                }

            } else {
                return "anchor_fix";
            }

        }


        return apprvDivCd;
    }

    //픽스단계대로 픽스자, 픽스일시 set
    public Article fixUserInfoSet(String apprvDivCd, String orgApprvDivcd, Article article, String userId) throws Exception {

        String artclFixUser = article.getArtclFixUser();
        String editorFixUser = article.getEditorFixUser();
        String anchorFixUser = article.getAnchorFixUser();
        String deskFixUser = article.getDeskFixUser();


        //픽스 fix_none으로 들어온경우
        if ("fix_none".equals(apprvDivCd)) {

            // article_fix에서 fix_none으로 픽스를 풀 경우 등록된 기자,일시 삭제
            //if ("article_fix".equals(orgApprvDivcd)) {
            article.setArtclFixUser(null);
            article.setArtclFixDtm(null);
            article.setEditorFixUser(null);
            article.setEditorFixDtm(null);
            article.setEditorId(null);
            article.setAnchorFixUser(null);
            article.setAnchorFixDtm(null);
            article.setDeskFixUser(null);
            article.setDeskFixDtm(null);
            //}
        }

        //픽스 article_fix으로 들어온경우
        if ("article_fix".equals(apprvDivCd)) {

            if ("fix_none".equals(orgApprvDivcd)) {
                article.setArtclFixUser(userId);
                article.setArtclFixDtm(new Date());
                article.setEditorFixUser(null);
                article.setEditorId(null);
                article.setEditorFixDtm(null);
                article.setAnchorFixUser(null);
                article.setAnchorFixDtm(null);
                article.setDeskFixUser(null);
                article.setDeskFixDtm(null);

                Integer orgArticleOrder = article.getArtclOrd();
                Long orgArticleId = article.getOrgArtclId();
                Long artclId = article.getArtclId();

                List<Article> articleList = articleRepository.findCopyArticle(orgArticleId);

                for (Article articleEntity : articleList) {

                    Long getArticleId = articleEntity.getArtclId();
                    Integer articleOrd = articleEntity.getArtclOrd();
                    Long getOrgArticleId = articleEntity.getOrgArtclId();

                    //원본 기사일 경우
                    if (articleOrd == 0) {
                        continue;
                    }

                    String apprvDicCd = articleEntity.getApprvDivCd();

                    if ("fix_none".equals(apprvDicCd)) { //복사된 기사가 fix_none일경우 article_fix로 업데이트

                        //Long orgArtclId = article.getOrgArtclId();
                        if (orgArticleOrder == 0) {//원본 기사일 경우 기사내용전부 업데이트

                            // 수정할 기사 빌드 후 업데이트 save
                            Article updateCopyArticle = fixCopyArticleBuild(articleEntity, article, userId);

                            //기사 로그 등록.
                            copyArticleActionLogUpdate(updateCopyArticle, article, userId);
                            //기사이력 등록.
                            Long articleHistId = updateArticleHist(updateCopyArticle);
                            //기사 자막 Update
                            fixCopyArticleCapUpdate(updateCopyArticle, artclId, articleHistId);
                            fixCopyAnchorCapUpdate(updateCopyArticle, artclId, articleHistId);

                            /* 기사 테그를 저장하는 부분 */
                            copyArticleTag(updateCopyArticle, article);

                            //엘라스틱서치 등록
                            elasticSearchArticleService.elasticPush(updateCopyArticle);

                            Long updateArtclId = updateCopyArticle.getArtclId();
                            //MQ메세지 전송
                            articleTopicService.articleTopic("CopyAarticle Update", updateArtclId);
                            //sendTopic("CopyAarticle Update", updateArtclId);

                        }/*else {

                            ArticleDTO articleDTO = articleMapper.toDto(articleEntity);
                            articleDTO.setApprvDivCd("article_fix");
                            articleDTO.setArtclFixUser(userId);
                            articleDTO.setArtclFixDtm(new Date());

                            articleMapper.updateFromDto(articleDTO, articleEntity);

                            articleRepository.save(articleEntity);
                        }*/
                    }
                }

            } else { // article_fix 으로 픽스를 풀 경우 등록된 에디터, 앵커 기록 삭제

                article.setEditorFixUser(null);
                article.setEditorFixDtm(null);
                article.setEditorId(null);
                article.setAnchorFixUser(null);
                article.setAnchorFixDtm(null);
                article.setDeskFixUser(null);
                article.setDeskFixDtm(null);

            }

        }
        //에디터 픽스일 경우 픽스한 에디터 set
        if ("editor_fix".equals(apprvDivCd)) {

            if ("article_fix".equals(orgApprvDivcd)) {
                article.setEditorFixUser(userId);
                article.setEditorFixDtm(new Date());
                article.setEditorId(userId);
                article.setAnchorFixUser(null);
                article.setAnchorFixDtm(null);
                article.setDeskFixUser(null);
                article.setDeskFixDtm(null);

            } else { // editor_fix 으로 픽스를 풀 경우 등록된 앵커, 데스커 기록 삭제

                if ("fix_none".equals(orgApprvDivcd)) {

                    article.setArtclFixUser(userId);
                    article.setArtclFixDtm(new Date());
                    article.setEditorFixUser(userId);
                    article.setEditorFixDtm(new Date());
                    article.setEditorId(userId);
                    article.setAnchorFixUser(null);
                    article.setAnchorFixDtm(null);
                    article.setDeskFixUser(null);
                    article.setDeskFixDtm(null);
                } else {

                    article.setAnchorFixUser(null);
                    article.setAnchorFixDtm(null);
                    article.setDeskFixUser(null);
                    article.setDeskFixDtm(null);
                }

            }
        }
        //앵커 픽스일 경우 픽스한 앵커 set
        if ("anchor_fix".equals(apprvDivCd)) {

            if ("desk_fix".equals(orgApprvDivcd)) { // anchor_fix 으로 픽스를 풀 경우 등록된 데스커 기록 삭제

                article.setDeskFixUser(null);
                article.setDeskFixDtm(null);

            } else {

                if ("fix_none".equals(orgApprvDivcd)) {

                    article.setArtclFixUser(userId);
                    article.setArtclFixDtm(new Date());
                    article.setEditorFixUser(userId);
                    article.setEditorFixDtm(new Date());
                    article.setEditorId(userId);
                    article.setAnchorFixUser(userId);
                    article.setAnchorFixDtm(new Date());
                    article.setDeskFixUser(null);
                    article.setDeskFixDtm(null);
                } else if ("article_fix".equals(orgApprvDivcd)) {

                    article.setEditorFixUser(userId);
                    article.setEditorFixDtm(new Date());
                    article.setEditorId(userId);
                    article.setAnchorFixUser(userId);
                    article.setAnchorFixDtm(new Date());
                    article.setDeskFixUser(null);
                    article.setDeskFixDtm(null);

                } else if ("editor_fix".equals(orgApprvDivcd)) {

                    article.setAnchorFixUser(userId);
                    article.setAnchorFixDtm(new Date());

                }

            }

        }
        //데스커 픽스일 경우 픽스한 데스커 set
        if ("desk_fix".equals(apprvDivCd)) {

            if ("fix_none".equals(orgApprvDivcd)) {

                article.setArtclFixUser(userId);
                article.setArtclFixDtm(new Date());
                article.setEditorFixUser(userId);
                article.setEditorFixDtm(new Date());
                article.setEditorId(userId);
                article.setAnchorFixUser(userId);
                article.setAnchorFixDtm(new Date());
                article.setDeskFixUser(userId);
                article.setDeskFixDtm(new Date());
            } else if ("article_fix".equals(orgApprvDivcd)) {

                article.setEditorFixUser(userId);
                article.setEditorFixDtm(new Date());
                article.setEditorId(userId);
                article.setAnchorFixUser(userId);
                article.setAnchorFixDtm(new Date());
                article.setDeskFixUser(userId);
                article.setDeskFixDtm(new Date());

            } else if ("editor_fix".equals(orgApprvDivcd)) {

                article.setAnchorFixUser(userId);
                article.setAnchorFixDtm(new Date());
                article.setDeskFixUser(userId);
                article.setDeskFixDtm(new Date());

            } else if ("anchor_fix".equals(orgApprvDivcd)) {

                article.setDeskFixUser(userId);
                article.setDeskFixDtm(new Date());
            }


        }

        return article;
    }

    //기사 액션 로그 등록
    public void articleActionLogfix(String apprvDivCd, String orgApprvDivcd, String userId, Article article) throws Exception {

        Set<ArticleCap> articleCapList = article.getArticleCap();//기사로그에 등록할 기사자막 리스트를 기사에서 가져온다.
        Set<AnchorCap> anchorCapList = article.getAnchorCap();//기사로그에 등록할 앵커자막 리스트를 기사에서 가져온다.
        article.setArticleCap(null);//기사에서 기사자막삭제
        article.setAnchorCap(null);//기사에서 앵커자막삭제

        //기사 액션로그 빌드
        ArticleActionLog articleActionLog = ArticleActionLog.builder()
                .article(article)
                .message(ActionMesg.fixM.getActionMesg(ActionMesg.fixM) + " " + "'" + orgApprvDivcd + "'" + " to " + "'" + apprvDivCd + "'")
                .action(ActionEnum.UPDATE.getAction(ActionEnum.UPDATE))
                .inputrId(userId)
                .artclCapInfo(marshallingJsonHelper.MarshallingJson(articleCapList))
                .anchorCapInfo(marshallingJsonHelper.MarshallingJson(anchorCapList))
                .artclInfo(marshallingJsonHelper.MarshallingJson(article)) //Json으로 기사내용저장
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
    public void confirmUser(ArticleDeleteConfirmDTO articleDeleteConfirmDTO, Long artclId, String userId) throws NoSuchAlgorithmException {

        User userEntity = userService.userFindOrFail(userId); //사용자 아이디로 사용자 유무 확인 및 사용자 정보조회.

        String pwd = articleDeleteConfirmDTO.getPassword();//파라미터로 들어온 비밀번호

        EncodingHelper encodingHelper = new EncodingHelper(pwd, saltKey);
        String hexPwd = encodingHelper.getHex();

        if (passwordEncoder.matches(hexPwd, userEntity.getPwd()) == false) { //현재 들어온 비밀번호와 등록되어 있는 비밀번호 확인
            throw new PasswordFailedException("Password failed.");
        }

        /*String pssword = passwordEncoder.*/

        Article article = articleFindOrFail(artclId);//기사조회 및 존재유무 확인.
        String inputr = article.getInputrId();

        if (inputr.equals(userId) == false) { //기사입력자 확인, 본인기사만 삭제 가능.
            if (userAuthChkService.authChk(AuthEnum.DeskFix.getAuth())) {
                throw new UserFailException("기사를 입력한 사용자가 아닙니다. 기사 입력자 아이디 : " + inputr);
            }
        } else {
            if (userAuthChkService.authChk(AuthEnum.DeskFix.getAuth()) ) {//관리자 권한 확인. 본인기사가 아니더라도 관리자면 삭제가능
                if (userId.equals(inputr) == false) { //하 잘못짯다 오픈전에 아라서 급하게 또 처리 ....ㅎ

                    throw new UserFailException("관리자 권한 이거나 기사를 입력한 사용자만 기사 삭제가 가능 합니다.");
                }
            }
        }


    }

    //기사 잠금여부 및 권한 확인.
    public ArticleAuthConfirmDTO articleConfirm(Long artclId, String userId) {

        Article article = articleFindOrFail(artclId); //기사아이디로 기사정보조회 및 기사유무 확인.

        String orgApprDivCd = article.getApprvDivCd(); //현재 기사의 픽스 값을 get
        String orgLckYn = article.getLckYn(); //잠금 여부값 확인
        String inputr = article.getInputrId(); //기사 작성자 get
        String lckrId = article.getLckrId();// 잠금 사용자 확인


        if ("Y".equals(orgLckYn) && userId.equals(lckrId) == false) { //이미 lckYn 값이 Y이면 잠긴상태이므로 리턴 true로 예외처리로 넘어간다.
            ArticleAuthConfirmDTO articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                    .artclId(article.getArtclId())
                    .lckYn(article.getLckYn())
                    .lckDtm(article.getLckDtm())
                    .lckrId(article.getLckrId())
                    .lckrNm(article.getLckrNm())
                    .msg("다른 사용자가 수정중 입니다.")
                    .authCode("Locked article")
                    .httpStatus(HttpStatus.METHOD_NOT_ALLOWED)
                    .build();

            return articleAuthConfirmDTO;
        }


        //현재접속자 그룹정보를 그룹이넘 리스트로 불러온다.
        List<String> fixAuthList = getAppAuth(userId);

        //픽스 권한 리스트로 들어온 파라미터 중 최상위 권한 get
        //AuthEnum fixAuth = AuthEnum.certity(fixAuthList);
        //권한이 PD인경우 기사 수정이 불가.
        /*if (fixAuth.equals(AuthEnum.PD)){ //수정.

            ArticleAuthConfirmDTO articleAuthConfirmDTO = ArticleAuthConfirmDTO.builder()
                    .artclId(article.getArtclId())
                    .msg("PD 권한은 기사를 수정 할 수 없습니다.")
                    .authCode("qArticlenot_be_modified")
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .build();

            return articleAuthConfirmDTO;//작상자만 기사를 수정가능
        }*/

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

            //FIX_NONE 상태일 경우 article_fix할 권한이상이 있는경우만 수정가능.
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

            // ARTICLE_FIX 상태일 경우 에디터 픽스 권한이상이 있는경우만 수정가능.
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

            // EDITOR_FIX 상태일 경우 할 ANCHOR_FIX 권한이상이 있는경우만 수정가능.
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

            // ANCHOR_FIX 상태일 경우 할 DESK_FIX 권한이상이 있는경우만 수정가능.
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

            // DESK_FIX 상태일 경우 할 DESK_FIX 권한이상이 있는경우만 수정가능.
            paf.setApproveCode(FixEnum.DESK_FIX.getFixeum(FixEnum.DESK_FIX));


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

    //기사 락이 6시간이상 걸려있는 기사 체크후 락 해제
    public void articleLockChk() {

        /*Date nowDate = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);

        cal.add(Calendar.HOUR, -6);

        Date formatDate = cal.getTime();*/

        //현재시간에서 -6시간 뺀 Date값을 가져온다
        Date formatDate = dateChangeHelper.LockChkTime();

        List<Article> articleList = articleRepository.findLockChkList(formatDate, "Y");

        for (Article article : articleList) {

            Long artclId = article.getArtclId();
            String lckrNm = article.getLckrNm();
            String lckrId = article.getLckrId();

            ArticleDTO articleDTO = articleMapper.toDto(article);
            articleDTO.setLckYn("N");
            articleDTO.setLckDtm(null);
            articleDTO.setLckrId(null);

            articleMapper.updateFromDto(articleDTO, article);
            articleRepository.save(article);

            log.info("Article UnLock From Server. Article Id : " + artclId + " Lock User Name : " + lckrNm + " ( " + lckrId + " )");
        }

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

