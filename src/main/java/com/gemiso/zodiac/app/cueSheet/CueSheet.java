package com.gemiso.zodiac.app.cueSheet;

import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.user.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inputr_id", nullable = false)
    private User inputr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delr_id")
    private User delr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_1_id")
    private User pd1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pd_2_id")
    private User pd2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anc_1_id")
    private User anc1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anc_2_id")
    private User anc2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lckr_id")
    private User lckr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "td_1_id")
    private User td1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "td_2_id")
    private User td2;

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
