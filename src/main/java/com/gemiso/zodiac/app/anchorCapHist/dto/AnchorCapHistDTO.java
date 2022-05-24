package com.gemiso.zodiac.app.anchorCapHist.dto;

import com.gemiso.zodiac.app.articleHist.dto.ArticleHistSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnchorCapHistDTO {

    private Long ancCapHistId;
    private int lnNo;
    private Long capTmpltId;
    private String capCtt;
    private String capRmk;
    private String symbolId;
    private String capDivCd;
    private Integer lnOrd;
    private ArticleHistSimpleDTO articleHist;
}
