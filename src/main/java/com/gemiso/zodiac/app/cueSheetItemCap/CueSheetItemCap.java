package com.gemiso.zodiac.app.cueSheetItemCap;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_cue_item_cap")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
public class CueSheetItemCap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_item_cap_id", nullable = false)
    private Long cueItemCapId;

    @Column(name = "cue_item_id", length = 21, nullable = false)
    private Long cueItemId;

    @Column(name = "cue_item_cap_div_cd", length = 50)
    private String cueItemCapDivCd;

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

    @Column(name = "cap_tmplt_id", nullable = false)
    private Long capTmpltId;

    @Column(name = "inputr_id", length = 50, nullable = false)
    private String inputrId;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Column(name = "delr_id", length = 50)
    private String delrId;

    @PrePersist
    public void prePersist() {

        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
    }

}
