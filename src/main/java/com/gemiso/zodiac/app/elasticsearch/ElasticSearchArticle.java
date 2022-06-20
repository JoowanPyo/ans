package com.gemiso.zodiac.app.elasticsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import javax.persistence.Embedded;
import java.util.Date;
import java.util.Objects;

//@TypeAlias("ans_article")
@Data
@Document(indexName = "ans_article", writeTypeHint = WriteTypeHint.FALSE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticSearchArticle {

    @Id
    @Field(type = FieldType.Long)
    private Long artclId;

    @Field(type = FieldType.Keyword)
    private String apprvDivCd;

    @Field(type = FieldType.Keyword)
    private String artclCateCd;

    @Field(type = FieldType.Keyword)
    private String artclDivCd;

    @Field(type = FieldType.Integer)
    private Integer artclOrd;

    @Field(type = FieldType.Text)
    private String artclTitl;

    @Field(type = FieldType.Text)
    private String artclTitlEn;

    @Field(type = FieldType.Keyword)
    private String artclTypCd;

    @Field(type = FieldType.Keyword)
    private String artclTypDtlCd;

    @Field(type = FieldType.Keyword)
    private String brdcPgmId;

    @Embedded
    private ElasticSearchCueSheet cueSheet;

    @Field(type = FieldType.Keyword)
    private String delYn;

    @Field(type = FieldType.Integer)
    private Integer deptCd;

    @Field(type = FieldType.Keyword)
    private String embgYn;

    @Field(type = FieldType.Date)
    private String inputDtm;

    @Field(type = FieldType.Keyword)
    private String inputrId;

    @Field(type = FieldType.Keyword)
    private String lckYn;

    @Field(type = FieldType.Integer)
    private Long orgArtclId;

    @PersistenceConstructor
    @Builder
    public ElasticSearchArticle(String apprvDivCd,
                                String artclCateCd,
                                String artclDivCd,
                                Long artclId,
                                Integer artclOrd,
                                String artclTitl,
                                String artclTitlEn,
                                String artclTypCd,
                                String artclTypDtlCd,
                                String brdcPgmId,
                                String brdcPgmNm,
                                Long cueId,
                                Long subrmId,
                                String delYn,
                                Integer deptCd,
                                String embgYn,
                                String inputDtm,
                                String inputrId,
                                String lckYn,
                                Long orgArtclId) {
        this.apprvDivCd = apprvDivCd;
        this.artclCateCd = artclCateCd;
        this.artclDivCd = artclDivCd;
        this.artclId = artclId;
        this.artclOrd = artclOrd;
        this.artclTitl = artclTitl;
        this.artclTitlEn = artclTitlEn;
        this.artclTypCd = artclTypCd;
        this.artclTypDtlCd = artclTypDtlCd;
        this.brdcPgmId = brdcPgmId;
        if(Objects.nonNull(cueId)) {
            this.cueSheet = new ElasticSearchCueSheet(brdcPgmNm, cueId, subrmId);
        }
        this.delYn = delYn;
        this.deptCd = deptCd;
        this.embgYn = embgYn;
        this.inputDtm = inputDtm;
        this.inputrId = inputrId;
        this.lckYn = lckYn;
        this.orgArtclId = orgArtclId;


    }
}

