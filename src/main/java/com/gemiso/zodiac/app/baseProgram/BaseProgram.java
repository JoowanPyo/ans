package com.gemiso.zodiac.app.baseProgram;

import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_brdc_pgm_basepgmsch")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
public class BaseProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bas_pgmsch_id",length = 50, nullable = false )
    private Long basePgmschId;

    @Column(name = "bas_dt", length = 10/*, nullable = false*/)
    private String basDt;

    @Column(name = "ch_div_cd", length = 50/*, nullable = false*/)
    private String chDivCd;

    @Column(name = "brdc_day", length = 10/*, nullable = false*/)
    private String brdcDay;

    @Column(name = "brdc_div_cd", length = 50)
    private String brdcDivCd;

    @Column(name = "brdc_start_clk", length = 8/*, nullable = false*/)
    private String brdcStartClk;

    @Column(name = "brdc_end_clk", length = 8)
    private String brdcEndClk;

    @Column(name = "brdc_start_dt", length = 10)
    private String brdcStartDt;

    @Column(name = "brdc_end_dt", length = 10/*, nullable = false*/)
    private String brdcEndDt;

    @Column(name = "brdc_sub_nm", length = 450)
    private String brdcSubNm;

    @Column(name = "endpgm_dt", length = 10)
    private String endpgmDt;

    @Column(name = "endpgm_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String endpgmYn;

    @Column(name = "input_dtm")
    private String inputDtm;

    @Column(name = "updt_dtm")
    private String updtDtm;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "pd_1_id", length = 50)
    private String pd1Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = pd_1_id)")
    private String pd1Nm;

    @Column(name = "pd_2_id", length = 50)
    private String pd2Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = pd_2_id)")
    private String pd2Nm;

    @Column(name = "anc_1_id", length = 50)
    private String anc1Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = anc_1_id)")
    private String anc1Nm;

    @Column(name = "anc_2_id", length = 50)
    private String anc2Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = anc_2_id)")
    private String anc2Nm;

    @Column(name = "td_1_id", length = 50)
    private String td1Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = td_1_id)")
    private String td1Nm;

    @Column(name = "td_2_id", length = 50)
    private String td2Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = td_2_id)")
    private String td2Nm;

    @Column(name = "inputr_id", length = 50/*, nullable = false*/)
    private String inputrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Column(name = "delr_id", length = 50)
    private String delrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = delr_id)")
    private String delrNm;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = updtr_id)")
    private String updtrNm;

    @Column(name = "bas_dt_id", length = 14)
    private String basDtId;

    @Column(name = "cap_id", length = 10)
    private String capId;

    @Column(name = "cap_loc", length = 50)
    private String capLoc;

    @Column(name = "bas_dt_ver")
    private int basDtVer;

    @Column(name = "vf_id", length = 10)
    private String vfId;

    @Column(name = "bas_dt_use_yn", columnDefinition = "bpchar(1) default 'N'")
    private String basDtUseYn;

    @Column(name = "vs_id", length = 10)
    private String vsId;

    @Column(name = "make_year", length = 4)
    private String makeYear;

    @ManyToOne
    @JoinColumn(name = "brdc_pgm_id")
    private Program program;

    @PrePersist
    public void prePersist() {

        if (this.endpgmYn == null || this.endpgmYn == "") {
            this.endpgmYn = "N";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
        if (this.basDtUseYn == null || this.basDtUseYn == "") {
            this.basDtUseYn = "N";
        }

    }


}
