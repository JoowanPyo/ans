package com.gemiso.zodiac.app.articleCap.dto;

import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCapUpdateDTO {

    private Long artclCapId;
    private String capDivCd;
    //private String capDivCdNm;
    private int lnNo;
    private String capCtt;
    private String capRmk;
    private Long articleId;
    private Long capTmpltId;
    private Long symbolId;
}
