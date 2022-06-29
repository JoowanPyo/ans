package com.gemiso.zodiac.app.elasticsearch.articleDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Schema(description = "엘라스틱 기사 articleDTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ElasticSearchArticleDTO {

    private String ancMentCtt;
    private String apprvDivCd;
    private String apprvDivCdNm;
    private String artclCateCd;
    private String artclCateCdNm;
    private String artclDivCd;
    private Long artclId;
    private Integer artclOrd;
    private String artclTitl;
    private String artclTitlEn;
    private String artclTypCd;
    private String artclTypCdNm;
    private String artclTypDtlCd;
    private String artclTypDtlCdNm;
    private String artclCtt;
    private String brdcPgmId;
    private ElasticSearchCueSheetDTO cueSheet;
    private String delYn;
    private Integer deptCd;
    private String deptNm;
    private String embgYn;
    private String inputDtm;
    private String inputrId;
    private String inputrNm;
    private String lckYn;

    private String lckrId;
    private String lckrNm;
    private Date lckDtm;

    private Long orgArtclId;
    private String rptrId;
    private String rptrNm;
    private Integer ancMentCttTime;
    private Integer artclCttTime;
    private Integer artclExtTime;
    private String editorFixUser;
    private String editorFixUserNm;
    private List<ElasticSearchArticleTagsDTO> tags;
    private List<ElasricSearchArticleMediaDTO> articleMedias;

}
