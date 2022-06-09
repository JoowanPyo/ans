package com.gemiso.zodiac.core.topic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.topic.cueSheetTopicDTO.CueSheetTakerTopicDTO;
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
        //topicSendService.topicWeb(webJson);

    }

    //큐시트 토픽 메세지 전송
    public void sendCueTopic(CueSheet cueSheet, String eventId, Object object) throws JsonProcessingException {

        Long cueId = cueSheet.getCueId();

        //토픽메세지 ArticleTopicDTO Json으로 변환후 send
        CueSheetTakerTopicDTO cueSheetTakerTopicDTO = new CueSheetTakerTopicDTO();
        //모델부분은 안넣어줘도 될꺼같음.
        cueSheetTakerTopicDTO.setEventId(eventId);
        cueSheetTakerTopicDTO.setCueId(cueId);
        cueSheetTakerTopicDTO.setCueVer(cueSheet.getCueVer());
        //takerCueSheetTopicDTO.setCueSheet(object);
        String json = marshallingJsonHelper.MarshallingJson(cueSheetTakerTopicDTO);


        //interface에 큐메세지 전송
        topicSendService.topicInterface(json);
        //web에 큐메세지 전송
        //topicSendService.topicWeb(json);

    }
}
