package com.gemiso.zodiac.app.article;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ArticleSerializer extends JsonSerializer<Article> {

    @Override
    public void serialize(Article value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {

    }
}
