package com.gemiso.zodiac.app.cueSheetItem.dto;

import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CueSheetItemDTO {

    private Long cueItemId;
    private String cueItemTitl;
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
    private String lckYn;
    private String delYn;
    private Date delDtm;
    private Date lckDtm;
    private Date inputDtm;
    private Date updtDtm;
    private String cueItemTypCd;
    private Date cueItemBrdcDtm;
    private String capChgYn;
    private Date capChgDtm;
    private String capStCd;
    private String artclStCd;
    private String mediaDurtn;
    private String newsBreakYn;
    private UserSimpleDTO inputr;
    private UserSimpleDTO updtr;
    private UserSimpleDTO delr;
    private UserSimpleDTO lckr;
    private Long cueId;
    private ArticleDTO article;
}
