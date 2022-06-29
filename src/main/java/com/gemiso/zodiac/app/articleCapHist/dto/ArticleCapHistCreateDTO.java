package com.gemiso.zodiac.app.articleCapHist.dto;

import com.gemiso.zodiac.app.articleHist.dto.ArticleHistSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleCapHistCreateDTO {

    //private Long artclCapHistId;
    private int lnNo;
    private Long capTmpltId;
    private String capCtt;
    private String capRmk;
    private String symbolId;
    private String capDivCd;
    private Integer lnOrd;
    private ArticleHistSimpleDTO articleHist;
}
