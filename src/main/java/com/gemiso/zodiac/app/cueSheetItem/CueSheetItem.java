package com.gemiso.zodiac.app.cueSheetItem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_cue_item")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "article")
@DynamicUpdate
public class CueSheetItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_item_id", nullable = false)
    private Long cueItemId;

    @Column(name = "cue_item_titl", length = 300)
    private String cueItemTitl;

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

    @Column(name = "cue_item_div_cd", length = 50)
    private String cueItemDivCd;

    @Column(name = "brdc_st_cd", length = 50)
    private String brdcStCd;

    @Column(name = "brdc_clk", length = 6)
    private String brdcClk;

    @Column(name = "chrgr_id", length = 20)
    private String chrgrId;

    @Column(name = "chrgr_nm", length = 100)
    private String chrgrNm;

    @Column(name = "artcl_cap_st_cd", length = 50)
    private String artclCapStCd;

    @Column(name = "cue_artcl_cap_chg_yn", columnDefinition = "bpchar(1) default 'N'")
    private String cueArtclCapChgYn;

    @Column(name = "cue_artcl_cap_chg_dtm")
    private Date cueArtclCapChgDtm;

    @Column(name = "cue_artcl_cap_st_cd", length = 50)
    private String cueArtclCapStCd;

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

    @Column(name = "cue_item_brdc_dtm")
    private Date cueItemBrdcDtm;

    @Column(name = "cap_chg_yn", columnDefinition = "bpchar(1) default 'N'")
    private String capChgYn;

    @Column(name = "cap_chg_dtm")
    private Date capChgDtm;

    @Column(name = "cap_st_cd", length = 50)
    private String capStCd;

    @Column(name = "artcl_st_cd", length = 50)
    private String artclStCd;

    @Column(name = "media_durtn", length = 20)
    private String mediaDurtn;

    @Column(name = "news_break_yn", columnDefinition = "bpchar(1) default 'N'")
    private String newsBreakYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inputr_id", nullable = false)
    private User inputr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updtr_id")
    private User updtr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delr_id")
    private User delr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lckr_id")
    private User lckr;

    @Column(name = "cue_id", nullable = false)
    private Long cueId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artcl_id")
    @JsonBackReference
    private Article article;
      /*@Column(name = "artcl_id", length = 21)
    private String artclId;*/

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
    }

}
