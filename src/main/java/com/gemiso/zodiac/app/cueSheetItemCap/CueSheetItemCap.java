package com.gemiso.zodiac.app.cueSheetItemCap;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.articleHist.ArticleHist;
import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.capTemplateGrp.CapTemplateGrp;
import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetMedia.CueSheetMedia;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.app.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_cue_item_cap")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CueSheetItemCap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_item_cap_id", nullable = false)
    private Long cueItemCapId;

/*    @Column(name = "cue_item_id", length = 21, nullable = false)
    private Long cueItemId;*/

    @Column(name = "cue_item_cap_div_cd")
    private String cueItemCapDivCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_item_cap_div_cd)")
    private String cueItemCapDivCdNm;

    @Column(name = "cap_ctt", columnDefinition = "text")
    private String capCtt;

    @Column(name = "cap_ord")
    private int capOrd;

    @Column(name = "ln_no")
    private int lnNo;

    @Column(name = "cap_prvw_id", length = 100)
    private String capPrvwId;

    @Column(name = "cap_class_cd")
    private String capClassCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = cap_class_cd)")
    private String capClassCdNm;

    @Column(name = "cap_prvw_url", length = 1000)
    private String capPrvwUrl;

    @Column(name = "color_info", length = 20)
    private String colorInfo;

    @Column(name = "cap_rmk", length = 2000)
    private String capRmk;

    @Column(name = "org_cue_item_cap_id", length = 21)
    private String orgCueItemCapId;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'")
    private String delYn;

    @Column(name = "del_dtm")
    private Date delDtm;

    /*@Column(name = "cap_tmplt_id", nullable = false)
    private Long capTmpltId;*/

    @Column(name = "inputr_id")
    private String inputrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Column(name = "updtr_id")
    private String updtrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = updtr_id)")
    private String updtrNm;

    @Column(name = "delr_id")
    private String delrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = delr_id)")
    private String delrNm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cap_tmplt_id")
    private CapTemplate capTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cue_item_id")
    @JsonBackReference
    private CueSheetItem cueSheetItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol_id")
    private Symbol symbol;
    

    @PrePersist
    public void prePersist() {

        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
    }

}
