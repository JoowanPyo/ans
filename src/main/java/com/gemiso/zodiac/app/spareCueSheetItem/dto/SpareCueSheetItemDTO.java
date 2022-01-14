package com.gemiso.zodiac.app.spareCueSheetItem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.article.dto.ArticleCueItemDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItemSymbol.dto.CueSheetItemSymbolDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpareCueSheetItemDTO {

    private Long spareCueItemId;
    private String cueItemTitl;
    private String cueItemTitlEn;
    private String cueItemCtt;
    private int cueItemOrd;
    private String cueItemOrdCd;
    private int cueItemTime;
    private String cueItemFrmCd;
    private String cueItemFrmCdNm;
    private String cueItemDivCd;
    private String cueItemDivCdNm;
    private String brdcStCd;
    private String brdcStCdNm;
    private String brdcClk;
    private String chrgrId;
    private String chrgrNm;
    private String artclCapStCd;
    private String artclCapStCdNm;
    private String cueArtclCapChgYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date cueArtclCapChgDtm;
    private String cueArtclCapStCd;
    private String cueArtclCapStCdNm;
    private String rmk;
    private String lckYn;
    private String delYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date delDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lckDtm;
    private String cueItemTypCd;
    private String cueItemTypCdNm;
    private String mediaChCd;
    private String mediaChCdNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date cueItemBrdcDtm;
    private String capChgYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date capChgDtm;
    private String capStCd;
    private String capStCdNm;
    private String artclStCd;
    private String artclStCdNm;
    private String mediaDurtn;
    private String newsBreakYn;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private String lckrId;
    private String lckrNm;
    private String artclTop;
    private String headLn;
    private String artclRef;
    private CueSheetSimpleDTO cueSheet;
    private ArticleCueItemDTO article;
    private List<CueSheetItemSymbolDTO> cueSheetMedia = new ArrayList<>();
}
