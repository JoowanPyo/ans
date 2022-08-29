package com.gemiso.zodiac.app.cueSheetMedia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItemRepository;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaCreateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaUpdateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaCreateMapper;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaMapper;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaUpdateMapper;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.topic.CueSheetTopicService;
import com.gemiso.zodiac.core.topic.TopicSendService;
import com.gemiso.zodiac.core.topic.cueSheetTopicDTO.CueSheetWebTopicDTO;
import com.gemiso.zodiac.core.topic.interfaceTopicDTO.TakerCueSheetTopicDTO;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetMediaService {

    private final CueSheetMediaRepository cueSheetMediaRepository;
    private final CueSheetItemRepository cueSheetItemRepository;

    private final CueSheetMediaMapper cueSheetMediaMapper;
    private final CueSheetMediaCreateMapper cueSheetMediaCreateMapper;
    private final CueSheetMediaUpdateMapper cueSheetMediaUpdateMapper;

    //private final UserAuthService userAuthService;

    private final TopicSendService topicSendService;
    private final CueSheetTopicService cueSheetTopicService;
    private final MarshallingJsonHelper marshallingJsonHelper;


    public List<CueSheetMediaDTO> findAll(Date sdate, Date edate, String trnsfFileNm, Long cueItemId, String mediaTypCd, String delYn) {

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, trnsfFileNm, cueItemId, mediaTypCd, delYn);

        List<CueSheetMedia> cueSheetMedia = (List<CueSheetMedia>) cueSheetMediaRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "mediaOrd"));

        List<CueSheetMediaDTO> cueSheetMediaDTOList = cueSheetMediaMapper.toDtoList(cueSheetMedia);

        return cueSheetMediaDTOList;
    }

    //목록조회 조건 빌드
    public BooleanBuilder getSearch(Date sdate, Date edate, String trnsfFileNm, Long cueItemId, String mediaTypCd, String delYn) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QCueSheetMedia qCueSheetMedia = QCueSheetMedia.cueSheetMedia;

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qCueSheetMedia.inputDtm.between(sdate, edate));
        }
        if (trnsfFileNm != null && trnsfFileNm.trim().isEmpty() == false) {
            booleanBuilder.and(qCueSheetMedia.trnsfFileNm.contains(trnsfFileNm));
        }
        if (ObjectUtils.isEmpty(cueItemId) == false) {
            booleanBuilder.and(qCueSheetMedia.cueSheetItem.cueItemId.eq(cueItemId));
        }

        if (mediaTypCd != null && mediaTypCd.trim().isEmpty() == false) {
            booleanBuilder.and(qCueSheetMedia.mediaTypCd.eq(mediaTypCd));
        }

        if (delYn != null && delYn.trim().isEmpty() == false) {
            booleanBuilder.and(qCueSheetMedia.delYn.eq(delYn));
        }


        return booleanBuilder;
    }

    public CueSheetMediaDTO find(Long cueMediaId) {

        CueSheetMedia cueSheetMedia = cueSheetMediaFindOrFail(cueMediaId);

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaMapper.toDto(cueSheetMedia);

        return cueSheetMediaDTO;
    }

    public Long create(CueSheetMediaCreateDTO cueSheetMediaCreateDTO, String userId) throws Exception {

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        cueSheetMediaCreateDTO.setInputrId(userId);

        CueSheetMedia cueSheetMedia = cueSheetMediaCreateMapper.toEntity(cueSheetMediaCreateDTO);

        cueSheetMediaRepository.save(cueSheetMedia);


        /********** MQ [TOPIC] ************/
        //이현준 부장, 이현진 차장 요청으로 매칭하고 부조전송 완료된 미디어만 웹소켓 메세지 전송
        CueSheetItem cueSheetItem = cueSheetMedia.getCueSheetItem();

        if (ObjectUtils.isEmpty(cueSheetItem) == false) {

            Long cueItemId = cueSheetItem.getCueItemId();

            Optional<CueSheetItem> cueSheetItemEntity = cueSheetItemRepository.findByCueItem(cueItemId);

            if (cueSheetItemEntity.isPresent()) {

                CueSheetItem getCueSheetItem = cueSheetItemEntity.get();
                String spareYn = getCueSheetItem.getSpareYn();

                CueSheet cueSheet = getCueSheetItem.getCueSheet();

                Long cueId = 0L;
                if (ObjectUtils.isEmpty(cueSheet) == false) {
                    cueId = cueSheet.getCueId();

                }


                cueSheetTopicService.sendMediTopicCreate(cueSheet, cueId, cueItemId, null, null, "Create Media",
                        spareYn, "N", "N");

            }
        }

        return cueSheetMedia.getCueMediaId();
    }

    public void update(CueSheetMediaUpdateDTO cueSheetMediaUpdateDTO, Long cueMediaId, String userId) {

        CueSheetMedia cueSheetMedia = cueSheetMediaFindOrFail(cueMediaId);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        cueSheetMediaUpdateDTO.setUpdtrId(userId);

        cueSheetMediaUpdateMapper.updateFromDto(cueSheetMediaUpdateDTO, cueSheetMedia);

        cueSheetMediaRepository.save(cueSheetMedia);

    }

    public void delete(Long cueMediaId, String userId) throws JsonProcessingException {

        CueSheetMedia cueSheetMedia = cueSheetMediaFindOrFail(cueMediaId);

        CueSheetMediaDTO cueSheetMediaDTO = cueSheetMediaMapper.toDto(cueSheetMedia);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        //String userId = userAuthService.authUser.getUserId();
        cueSheetMediaDTO.setDelrId(userId);
        cueSheetMediaDTO.setDelDtm(new Date());
        cueSheetMediaDTO.setDelYn("Y");

        cueSheetMediaMapper.updateFromDto(cueSheetMediaDTO, cueSheetMedia);

        cueSheetMediaRepository.save(cueSheetMedia);

        CueSheetItem cueSheetItem = cueSheetMedia.getCueSheetItem();

        if (ObjectUtils.isEmpty(cueSheetItem) == false) {

            Long cueItemId = cueSheetItem.getCueItemId();

            Optional<CueSheetItem> cueSheetItemEntity = cueSheetItemRepository.findByCueItem(cueItemId);

            if (cueSheetItemEntity.isPresent()) {

                CueSheetItem getCueSheetItem = cueSheetItemEntity.get();
                String spareYn = getCueSheetItem.getSpareYn();

                CueSheet cueSheet = getCueSheetItem.getCueSheet();
                Long cueId = 0L;
                if (ObjectUtils.isEmpty(cueSheet) == false) {
                    cueId = cueSheet.getCueId();

                }


                sendCueTopicCreate(cueSheet, cueId, cueItemId, 0L, null, "Delete Media",
                        spareYn, "N", "Y", null);

            }
        }

    }

    public CueSheetMedia cueSheetMediaFindOrFail(Long cueMediaId) {

        Optional<CueSheetMedia> cueSheetMedia = cueSheetMediaRepository.findByCueSheetMedia(cueMediaId);

        if (!cueSheetMedia.isPresent()) {
            throw new ResourceNotFoundException("큐시트 영상을 찾을 수 없습니다. 큐시트 영상 아이디 : " + cueMediaId);
        }

        return cueSheetMedia.get();

    }

    //큐시트 토픽 메세지 전송
    public void sendCueTopicCreate(CueSheet cueSheet, Long cueId, Long cueItemId, Long artclId, Long cueTmpltId, String eventId,
                                   String spareYn, String prompterFlag, String videoTakerFlag, Article article) throws JsonProcessingException {

        try {

            Integer cueVer = 0;
            Integer cueOderVer = 0;
            if (ObjectUtils.isEmpty(cueSheet) == false) {

                cueVer = cueSheet.getCueVer();
                cueOderVer = cueSheet.getCueOderVer();

            }

            Long orgArtclId = null;
            if (ObjectUtils.isEmpty(article) == false) {
                orgArtclId = article.getOrgArtclId();
            }

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
            }

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

        } catch (IOException | TimeoutException e){

            log.error("CueSheetMedia Topic Errer : CueSheet - " + cueSheet.toString() + " CueItemId - " + cueItemId + " Message - " + eventId + "  Topic Error");

        }
    }


}
