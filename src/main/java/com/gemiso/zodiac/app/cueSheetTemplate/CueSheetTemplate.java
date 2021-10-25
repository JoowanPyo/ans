package com.gemiso.zodiac.app.cueSheetTemplate;

import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

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

    @Column(name = "news_div_cd")
    private String newsDivCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = news_div_cd)")
    private String newsDivCdNm;

    @Column(name = "brdc_start_time", length = 6)
    private String brdcStartTime;

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

    @Column(name = "pd_1_id")
    private String pd1Id;

    @Column(name = "pd_1_nm", length = 100)
    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = pd_1_id)")
    private String pd1Nm;

    @JoinColumn(name = "pd_2_id")
    private String pd2Id;

    @Column(name = "pd_2_nm", length = 100)
    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = pd_2_id)")
    private String pd2Nm;

    @JoinColumn(name = "anc_1_id")
    private String anc1Id;

    @Column(name = "anc_1_nm", length = 100)
    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = anc_1_id)")
    private String anc1Nm;

    @JoinColumn(name = "anc_2_id")
    private String anc2Id;

    @Column(name = "anc_2_nm", length = 100)
    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = anc_2_id)")
    private String anc2Nm;

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
