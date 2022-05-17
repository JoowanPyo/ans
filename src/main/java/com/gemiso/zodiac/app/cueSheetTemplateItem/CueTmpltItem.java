package com.gemiso.zodiac.app.cueSheetTemplateItem;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.cueSheetTemplateMedia.CueTmpltMedia;
import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.app.cueSheetTemplateItemCap.CueTmpltItemCap;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.CueTmplSymbol;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "tb_cue_tmplt_item",
        indexes = { @Index(name = "index_cue_tmplt_cue_item_titl", columnList = "cue_item_titl")
                ,@Index(name = "index_cue_tmplt_cue_item_titl_en", columnList = "cue_item_titl_en")}
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"cueSheetTemplate"})
@Setter
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CueTmpltItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_tmplt_item_id", nullable = false)
    private Long cueTmpltItemId;

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

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_item_frm_cd)")
    private String cueItemFrmCdNm;

    @Column(name = "cue_item_div_cd", length = 50)
    private String cueItemDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_item_div_cd)")
    private String cueItemDivCdNm;

    @Column(name = "lck_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String lckYn;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "lck_dtm")
    private Date lckDtm;

    @Column(name = "media_ch_cd", length = 50)
    private String mediaChCd;

    @Column(name = "media_durtn", length = 20)
    private String mediaDurtn;

    @Column(name = "inputr_id", length = 50, nullable = false)
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cue_tmplt_id",nullable = false)
    @JsonBackReference
    private CueSheetTemplate cueSheetTemplate;

    @OneToMany(mappedBy = "cueTmpltItem")
    @JsonManagedReference
    @Where(clause = "del_yn = 'N'")
    private List<CueTmpltItemCap> cueTmpltItemCap = new ArrayList<>();

    @OneToMany(mappedBy = "cueTmpltItem")
    @JsonManagedReference
    private List<CueTmplSymbol> cueTmplSymbol = new ArrayList<>();

    @OneToMany(mappedBy = "cueTmpltItem")
    @JsonManagedReference
    @Where(clause = "del_yn = 'N'")
    private List<CueTmpltMedia> cueTmpltMedia = new ArrayList<>();


    @PrePersist
    public void prePersist() {

        if (this.lckYn == null || this.lckYn == ""){
            this.lckYn = "Y";
        }
        if (this.delYn == null || this.delYn == ""){
            this.delYn = "N";
        }

    }

}
