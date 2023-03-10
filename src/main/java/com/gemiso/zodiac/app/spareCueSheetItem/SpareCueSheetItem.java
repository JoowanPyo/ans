package com.gemiso.zodiac.app.spareCueSheetItem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMedia;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_spare_cue_item")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"cueSheet","article","cueSheetMedia"})
@DynamicUpdate
public class SpareCueSheetItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spare_cue_item_id", nullable = false)
    private Long spareCueItemId;

    @Column(name = "cue_item_titl", length = 300)
    private String cueItemTitl;

    @Column(name = "cue_item_titl_en", length = 300)
    private String cueItemTitlEn;

    @Column(name = "cue_item_ctt", length = 500)
    private String cueItemCtt;

    @Column(name = "cue_item_ord")
    private int cueItemOrd;

    @Column(name = "cue_item_ord_cd", length = 5)
    private String cueItemOrdCd;

    @Column(name = "cue_item_time")
    private int cueItemTime;

    @Column(name = "cue_item_frm_cd", length = 50)
    private String cueItemFrmCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_item_frm_cd)")
    private String cueItemFrmCdNm;

    @Column(name = "cue_item_div_cd", length = 50)
    private String cueItemDivCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_item_div_cd)")
    private String cueItemDivCdNm;

    @Column(name = "brdc_st_cd", length = 50)
    private String brdcStCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = brdc_st_cd)")
    private String brdcStCdNm;

    @Column(name = "brdc_clk", length = 6)
    private String brdcClk;

    @Column(name = "chrgr_id", length = 20)
    private String chrgrId;

    @Column(name = "chrgr_nm", length = 100)
    private String chrgrNm;

    @Column(name = "artcl_cap_st_cd", length = 50)
    private String artclCapStCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_cap_st_cd)")
    private String artclCapStCdNm;

    @Column(name = "cue_artcl_cap_chg_yn", columnDefinition = "bpchar(1) default 'N'")
    private String cueArtclCapChgYn;

    @Column(name = "cue_artcl_cap_chg_dtm")
    private Date cueArtclCapChgDtm;

    @Column(name = "cue_artcl_cap_st_cd", length = 50)
    private String cueArtclCapStCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_artcl_cap_st_cd)")
    private String cueArtclCapStCdNm;

    @Column(name = "rmk", length = 500)
    private String rmk;

    @Column(name = "lck_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String lckYn;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "lck_dtm")
    private Date lckDtm;

    @Column(name = "cue_item_typ_cd", length = 50)
    private String cueItemTypCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_item_typ_cd)")
    private String cueItemTypCdNm;

    @Column(name = "media_ch_cd", length = 50)
    private String mediaChCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = media_ch_cd)")
    private String mediaChCdNm;

    @Column(name = "cue_item_brdc_dtm")
    private Date cueItemBrdcDtm;

    @Column(name = "cap_chg_yn", columnDefinition = "bpchar(1) default 'N'")
    private String capChgYn;

    @Column(name = "cap_chg_dtm")
    private Date capChgDtm;

    @Column(name = "cap_st_cd", length = 50)
    private String capStCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = cap_st_cd)")
    private String capStCdNm;

    @Column(name = "artcl_st_cd", length = 50)
    private String artclStCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_st_cd)")
    private String artclStCdNm;

    @Column(name = "media_durtn", length = 20)
    private String mediaDurtn;

    @Column(name = "news_break_yn", columnDefinition = "bpchar(1) default 'N'")
    private String newsBreakYn;

    @Column(name = "inputr_id", length = 50)
    private String inputrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = updtr_id)")
    private String updtrNm;

    @Column(name = "delr_id", length = 50)
    private String delrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = delr_id)")
    private String delrNm;

    @Column(name = "lckr_id", length = 50)
    private String lckrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = lckr_id)")
    private String lckrNm;

    @Column(name = "artcl_top", columnDefinition = "bpchar(1) default 'N'")
    private String artclTop;

    @Column(name = "head_ln", columnDefinition = "bpchar(1) default 'N'")
    private String headLn;

    @Column(name = "artcl_ref", columnDefinition = "bpchar(1) default 'N'")
    private String artclRef;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cue_id", nullable = false)
    @JsonBackReference
    private CueSheet cueSheet;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artcl_id")
    @JsonBackReference
    private Article article;

    @OneToMany(mappedBy = "cueSheetItem")
    @JsonManagedReference
    private List<CueSheetMedia> cueSheetMedia = new ArrayList<>();


    @PrePersist
    public void prePersist() {

        if (this.cueArtclCapChgYn == null || this.cueArtclCapChgYn == "") {
            this.cueArtclCapChgYn = "N";
        }
        if (this.lckYn == null || this.lckYn == "") {
            this.lckYn = "N";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
        if (this.capChgYn == null || this.capChgYn == "") {
            this.capChgYn = "N";
        }
        if (this.newsBreakYn == null || this.newsBreakYn == "") {
            this.newsBreakYn = "N";
        }
        if (this.artclTop == null || this.artclTop == "") {
            this.artclTop = "N";
        }
        if (this.headLn == null || this.headLn == "") {
            this.headLn = "N";
        }
        if (this.artclRef == null || this.artclRef == "") {
            this.artclRef = "N";
        }
    }

}
