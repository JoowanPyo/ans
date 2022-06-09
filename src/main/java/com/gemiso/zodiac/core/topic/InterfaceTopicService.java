package com.gemiso.zodiac.core.topic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.app.appInterface.takerProgramDTO.ParentProgramDTO;
import com.gemiso.zodiac.app.appInterface.takerUpdateDTO.TakerToCueBodyDTO;
import com.gemiso.zodiac.app.appInterface.takerUpdateDTO.TakerToCueBodyDataDTO;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.topic.articleTopicDTO.ArticleTopicDTO;
import com.gemiso.zodiac.core.topic.interfaceTopicDTO.TakerToCueArrayBodyDTO;
import com.gemiso.zodiac.core.topic.interfaceTopicDTO.TakerToCueTopicArrayDTO;
import com.gemiso.zodiac.core.topic.interfaceTopicDTO.TakerToCueTopicDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class InterfaceTopicService {

    private final TopicSendService topicSendService;

    private final MarshallingJsonHelper marshallingJsonHelper;


    //큐시트 아이템 상태 업데이트 [Taker to web]
    //테이커에서 플레이, 선택 정보를 웹으로 전달 웹은 이를 표시
    public void takerStatusUpdate(TakerToCueBodyDTO takerToCueBodyDTO) throws JsonProcessingException {

        List<TakerToCueBodyDataDTO> bodyList = takerToCueBodyDTO.getBody();
        Long cueId = takerToCueBodyDTO.getCue_id();

        TakerToCueTopicArrayDTO takerToCueTopicArrayDTO = new TakerToCueTopicArrayDTO();

        //topic 메세지 header 생성
        TakerToCueTopicDTO header = new TakerToCueTopicDTO();
        takerToCueTopicArrayDTO.setEventId("CueSheetItem Start From The Taker");
        takerToCueTopicArrayDTO.setCueId(cueId);

        //topic 메세지 body 생성
        List<TakerToCueArrayBodyDTO> newBodyList = new ArrayList<>();

        for (TakerToCueBodyDataDTO body : bodyList ){

            TakerToCueArrayBodyDTO takerToCueArrayBodyDTO = new TakerToCueArrayBodyDTO();
            takerToCueArrayBodyDTO.setCueItemId(body.getRd_id());
            takerToCueArrayBodyDTO.setStatus(body.getStatus());

            newBodyList.add(takerToCueArrayBodyDTO);
        }

        takerToCueTopicArrayDTO.setBody(newBodyList);

        String json = marshallingJsonHelper.MarshallingJson(takerToCueTopicArrayDTO);

        topicSendService.topicWeb(json);

    }

    //테이커에서 방송상태 업데이트로 들어온 정보를 web에 전송
    public void cueStatusUpdate(Long cueId, String cueStCd) throws JsonProcessingException {


        //클로아이언트로 MQ메세지 전송
        TakerToCueTopicDTO takerToCueTopicDTO = new TakerToCueTopicDTO();
        takerToCueTopicDTO.setEventId("CueSheet cueStCd "+cueStCd+" update");
        //takerToCueTopicDTO.setCueItemId(rdId);
        takerToCueTopicDTO.setCueId(cueId);

        String json = marshallingJsonHelper.MarshallingJson(takerToCueTopicDTO);

        topicSendService.topicWeb(json);

    }

    //기사 잠금시 테이커전송
    public void articleLock(String eventId , Long artclId) throws JsonProcessingException {

        //토픽메세지 ArticleTopicDTO Json으로 변환후 send
        ArticleTopicDTO articleTopicDTO = new ArticleTopicDTO();
        articleTopicDTO.setEventId(eventId);
        articleTopicDTO.setArtclId(artclId);
        //모델부분은 안넣어줘도 될꺼같음.
        //articleTopicDTO.setArticle(articleLockDTO);
        String json = marshallingJsonHelper.MarshallingJson(articleTopicDTO);

        topicSendService.topicInterface(json);

    }
}
