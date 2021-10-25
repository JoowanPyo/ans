package com.gemiso.zodiac.app.articleCap.dto;

import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateDTO;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCapSimpleDTO {

    private Long artclCapId;
    private String capDivCd;
    private String chDivCdNm;
    private int lnNo;
    private String capCtt;
    private String capRmk;
    //private ArticleDTO article;
    private CapTemplateDTO capTemplate;
    private SymbolDTO symbol;
}
