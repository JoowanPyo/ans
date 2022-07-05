package com.gemiso.zodiac.core.topic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.topic.cueSheetTopicDTO.CueSheetTakerTopicDTO;
import com.gemiso.zodiac.core.topic.cueSheetTopicDTO.CueSheetUnlockTopicDTO;
import com.gemiso.zodiac.core.topic.cueSheetTopicDTO.CueSheetWebTopicDTO;
import com.gemiso.zodiac.core.topic.interfaceTopicDTO.TakerCueSheetTopicDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CueSheetTopicService {

    private final TopicSendService topicSendService;

    private final MarshallingJsonHelper marshallingJsonHelper;
    //큐시트 토픽 메세지 전송
    public void sendCueTopicCreate(CueSheet cueSheet, Long cueId, Long cueItemId, Long artclId, Long cueTmpltId, String eventId,
                                   String spareYn, String prompterFlag, String videoTakerFlag) throws Exception {

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

    }

    //미디어
    public void sendMediTopicCreate(CueSheet cueSheet, Long cueId, Long cueItemId, Long artclId, Long cueTmpltId, String eventId,
                                   String spareYn, String prompterFlag, String videoTakerFlag) throws Exception {


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

    }

    //큐시트 토픽 메세지 전송
    public void sendCueTopic(CueSheet cueSheet, String eventId, Object object) throws Exception {

        Long cueId = cueSheet.getCueId();

        //토픽메세지 ArticleTopicDTO Json으로 변환후 send
        TakerCueSheetTopicDTO cueSheetTakerTopicDTO = new TakerCueSheetTopicDTO();
        //모델부분은 안넣어줘도 될꺼같음.
        cueSheetTakerTopicDTO.setEvent_id(eventId);
        cueSheetTakerTopicDTO.setCue_id(cueId);
        //cueSheetTakerTopicDTO.setCueVer(cueSheet.getCueVer());
        //takerCueSheetTopicDTO.setCueSheet(object);
        String json = marshallingJsonHelper.MarshallingJson(cueSheetTakerTopicDTO);


        //interface에 큐메세지 전송
        topicSendService.topicInterface(json);
        //web에 큐메세지 전송
        //topicSendService.topicWeb(json);

    }

    //언락
    public void sendUnLockTopic(String eventId, Long cueId, String userId, String userNm) throws Exception {


        CueSheetUnlockTopicDTO cueSheetUnlockTopicDTO = new CueSheetUnlockTopicDTO();
        cueSheetUnlockTopicDTO.setMsg(eventId);
        cueSheetUnlockTopicDTO.setCueId(cueId);
        cueSheetUnlockTopicDTO.setUserId(userId);
        cueSheetUnlockTopicDTO.setUserNm(userNm);


        String webJson = marshallingJsonHelper.MarshallingJson(cueSheetUnlockTopicDTO);
        //web에 큐메세지 전송
        topicSendService.topicWeb(webJson);

    }
}
