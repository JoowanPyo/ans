package com.gemiso.zodiac.app.articleCap.dto;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateDTO;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnchorCapSimpleDTO {

    private Long artclCapId;
    private String capDivCd;
    private String capDivCdNm;
    private int lnNo;
    private String capCtt;
    private String capRmk;
    //private Article article;
    private CapTemplateDTO capTemplate;
    private SymbolDTO symbol;
}
