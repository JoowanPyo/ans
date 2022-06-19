package com.gemiso.zodiac.app.articleMedia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleRepository;
import com.gemiso.zodiac.app.article.ArticleService;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.article.mapper.ArticleMapper;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaCreateDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaUpdateDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaCreateMapper;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaMapper;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaUpdateMapper;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetRepository;
import com.gemiso.zodiac.app.cueSheet.CueSheetService;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemService;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.topic.TopicSendService;
import com.gemiso.zodiac.core.topic.interfaceTopicDTO.TakerCueSheetTopicDTO;
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

    private final ArticleMediaMapper articleMediaMapper;
    private final ArticleMediaCreateMapper articleMediaCreateMapper;
    private final ArticleMediaUpdateMapper articleMediaUpdateMapper;
    private final ArticleMapper articleMapper;

    private final ArticleService articleService;
    private final CueSheetItemService cueSheetItemService;
    private final CueSheetService cueSheetService;

    //private final UserAuthService userAuthService;

    private final TopicSendService topicSendService;

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

    public ArticleMediaDTO create(ArticleMediaCreateDTO articleMediaCreateDTO, String userId) throws JsonProcessingException {


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

                    String mediaTypCd = articleMediaCreateDTO.getMediaTypCd();


                    if ("media_typ_001".equals(mediaTypCd)) {

                        sendCueTopicCreate(cuesheetEntity, cuesheetEntity.getCueId(), cueSheetItemEntity.getCueItemId(), articleId, null, "Article Media Create",
                                cueSheetItemEntity.getSpareYn(), "Y", "Y", article);
                    }
                }
            }


        }

        return articleMediaDTO;

    }

    //큐시트 토픽 메세지 전송
    public void sendCueTopicCreate(CueSheet cueSheet, Long cueId, Long cueItemId, Long artclId, Long cueTmpltId, String eventId,
                                   String spareYn, String prompterFlag, String videoTakerFlag, Article article) throws JsonProcessingException {

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

    }

    public void delete(Long artclMediaId, String userId) throws JsonProcessingException {

        ArticleMedia articleMedia = articleMediaFindOrFail(artclMediaId);

        ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);

        articleMediaDTO.setDelrId(userId);
        articleMediaDTO.setDelDtm(new Date());
        articleMediaDTO.setDelYn("Y");

        articleMediaMapper.updateFromDto(articleMediaDTO, articleMedia);

        articleMediaRepository.save(articleMedia);

        ArticleSimpleDTO articleSimpleDTO = articleMediaDTO.getArticle();
        Long artclId = articleSimpleDTO.getArtclId();

        Article article = articleService.articleFindOrFail(artclId);

        ArticleDTO articleDTO = articleMapper.toDto(article);
        Integer orgVideoTime = articleDTO.getVideoTime();
        Integer newVideoTime = articleMedia.getMediaDurtn();
        Integer totalVideoTime = Optional.ofNullable(orgVideoTime).orElse(0) - Optional.ofNullable(newVideoTime).orElse(0);
        articleDTO.setVideoTime(totalVideoTime);

        articleMapper.updateFromDto(articleDTO, article);
        articleRepository.save(article);




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

                    String mediaTypCd = articleMedia.getMediaTypCd();


                    if ("media_typ_001".equals(mediaTypCd)) {

                        sendCueTopicCreate(cuesheetEntity, cuesheetEntity.getCueId(), cueSheetItemEntity.getCueItemId(), articleId, null, "Article Media Delete",
                                cueSheetItemEntity.getSpareYn(), "N", "Y", article);
                    }
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
