package com.gemiso.zodiac.app.cueSheetTemplateItemCap;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.cueSheetTemplateItem.CueTmpltItem;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "tb_cue_tmplt_item_cap"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"capTemplate","cueTmpltItem"})
@Setter
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CueTmpltItemCap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_item_cap_id", nullable = false)
    private Long cueItemCapId;

    @Column(name = "cue_item_cap_div_cd", length = 50)
    private String cueItemCapDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_item_cap_div_cd)")
    private String cueItemCapDivCdNm;

    @Column(name = "cap_ctt", length = 2000)
    private String capCtt;

    @Column(name = "cap_ord")
    private int capOrd;

    @Column(name = "ln_no")
    private int lnNo;

    @Column(name = "cap_prvw_id", length = 100)
    private String capPrvwId;

    @Column(name = "cap_class_cd", length = 50)
    private String capClassCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = cap_class_cd)")
    private String capClassCdNm;

    @Column(name = "cap_prvw_url", length = 1000)
    private String capPrvwUrl;

    @Column(name = "color_info", length = 20)
    private String colorInfo;

    @Column(name = "cap_rmk", length = 2000)
    private String capRmk;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'")
    private String delYn;

    @Column(name = "del_dtm")
    private Date delDtm;

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

    @ManyToOne
    @JoinColumn(name = "cap_tmplt_id")
    private CapTemplate capTemplate;

    @ManyToOne
    @JoinColumn(name = "cue_tmplt_item_id")
    @JsonBackReference
    private CueTmpltItem cueTmpltItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "symbol_id")
    private Symbol symbol;

    @PrePersist
    public void prePersist() {

        if (this.delYn == null || this.delYn == ""){
            this.delYn = "N";
        }

    }

}