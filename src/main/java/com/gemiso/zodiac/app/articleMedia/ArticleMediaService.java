package com.gemiso.zodiac.app.articleMedia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleRepository;
import com.gemiso.zodiac.app.article.ArticleService;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.article.mapper.ArticleMapper;
import com.gemiso.zodiac.app.articleActionLog.ArticleActionLog;
import com.gemiso.zodiac.app.articleActionLog.ArticleActionLogRepository;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaCreateDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaUpdateDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaCreateMapper;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaMapper;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaUpdateMapper;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetRepository;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.elasticsearch.ElasticSearchArticleService;
import com.gemiso.zodiac.app.facilityManage.FacilityManageService;
import com.gemiso.zodiac.app.facilityManage.dto.FacilityManageDTO;
import com.gemiso.zodiac.app.lbox.LboxService;
import com.gemiso.zodiac.core.enumeration.ActionEnum;
import com.gemiso.zodiac.core.enumeration.ActionMesg;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.topic.ArticleTopicService;
import com.gemiso.zodiac.core.topic.CueSheetTopicService;
import com.gemiso.zodiac.core.topic.TopicSendService;
import com.gemiso.zodiac.core.topic.cueSheetTopicDTO.CueSheetWebTopicDTO;
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
public class ArticleMediaService {

    private final ArticleMediaRepository articleMediaRepository;
    private final ArticleRepository articleRepository;
    private final CueSheetRepository cueSheetRepository;
    private final CueSheetItemRepository cueSheetItemRepository;
    private final ArticleActionLogRepository articleActionLogRepository;

    private final ArticleMediaMapper articleMediaMapper;
    private final ArticleMediaCreateMapper articleMediaCreateMapper;
    private final ArticleMediaUpdateMapper articleMediaUpdateMapper;
    private final ArticleMapper articleMapper;

    private final ArticleService articleService;
    private final CueSheetTopicService cueSheetTopicService;
    private final ElasticSearchArticleService elasticSearchArticleService;
    //private final CueSheetItemService cueSheetItemService;
    //private final CueSheetService cueSheetService;

    //private final UserAuthService userAuthService;

    private final TopicSendService topicSendService;
    private final FacilityManageService facilityManageService;
    private final LboxService lboxService;
    private final ArticleTopicService articleTopicService;

    private final MarshallingJsonHelper marshallingJsonHelper;


    public List<ArticleMediaDTO> findAll(Date sdate, Date edate, String trnsfFileNm, Long artclId, String mediaTypCd) {

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, trnsfFileNm, artclId, mediaTypCd);

        List<ArticleMedia> articleMediaList = (List<ArticleMedia>) articleMediaRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "mediaOrd"));

        List<ArticleMediaDTO> articleMediaDTOList = articleMediaMapper.toDtoList(articleMediaList);

        return articleMediaDTOList;
    }

    public ArticleMediaDTO find(Long artclMediaId) {

        ArticleMedia articleMedia = articleMediaFindOrFail(artclMediaId);

        ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);

        return articleMediaDTO;

    }

    public ArticleMediaDTO create(ArticleMediaCreateDTO articleMediaCreateDTO, String userId) throws Exception {

        /**********기사 미디어 타입 계산**********/

        /*ArticleSimpleDTO articleSimpleDTO = articleMediaCreateDTO.getArticle();
        Long artclId = articleSimpleDTO.getArtclId();
        Article article = articleService.articleFindOrFail(artclId);

        int videoTime = Optional.ofNullable(article.getVideoTime()).orElse(0);
        int mediaDuration = Optional.ofNullable(articleMediaCreateDTO.getMediaDurtn()).orElse(0);

        ArticleDTO articleDTO = articleMapper.toDto(article);
        articleDTO.setVideoTime(videoTime + mediaDuration);

        articleMapper.updateFromDto(articleDTO, article);

        articleRepository.save(article);*/


        articleMediaCreateDTO.setInputrId(userId);

        ArticleMedia articleMedia = articleMediaCreateMapper.toEntity(articleMediaCreateDTO);

        articleMediaRepository.save(articleMedia);


        Long artclMediaId = articleMedia.getArtclMediaId();
        Integer contId = articleMedia.getContId();

        //기사영상 등록 후 생성된 아이디만 response [아이디로 다시 상세조회 api 호출.]
        ArticleMediaDTO articleMediaDTO = new ArticleMediaDTO();
        articleMediaDTO.setArtclMediaId(artclMediaId);
        articleMediaDTO.setContId(contId);


        ArticleSimpleDTO articleSimpleDTO = articleMediaCreateDTO.getArticle();
        Long artclId = articleSimpleDTO.getArtclId();
        Article article = articleService.articleFindOrFail(artclId);

        //기사 로그 등록.
        copyArticleActionLogUpdate(article, userId, "C");

        //엘라스틱 서치 등록
        elasticSearchArticleService.elasticPush(article);

        //원본기사일 경우 복사된 기사의 미디어 동기화
        updateCopyArticle(article, userId ,"C", "Create Media");

        /********** MQ [TOPIC] ************/

        //이현준 부장, 이현진 차장 요청으로 매칭하고 부조전송 완료된 미디어만 웹소켓 메세지 전송
        CueSheet cueSheet = article.getCueSheet();

        if (ObjectUtils.isEmpty(cueSheet) == false) {

            Long cueId = cueSheet.getCueId();

            Optional<CueSheet> getCueSheet = cueSheetRepository.findByCue(cueId);

            if (getCueSheet.isPresent()) {

                CueSheet cuesheetEntity = getCueSheet.get();

                Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findArticleCue(artclId);

                if (cueSheetItem.isPresent()) {

                    CueSheetItem cueSheetItemEntity = cueSheetItem.get();

                    //Article article = articleMedia.getArticle();
                    Long articleId = null;
                    if (ObjectUtils.isEmpty(article) == false) {
                        articleId = article.getArtclId();
                    }
                    /************ MQ messages *************/

                    cueSheetTopicService.sendCueTopicCreate(cueSheet, cueId, null, artclId, null, "Create Media",
                            null, "N", "N");
                }
            }
        }
        return articleMediaDTO;
    }

    public void updateCopyArticle(Article article, String userId, String action, String eventId) throws Exception {

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

                        //기사 미디어 update
                        copyarticleMediaUpdate(copyArticle, articleId, userId);

                        //기사 로그 등록.
                        copyArticleActionLogUpdate(copyArticle, userId, action);

                        Long copyArticleId = copyArticle.getArtclId();
                        Article getCopyArticle = articleService.articleFindOrFail(copyArticleId);
                        //엘라스틱서치 등록
                        elasticSearchArticleService.elasticPush(getCopyArticle);

                        Long artclId = getCopyArticle.getArtclId();
                        //MQ메세지 전송
                        //articleTopicService.articleTopic("Article Media Update", artclId);
                        //sendTopic("CopyAarticle Update", artclId);


                        /********** MQ [TOPIC] ************/

                        //이현준 부장, 이현진 차장 요청으로 매칭하고 부조전송 완료된 미디어만 웹소켓 메세지 전송
                        CueSheet cueSheet = getCopyArticle.getCueSheet();

                        if (ObjectUtils.isEmpty(cueSheet) == false) {

                            Long cueId = cueSheet.getCueId();

                            Optional<CueSheet> getCueSheet = cueSheetRepository.findByCue(cueId);

                            if (getCueSheet.isPresent()) {

                                CueSheet cuesheetEntity = getCueSheet.get();

                                Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findArticleCue(artclId);

                                if (cueSheetItem.isPresent()) {

                                    CueSheetItem cueSheetItemEntity = cueSheetItem.get();

                                    //Article article = articleMedia.getArticle();
                                    Long getArticleId = null;
                                    if (ObjectUtils.isEmpty(article) == false) {
                                        getArticleId = article.getArtclId();
                                    }
                                    /************ MQ messages *************/

                                    cueSheetTopicService.sendCueTopicCreate(cueSheet, cueId, null, getArticleId, null, eventId,
                                            null, "N", "N");
                                }
                            }
                        }

                    }
                }

            }
        }

    }

    //기사 미디어 update
    public void copyarticleMediaUpdate(Article updateArticle, Long orgArticleId, String userId) throws Exception {

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
                //추후에 T는 클라우드 콘피그 교체
                //부조전송
                if (subrmNm != null && subrmNm.trim().isEmpty() == false) {
                    lboxService.mediaTransfer(mediaId, contentId, subrmNm, "T", false, false, userId);
                }
            }
        }

    }

    //기사 수정 로그
    public void copyArticleActionLogUpdate(Article article, String userId, String action) throws Exception {


        if ("C".equals(action)) {

            //기사로그 저장
            buildArticleActionLog(ActionMesg.mediaC.getActionMesg(ActionMesg.mediaC), userId, article);
        } else if ("D".equals(action)){

            //기사로그 저장
            buildArticleActionLog(ActionMesg.mediaD.getActionMesg(ActionMesg.mediaD), userId, article);
        }


    }

    //기사로그 저장
    public void buildArticleActionLog(String actionLog, String userId, Article article) throws JsonProcessingException {

        ArticleActionLog articleActionLog = new ArticleActionLog(); //기사액션로그 저장할 엔티티

        //기사로그 빌드
        articleActionLog = ArticleActionLog.builder()
                .article(article)
                .message(actionLog)
                .action(ActionEnum.UPDATE.getAction(ActionEnum.UPDATE))
                .inputrId(userId)
                .artclInfo(marshallingJsonHelper.MarshallingJson(article)) //Json으로 기사내용저장
                .build();

        //기사로그 저장
        articleActionLogRepository.save(articleActionLog);

    }

    //큐시트 토픽 메세지 전송
    public void sendCueTopicCreate(CueSheet cueSheet, Long cueId, Long cueItemId, Long artclId, Long cueTmpltId, String eventId,
                                   String spareYn, String prompterFlag, String videoTakerFlag, Article article) throws Exception {

        Integer cueVer = 0;
        Integer cueOderVer = 0;
        if (ObjectUtils.isEmpty(cueSheet) == false) {

            cueVer = cueSheet.getCueVer();
            cueOderVer = cueSheet.getCueOderVer();

        }

        /*Long orgArtclId = article.getOrgArtclId();

        if (artclId.equals(orgArtclId) == false) {

            //토픽메세지 ArticleTopicDTO Json으로 변환후 send
            TakerCueSheetTopicDTO takerCueSheetTopicDTO = new TakerCueSheetTopicDTO();
            //모델부분은 안넣어줘도 될꺼같음.
            takerCueSheetTopicDTO.setEvent_id(eventId);
            takerCueSheetTopicDTO.setCue_id(cueId);
            takerCueSheetTopicDTO.setCue_ver(cueVer);
            takerCueSheetTopicDTO.setCue_oder_ver(cueOderVer);
            takerCueSheetTopicDTO.setCue_item_id(cueItemId); //변경된 내용 추가
            takerCueSheetTopicDTO.setArtcl_id(artclId);
            takerCueSheetTopicDTO.setCue_tmplt_id(cueTmpltId);
            takerCueSheetTopicDTO.setSpare_yn(spareYn);
            takerCueSheetTopicDTO.setPrompter(prompterFlag);
            takerCueSheetTopicDTO.setVideo_taker(videoTakerFlag);
            String interfaceJson = marshallingJsonHelper.MarshallingJson(takerCueSheetTopicDTO);

            //interface에 큐메세지 전송
            topicSendService.topicInterface(interfaceJson);
        }*/

        CueSheetWebTopicDTO cueSheetWebTopicDTO = new CueSheetWebTopicDTO();
        cueSheetWebTopicDTO.setEventId("Article Media Create");
        cueSheetWebTopicDTO.setCueId(cueId);
        cueSheetWebTopicDTO.setCueItemId(cueItemId);
        cueSheetWebTopicDTO.setArtclId(artclId);
        cueSheetWebTopicDTO.setCueVer(cueVer);
        cueSheetWebTopicDTO.setCueOderVer(cueOderVer);
        cueSheetWebTopicDTO.setSpareYn(spareYn);
        String webJson = marshallingJsonHelper.MarshallingJson(cueSheetWebTopicDTO);
        //web에 큐메세지 전송
        topicSendService.topicWeb(webJson);

    }

    public void update(ArticleMediaUpdateDTO articleMediaUpdateDTO, Long artclMediaId, String userId) {

        ArticleMedia articleMedia = articleMediaFindOrFail(artclMediaId);

        articleMediaUpdateDTO.setUpdtrId(userId);

        articleMediaUpdateMapper.updateFromDto(articleMediaUpdateDTO, articleMedia);

        articleMediaRepository.save(articleMedia);

        /**********기사 미디어 타입 계산**********/

        /*Article getArticle = articleMedia.getArticle();
        Long artclId = getArticle.getArtclId();
        Article article = articleService.articleFindOrFail(artclId);

        List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaList(artclId);

        int total = 0;

        for (ArticleMedia media : articleMediaList){

            int mediaDurtn = media.getMediaDurtn();

            total = total + mediaDurtn;
        }

        ArticleDTO articleDTO = articleMapper.toDto(article);
        articleDTO.setVideoTime(total);

        articleMapper.updateFromDto(articleDTO, article);

        articleRepository.save(article);*/

    }

    public void delete(Long artclMediaId, String userId) throws Exception {

        ArticleMedia articleMedia = articleMediaFindOrFail(artclMediaId);

        ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);

        articleMediaDTO.setDelrId(userId);
        articleMediaDTO.setDelDtm(new Date());
        articleMediaDTO.setDelYn("Y");

        articleMediaMapper.updateFromDto(articleMediaDTO, articleMedia);

        articleMediaRepository.save(articleMedia);

        /*ArticleSimpleDTO articleSimpleDTO = articleMediaDTO.getArticle();
        Long artclId = articleSimpleDTO.getArtclId();

        Article article = articleService.articleFindOrFail(artclId);

        ArticleDTO articleDTO = articleMapper.toDto(article);
        Integer orgVideoTime = articleDTO.getVideoTime();
        Integer newVideoTime = articleMedia.getMediaDurtn();
        Integer totalVideoTime = Optional.ofNullable(orgVideoTime).orElse(0) - Optional.ofNullable(newVideoTime).orElse(0);
        articleDTO.setVideoTime(totalVideoTime);

        articleMapper.updateFromDto(articleDTO, article);
        articleRepository.save(article);*/


        Article getArticle = articleMedia.getArticle();
        Long artclId = getArticle.getArtclId();
        Article article = articleService.articleFindOrFail(artclId);

        //기사 로그 등록.
        copyArticleActionLogUpdate(article, userId, "D");

        //엘라스틱 서치 등록
        elasticSearchArticleService.elasticPush(article);

        //원본기사일 경우 복사된 기사의 미디어 동기화
        updateCopyArticle(article, userId ,"D", "Delete Media");

      /*  List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaList(artclId);

        int total = 0;

        for (ArticleMedia media : articleMediaList){

            int mediaDurtn = media.getMediaDurtn();

            total = total + mediaDurtn;
        }

        ArticleDTO articleDTO = articleMapper.toDto(article);
        articleDTO.setVideoTime(total);

        articleMapper.updateFromDto(articleDTO, article);

        articleRepository.save(article);*/


        //Long artclId = articleEntity.getArtclId();
        //Article article = articleService.articleFindOrFail(artclId);

        CueSheet cueSheet = article.getCueSheet();

        if (ObjectUtils.isEmpty(cueSheet) == false) {

            Long cueId = cueSheet.getCueId();

            Optional<CueSheet> getCueSheet = cueSheetRepository.findByCue(cueId);

            if (getCueSheet.isPresent()) {

                CueSheet cuesheetEntity = getCueSheet.get();

                Optional<CueSheetItem> cueSheetItem = cueSheetItemRepository.findArticleCue(artclId);

                if (cueSheetItem.isPresent()) {

                    CueSheetItem cueSheetItemEntity = cueSheetItem.get();

                    /********** MQ [TOPIC] ************/
                    //Article article = articleMedia.getArticle();
                    Long articleId = null;
                    if (ObjectUtils.isEmpty(article) == false) {
                        articleId = article.getArtclId();
                    }


                    cueSheetTopicService.sendCueTopicCreate(cueSheet, cueId, null, artclId, null, "Delete Media",
                            null, "N", "N");

                }
            }


        }

    }

    public ArticleMedia articleMediaFindOrFail(Long artclMediaId) {

        Optional<ArticleMedia> articleMedia = articleMediaRepository.findByArticleMedia(artclMediaId);

        if (!articleMedia.isPresent()) {
            throw new ResourceNotFoundException("기사 미디어를 찾을 수 없습니다. 기사 미디어 아이디 : " + artclMediaId);
        }

        return articleMedia.get();
    }

    public BooleanBuilder getSearch(Date sdate, Date edate, String trnsfFileNm, Long artclId, String mediaTypCd) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticleMedia qArticleMedia = QArticleMedia.articleMedia;

        booleanBuilder.and(qArticleMedia.delYn.eq("N"));

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qArticleMedia.inputDtm.between(sdate, edate));
        }
        if (trnsfFileNm != null && trnsfFileNm.trim().isEmpty() == false) {
            booleanBuilder.and(qArticleMedia.trnsfFileNm.contains(trnsfFileNm));
        }
        if (ObjectUtils.isEmpty(artclId) == false) {
            booleanBuilder.and(qArticleMedia.article.artclId.eq(artclId));
        }

        if (mediaTypCd != null && mediaTypCd.trim().isEmpty() == false) {
            booleanBuilder.and(qArticleMedia.mediaTypCd.eq(mediaTypCd));
        }


        return booleanBuilder;
    }


}
