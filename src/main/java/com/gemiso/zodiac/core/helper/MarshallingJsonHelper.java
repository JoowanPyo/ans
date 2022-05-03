package com.gemiso.zodiac.core.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MarshallingJsonHelper {


    public String MarshallingJson(Object object) throws JsonProcessingException {

        //TokenBuffer buffer = new TokenBuffer(null);

        ObjectMapper mapper = new ObjectMapper();

        //SimpleModule simpleModule = new SimpleModule();

        //mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);

        // ArticleTopicDTO 클래스는 ArticleTopicDTOSerializer Serialize 하겠다는 의지의 표현.
        //simpleModule.addSerializer(ArticleTopicDTO.class, new ArticleTopicDTOSerializer());

        //mapper.registerModule(simpleModule);
        //mapper.writeValue(buffer, articleTopicDTO);
        //JsonNode root = mapper.readTree(buffer.asParser());

        //String jsonInString = mapper.writeValueAsString(articleTopicDTO);

        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        String jsonInString = mapper.writeValueAsString(object);

        return jsonInString;
    }

}
