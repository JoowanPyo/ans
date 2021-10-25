package com.gemiso.zodiac.app.cueSheetItem.dto;

import com.gemiso.zodiac.app.article.dto.ArticleCueItemDTO;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private String cueItemOrdCdNm;
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
    private Date cueArtclCapChgDtm;
    private String cueArtclCapStCd;
    private String cueArtclCapStCdNm;
    private String rmk;
    private String lckYn;
    private String delYn;
    private Date delDtm;
    private Date lckDtm;
    private String cueItemTypCd;
    private String cueItemTypCdNm;
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
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private String lckrId;
    private String lckrNm;
    private Long cueId;
    private ArticleCueItemDTO article;
    private List<CueSheetItemSymbolDTO> cueSheetItemSymbolDTO = new ArrayList<>();

}
