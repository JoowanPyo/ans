package com.gemiso.zodiac.app.elasticsearch.articleEntity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Embeddable;
import javax.persistence.Id;

@Data
@Document(indexName = "ans_article_tag")
@Embeddable
@NoArgsConstructor
public class ElasticSearchArticleTags {

    @Id
    @Field(type = FieldType.Long)
    private Long tagId;

    @Field(type = FieldType.Text)
    private String tag;

}
