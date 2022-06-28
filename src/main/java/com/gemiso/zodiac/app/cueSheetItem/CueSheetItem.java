package com.gemiso.zodiac.app.cueSheetItem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCap;
import com.gemiso.zodiac.app.cueSheetItemSymbol.CueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMedia;
import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "tb_cue_item")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"cueSheet","article","cueSheetTemplate","cueSheetMedia","cueSheetItemCap","cueSheetItemSymbol"})
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CueSheetItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_item_id", nullable = false)
    private Long cueItemId;

    @Column(name = "cue_item_titl", length = 300)
    private String cueItemTitl;

    @Column(name = "cue_item_titl_en", length = 300)
    private String cueItemTitlEn;

    @Column(name = "cue_item_ctt", columnDefinition = "text")
    private String cueItemCtt;

    @Column(name = "cue_item_ord")
    private int cueItemOrd;

    @Column(name = "cue_item_ord_cd", length = 5)
    private String cueItemOrdCd;

    @Column(name = "cue_item_time")
    private int cueItemTime;

    @Column(name = "cue_item_frm_cd", length = 50)
    private String cueItemFrmCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_item_frm_cd)")
    private String cueItemFrmCdNm;

    @Column(name = "cue_item_div_cd", length = 50)
    private String cueItemDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_item_div_cd)")
    private String cueItemDivCdNm;

    @Column(name = "brdc_st_cd", length = 50)
    private String brdcStCd;

    /*@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = brdc_st_cd)")
    private String brdcStCdNm;*/

    @Column(name = "brdc_clk", length = 6)
    private String brdcClk;

    @Column(name = "chrgr_id", length = 20)
    private String chrgrId;

    @Column(name = "chrgr_nm", length = 100)
    private String chrgrNm;

    @Column(name = "artcl_cap_st_cd", length = 50)
    private String artclCapStCd;

   /* @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_cap_st_cd)")
    private String artclCapStCdNm;*/

    @Column(name = "cue_artcl_cap_chg_yn", columnDefinition = "bpchar(1) default 'N'")
    private String cueArtclCapChgYn;

    @Column(name = "cue_artcl_cap_chg_dtm")
    private Date cueArtclCapChgDtm;

    @Column(name = "cue_artcl_cap_st_cd", length = 50)
    private String cueArtclCapStCd;

    @Basic(fetch = FetchType.LAZY)
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

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_item_typ_cd)")
    private String cueItemTypCdNm;

    @Column(name = "media_ch_cd", length = 50)
    private String mediaChCd;

    @Basic(fetch = FetchType.LAZY)
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

    /*@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = cap_st_cd)")
    private String capStCdNm;*/

    @Column(name = "artcl_st_cd", length = 50)
    private String artclStCd;

    /*@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = artcl_st_cd)")
    private String artclStCdNm;*/

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

    /*@Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = updtr_id)")
    private String updtrNm;*/

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

    @Column(name = "spare_yn", columnDefinition = "bpchar(1) default 'N'")
    private String spareYn;

    @Column(name = "artcl_ext_time")
    private Integer artclExtTime;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cue_id", nullable = false)
    @JsonBackReference
    private CueSheet cueSheet;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY/*, cascade = CascadeType.ALL*/)
    @JoinColumn(name = "artcl_id")
    @JsonBackReference
    private Article article;

    @BatchSize(size = 100)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cue_tmplt_id")
    private CueSheetTemplate cueSheetTemplate;

    @BatchSize(size = 100)
    @OrderBy(value = "inputDtm DESC")
    @OneToMany(mappedBy = "cueSheetItem", fetch = FetchType.LAZY)
    @Where(clause = "del_yn = 'N'")
    @JsonManagedReference
    private Set<CueSheetMedia> cueSheetMedia = Collections.emptySet();

    @BatchSize(size = 100)
    @OrderBy(value = "capOrd ASC")
    @OneToMany(mappedBy = "cueSheetItem", fetch = FetchType.LAZY)
    @Where(clause = "del_yn = 'N'")
    @JsonManagedReference
    private Set<CueSheetItemCap> cueSheetItemCap = Collections.emptySet();

      /*@Column(name = "artcl_id", length = 21)
    private String artclId;*/

    @BatchSize(size = 100)
    @OrderBy(value = "ord ASC")
    @OneToMany(mappedBy = "cueSheetItem", fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<CueSheetItemSymbol> cueSheetItemSymbol = Collections.emptySet();


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
        if (this.spareYn == null || this.spareYn == "") {
            this.spareYn = "N";
        }
    }

}
