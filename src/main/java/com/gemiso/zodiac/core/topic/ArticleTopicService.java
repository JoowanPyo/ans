package com.gemiso.zodiac.core.topic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.topic.articleTopicDTO.ArticleLockTopicDTO;
import com.gemiso.zodiac.core.topic.articleTopicDTO.ArticleTopicDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleTopicService {

    private final TopicSendService topicSendService;

    private final MarshallingJsonHelper marshallingJsonHelper;


    public void articleCreateSendTopic(Long articleId) throws JsonProcessingException {

        ArticleTopicDTO articleTopicDTO = new ArticleTopicDTO();
        articleTopicDTO.setEventId("AC");
        //이부분은 안보내줘도 될듯
        articleTopicDTO.setArtclId(articleId);
        String json = marshallingJsonHelper.MarshallingJson(articleTopicDTO);
        //String json = marshallingJson(articleTopicDTO);
        //System.out.println(json);

        topicSendService.topicWeb(json);

    }

    public void articleTopic(String eventId, Long artclId) throws JsonProcessingException {

        //토픽메세지 ArticleTopicDTO Json으로 변환후 send
        ArticleTopicDTO articleTopicDTO = new ArticleTopicDTO();
        articleTopicDTO.setEventId(eventId);
        articleTopicDTO.setArtclId(artclId);
        String json = marshallingJsonHelper.MarshallingJson(articleTopicDTO);

        topicSendService.topicWeb(json);
    }

    public void articleLockTopic(String eventId, Long artclId, String msg, Date lckDtm, String lckrId, String lckrNm) throws JsonProcessingException {

        ArticleLockTopicDTO articleLockTopicDTO = new ArticleLockTopicDTO();

        articleLockTopicDTO.setEventId(eventId);
        articleLockTopicDTO.setArtclId(artclId);
        articleLockTopicDTO.setMsg(msg);
        articleLockTopicDTO.setLckDtm(lckDtm);
        articleLockTopicDTO.setLckrId(lckrId);
        articleLockTopicDTO.setLckrNm(lckrNm);

        //토픽메세지 ArticleTopicDTO Json으로 변환후 send

        String json = marshallingJsonHelper.MarshallingJson(articleLockTopicDTO);

        topicSendService.topicWeb(json);
    }
}
