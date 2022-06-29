package com.gemiso.zodiac.app.cueSheetItem.dto;

import com.gemiso.zodiac.app.article.dto.ArticleUpdateDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItemCap.dto.CueSheetItemCapCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CueSheetItemUpdateDTO {

    private Long cueItemId;
    private String cueItemTitl;
    private String cueItemTitlEn;
    private String cueItemCtt;
    //private int cueItemOrd;
    private String cueItemOrdCd;
    //private String cueItemOrdCdNm;
    private int cueItemTime;
    private String cueItemFrmCd;
    //private String cueItemFrmCdNm;
    private String cueItemDivCd;
    //private String cueItemDivCdNm;
    private String brdcStCd;
    //private String brdcStCdNm;
    private String brdcClk;
    private String chrgrId;
    private String chrgrNm;
    private String artclCapStCd;
    //private String artclCapStCdNm;
    private String cueArtclCapChgYn;
    private Date cueArtclCapChgDtm;
    private String cueArtclCapStCd;
    //private String cueArtclCapStCdNm;
    private String rmk;
    //private String lckYn;
    //private String delYn;
    //private Date delDtm;
    //private Date lckDtm;
    private String cueItemTypCd;
    //private String cueItemTypCdNm;
    private String mediaChCd;
    //private String mediaChCdNm;
    private Date cueItemBrdcDtm;
    private String capChgYn;
    private Date capChgDtm;
    private String capStCd;
    private String capStCdNm;
    private String artclStCd;
    private String artclStCdNm;
    private String mediaDurtn;
    private String newsBreakYn;
    //private String inputrId;
    //private String inputrNm;
    private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    //private String lckrId;
    //private String lckrNm;
    private String artclTop;
    private String headLn;
    private String artclRef;
    private String spareYn;
    private Integer artclExtTime;
    private CueSheetSimpleDTO cueSheet;
    private ArticleUpdateDTO article;
    private CueSheetTemplateSimpleDTO cueSheetTemplate;
    private List<CueSheetItemCapCreateDTO> cueSheetItemCap = new ArrayList<>();
}
