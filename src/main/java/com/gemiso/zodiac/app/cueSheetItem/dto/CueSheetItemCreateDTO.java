package com.gemiso.zodiac.app.cueSheetItem.dto;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCap;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CueSheetItemCreateDTO {

    //private Long cueItemId;
    private String cueItemTitl;
    private String cueItemTitlEn;
    private String cueItemCtt;
    private int cueItemOrd;
    private String cueItemOrdCd;
    private int cueItemTime;
    private String cueItemFrmCd;
    private String cueItemDivCd;
    private String brdcStCd;
    private String brdcClk;
    private String chrgrId;
    private String chrgrNm;
    private String artclCapStCd;
    private String cueArtclCapChgYn;
    private Date cueArtclCapChgDtm;
    private String cueArtclCapStCd;
    private String rmk;
    private String cueItemTypCd;
    private String mediaChCd;
    private Date cueItemBrdcDtm;
    private String capChgYn;
    private Date capChgDtm;
    private String capStCd;
    private String capStCdNm;
    private String artclStCd;
    private String artclStCdNm;
    private String mediaDurtn;
    private String newsBreakYn;
    private String inputrId;
    private String artclTop;
    private String headLn;
    private String artclRef;
    private CueSheetSimpleDTO cueSheet;
    private ArticleSimpleDTO article;
    private CueSheetTemplateSimpleDTO cueSheetTemplate;
    private List<CueSheetItemCapCreateDTO> cueSheetItemCap = new ArrayList<>();
}
