package com.gemiso.zodiac.app.elasticsearch.articleEntity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

import javax.persistence.Column;
import javax.persistence.Embedded;
import java.util.List;
import java.util.Objects;

//ans_article_prod 운영
//@TypeAlias("ans_article")
@Data
@Document(indexName = "ans_article", writeTypeHint = WriteTypeHint.FALSE)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticSearchArticle {


    @Field(type = FieldType.Text)
    private String ancMentCtt;

    @Field(type = FieldType.Keyword)
    private String apprvDivCd;

    @Field(type = FieldType.Keyword)
    private String apprvDivCdNm;

    @Field(type = FieldType.Keyword)
    private String artclCateCd;

    @Field(type = FieldType.Keyword)
    private String artclCateCdNm;

    @Field(type = FieldType.Keyword)
    private String artclDivCd;

    @Id
    @Field(type = FieldType.Long)
    private Long artclId;

    @Field(type = FieldType.Integer)
    private Integer artclOrd;

    @Field(type = FieldType.Text)
    private String artclTitl;

    @Field(type = FieldType.Text)
    private String artclTitlEn;

    @Field(type = FieldType.Keyword)
    private String artclTypCd;

    @Field(type = FieldType.Keyword)
    private String artclTypCdNm;

    @Field(type = FieldType.Keyword)
    private String artclTypDtlCd;

    @Field(type = FieldType.Keyword)
    private String artclTypDtlCdNm;

    @Field(type = FieldType.Text)
    private String artclCtt;

    @Field(type = FieldType.Keyword)
    private String brdcPgmId;

    @Embedded
    private ElasticSearchCueSheet cueSheet;

    @Field(type = FieldType.Keyword)
    private String delYn;

    @Field(type = FieldType.Keyword)
    private Long deptCd;

    @Field(type = FieldType.Keyword)
    private String deptNm;

    @Field(type = FieldType.Keyword)
    private String embgYn;

    @Field(type = FieldType.Date)
    private String inputDtm;

    @Field(type = FieldType.Keyword)
    private String inputrId;

    @Field(type = FieldType.Keyword)
    private String inputrNm;

    @Field(type = FieldType.Keyword)
    private String lckYn;

    @Field(type = FieldType.Integer)
    private Long orgArtclId;

    @Field(type = FieldType.Keyword)
    private String rptrId;

    @Field(type = FieldType.Keyword)
    private String rptrNm;

    @Field(type = FieldType.Integer)
    private Integer ancMentCttTime;

    @Field(type = FieldType.Integer)
    private Integer artclCttTime;

    @Field(type = FieldType.Integer)
    private Integer artclExtTime;

    @Field(type = FieldType.Keyword)
    private String editorFixUser;

    @Field(type = FieldType.Keyword)
    private String editorFixUserNm;

    @Field(type = FieldType.Nested)
    private List<ElasticSearchArticleTags> tags;

    @Field(type = FieldType.Nested)
    private List<ElasricSearchArticleMedia> articleMedias;



    @PersistenceConstructor
    @Builder
    public ElasticSearchArticle(String ancMentCtt,
                                String apprvDivCd,
                                String apprvDivCdNm,
                                String artclCateCd,
                                String artclCateCdNm,
                                String artclDivCd,
                                Long artclId,
                                Integer artclOrd,
                                String artclTitl,
                                String artclTitlEn,
                                String artclTypCd,
                                String artclTypCdNm,
                                String artclTypDtlCd,
                                String artclTypDtlCdNm,
                                String artclCtt,
                                String brdcPgmId,
                                ElasticSearchCueSheet cueSheet,
                                String delYn,
                                Long deptCd,
                                String deptNm,
                                String embgYn,
                                String inputDtm,
                                String inputrId,
                                String inputrNm,
                                String lckYn,
                                Long orgArtclId,
                                String rptrId,
                                String rptrNm,
                                String brdcPgmNm,
                                Long cueId,
                                Long subrmId,
                                Integer ancMentCttTime,
                                Integer artclCttTime,
                                Integer artclExtTime,
                                String editorFixUser,
                                String editorFixUserNm,
                                List<ElasticSearchArticleTags> tags,
                                List<ElasricSearchArticleMedia> articleMedias){

        this.ancMentCtt = ancMentCtt;
        this.apprvDivCd = apprvDivCd;
        this.apprvDivCdNm = apprvDivCdNm;
        this.artclCateCd = artclCateCd;
        this.artclCateCdNm = artclCateCdNm;
        this.artclDivCd = artclDivCd;
        this.artclId = artclId;
        this.artclOrd = artclOrd;
        this.artclTitl = artclTitl;
        this.artclTitlEn = artclTitlEn;
        this.artclTypCd = artclTypCd;
        this.artclTypCdNm = artclTypCdNm;
        this.artclTypDtlCd = artclTypDtlCd;
        this.artclTypDtlCdNm = artclTypDtlCdNm;
        this.artclCtt = artclCtt;
        this.brdcPgmId = brdcPgmId;
        if(Objects.nonNull(cueId)) {
            this.cueSheet = new ElasticSearchCueSheet(brdcPgmNm, cueId, subrmId);
        }
        this.delYn = delYn;
        this.deptCd = deptCd;
        this.deptNm = deptNm;
        this.embgYn = embgYn;
        this.inputDtm = inputDtm;
        this.inputrId = inputrId;
        this.inputrNm = inputrNm;
        this.lckYn = lckYn;
        this.orgArtclId = orgArtclId;
        this.rptrId = rptrId;
        this.rptrNm = rptrNm;
        this.ancMentCttTime = ancMentCttTime;
        this.artclCttTime = artclCttTime;
        this.artclExtTime = artclExtTime;
        this.editorFixUser = editorFixUser;
        this.editorFixUserNm = editorFixUserNm;
        this.tags = tags;
        this.articleMedias = articleMedias;
    }

   /* @PersistenceConstructor
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


    }*/
}

