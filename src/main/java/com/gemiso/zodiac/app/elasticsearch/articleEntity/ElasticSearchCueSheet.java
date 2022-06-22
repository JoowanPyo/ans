package com.gemiso.zodiac.app.elasticsearch.articleEntity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Embeddable;

@Data
@Document(indexName = "ans_cueSheet")
@Embeddable
@NoArgsConstructor
public class ElasticSearchCueSheet {

    @Field(type = FieldType.Long)
    private Long cueId;

    @Field(type = FieldType.Keyword)
    private String brdcPgmNm;

    @Field(type = FieldType.Integer)
    private Long subrmId;

    public ElasticSearchCueSheet(String brdcPgmNm, Long cueId, Long subrmId) {
        this.brdcPgmNm = brdcPgmNm;
        this.cueId = cueId;
        this.subrmId = subrmId;
    }
}
