package com.gemiso.zodiac.app.articleMedia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaCreateDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaUpdateDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaCreateMapper;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaMapper;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaUpdateMapper;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.core.topic.TopicService;
import com.gemiso.zodiac.core.topic.articleTopicDTO.TakerCueSheetTopicDTO;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleMediaService {

    private final ArticleMediaRepository articleMediaRepository;

    private final ArticleMediaMapper articleMediaMapper;
    private final ArticleMediaCreateMapper articleMediaCreateMapper;
    private final ArticleMediaUpdateMapper articleMediaUpdateMapper;

    private final UserAuthService userAuthService;

    private final TopicService topicService;

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

    public Long create(ArticleMediaCreateDTO articleMediaCreateDTO) throws JsonProcessingException {


        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        articleMediaCreateDTO.setInputrId(userId);

        ArticleMedia articleMedia = articleMediaCreateMapper.toEntity(articleMediaCreateDTO);

        articleMediaRepository.save(articleMedia);

        /********** MQ [TOPIC] ************/
        Article article = articleMedia.getArticle();
        Long articleId = null;
        if (ObjectUtils.isEmpty(article) == false){
            articleId = article.getArtclId();
        }

        sendCueTopicCreate(null, null, null , articleId, null, "Article Media Create",
                null, "Y", "Y");

        return articleMedia.getArtclMediaId();

    }

    //큐시트 토픽 메세지 전송
    public void sendCueTopicCreate(CueSheet cueSheet, Long cueId, Long cueItemId, Long artclId, Long cueTmpltId, String eventId,
                                   String spareYn, String prompterFlag, String videoTakerFlag) throws JsonProcessingException {

        Integer cueVer = 0;
        Integer cueOderVer = 0;
        if (ObjectUtils.isEmpty(cueSheet) == false){

            cueVer = cueSheet.getCueVer();
            cueOderVer = cueSheet.getCueOderVer();

        }

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
        String json = marshallingJsonHelper.MarshallingJson(takerCueSheetTopicDTO);

        //interface에 큐메세지 전송
        topicService.topicInterface(json);
        //web에 큐메세지 전송
        topicService.topicWeb(json);

    }

    public void update(ArticleMediaUpdateDTO articleMediaUpdateDTO, Long artclMediaId) {

        ArticleMedia articleMedia = articleMediaFindOrFail(artclMediaId);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        articleMediaUpdateDTO.setUpdtrId(userId);

        articleMediaUpdateMapper.updateFromDto(articleMediaUpdateDTO, articleMedia);

        articleMediaRepository.save(articleMedia);

    }

    public void delete(Long artclMediaId) {

        ArticleMedia articleMedia = articleMediaFindOrFail(artclMediaId);

        ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        articleMediaDTO.setDelrId(userId);
        articleMediaDTO.setDelDtm(new Date());
        articleMediaDTO.setDelYn("Y");

        articleMediaMapper.updateFromDto(articleMediaDTO, articleMedia);

        articleMediaRepository.save(articleMedia);

    }

    public ArticleMedia articleMediaFindOrFail(Long artclMediaId) {

        Optional<ArticleMedia> articleMedia = articleMediaRepository.findByArticleMedia(artclMediaId);

        if (!articleMedia.isPresent()) {
            throw new ResourceNotFoundException("ArticleMediaId not found. ArticleMediaId : " + artclMediaId);
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
        if (ObjectUtils.isEmpty(artclId) == false){
            booleanBuilder.and(qArticleMedia.article.artclId.eq(artclId));
        }

        if (mediaTypCd != null && mediaTypCd.trim().isEmpty() == false){
            booleanBuilder.and(qArticleMedia.mediaTypCd.eq(mediaTypCd));
        }


        return booleanBuilder;
    }


}
