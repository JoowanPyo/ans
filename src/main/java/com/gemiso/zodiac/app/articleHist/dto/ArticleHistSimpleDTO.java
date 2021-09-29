package com.gemiso.zodiac.app.articleHist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleHistSimpleDTO {

    private Long artclHistId;
    private String chDivCd;
    private String artclTitl;
    private String artclTitlEn;
    private String artclCtt;
    private String ancMentCtt;
    private int artclOrd;
    private Long orgArtclId;
    private Date inputDtm;
    private int ver;
    //private ArticleDTO article;
}
