package com.gemiso.zodiac.app.articleCap.dto;

import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.capTemplate.dto.CapTemplateDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCapDTO {

    private Long artclCapId;
    private String capDivCd;
    private int lnNo;
    private String capCtt;
    private String capRmk;
    private ArticleDTO article;
    private CapTemplateDTO cap;
    private SymbolDTO symbol;
}
