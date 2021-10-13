package com.gemiso.zodiac.app.cueSheetTemplate;

import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_cue_tmplt"
        /*uniqueConstraints = {
                @UniqueConstraint(name = "file_fileId_unique", columnNames = "file_id")
        }*/)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "program")
@DynamicUpdate
public class CueSheetTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_tmplt_id", nullable = false)
    private Long cueTmpltId;

    @Column(name = "brdc_pgm_nm", length = 150)
    private String brdcPgmNm;

    @Column(name = "cue_tmplt_nm", length = 150)
    private String cueTmpltNm;

    @Column(name = "news_div_cd", length = 50)
    private String newsDivCd;

    @Column(name = "brdc_start_time", length = 6)
    private String brdcStartTime;

    @Column(name = "pd_1_nm", length = 100)
    private String pd1Nm;

    @Column(name = "pd_2_nm", length = 100)
    private String pd2Nm;

    @Column(name = "anc_1_nm", length = 100)
    private String anc1Nm;

    @Column(name = "anc_2_nm", length = 100)
    private String anc2Nm;

    @Column(name = "rmk", length = 500)
    private String rmk;

    @Column(name = "pgmsch_time", length = 8)
    private String pgmschTime;

    @Column(name = "cap_hil_clr_rgb_1", length = 12)
    private String capHilClrRgb1;

    @Column(name = "cap_hil_clr_rgb_2", length = 12)
    private String capHilClrRgb2;

    @Column(name = "cap_hil_clr_rgb_3", length = 12)
    private String capHilClrRgb3;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'")
    private String delYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_1_id", nullable = false)
    private User pd1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_2_id", nullable = false)
    private User pd2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anc_1_id", nullable = false)
    private User anc1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anc_2_id", nullable = false)
    private User anc2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inputr_id", nullable = false)
    private User inputr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updtr_id")
    private User updtr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delr_id")
    private User delr;

    @ManyToOne
    @JoinColumn(name = "brdc_pgm_id")
    private Program program;

    @PrePersist
    public void prePersist() {

        if(this.delYn == null || this.delYn == ""){
            this.delYn = "N";
        }
    }
}
