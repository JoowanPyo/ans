package com.gemiso.zodiac.app.cueSheet;

import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "tb_cue"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
public class CueSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_id", nullable = false)
    private Long cueId;

    @Column(name = "cue_div_cd", length = 50)
    private String cueDivCd;

    @Column(name = "ch_div_cd", length = 50)
    private String chDivCd;

    @Column(name = "brdc_dt", length = 10)
    private String brdcDt;

    @Column(name = "brdc_start_time", length = 8)
    private String brdcStartTime;

    @Column(name = "brdc_end_time", length = 8)
    private String brdcEndTime;

    @Column(name = "brdc_sch_time", length = 8)
    private String brdcSchTime;

    @Column(name = "brdc_pgm_nm", length = 450)
    private String brdcPgmNm;

    @Column(name = "cue_st_cd", length = 50)
    private String cueStCd;

    @Column(name = "stdio_id", length = 10)
    private String stdioId;

    @Column(name = "subrm_id", length = 10)
    private String subrmId;

    @Column(name = "lck_dtm")
    private Date lckDtm;

    @Column(name = "lck_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String lckYn;

    @Column(name = "del_dtm")
    private Date delDtm;

    @CreatedDate
    @Column(name = "input_dtm", updatable = false)
    private Date inputDtm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "inputr_id", length = 50 , nullable = false)
    private String inputrId;

    @Column(name = "delr_id", length = 50)
    private String delrId;

    @Column(name = "pd_1_id", length = 50)
    private String pd1Id;

    @Column(name = "pd_2_id", length = 50)
    private String pd2Id;

    @Column(name = "anc_1_id", length = 50)
    private String anc1Id;

    @Column(name = "anc_2_id", length = 50)
    private String anc2Id;

    @Column(name = "lckr_id", length = 50)
    private String lckrId;

    @Column(name = "td_1_id", length = 50)
    private String td1Id;

    @Column(name = "td_2_id", length = 50)
    private String td2Id;

    @Column(name = "remark", length = 500)
    private String remark;

    /*@Column(name = "brdc_pgm_id")*/
    @ManyToOne
    @JoinColumn(name = "brdc_pgm_id")
    private Program program;

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
