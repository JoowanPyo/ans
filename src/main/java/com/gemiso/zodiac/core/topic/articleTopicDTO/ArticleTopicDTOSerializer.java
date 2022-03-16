package com.gemiso.zodiac.core.topic.articleTopicDTO;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ArticleTopicDTOSerializer extends JsonSerializer<ArticleTopicDTO> {

    @Override
    public void serialize(ArticleTopicDTO value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {


        gen.writeStartObject();

        gen.writeFieldName("eventId");
        gen.writeString(String.valueOf(value.getEventId()));

        gen.writeFieldName("artclId");
        gen.writeString(String.valueOf(value.getArtclId()));

        gen.writeFieldName("article");
        gen.writeString(String.valueOf(value.getArticle()));

        gen.writeEndObject();
    }
}
