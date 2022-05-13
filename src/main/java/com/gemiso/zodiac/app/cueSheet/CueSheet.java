package com.gemiso.zodiac.app.cueSheet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.baseProgram.BaseProgram;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.program.Program;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "tb_cue",
        indexes = { @Index(name = "index_cue_brdc_dt", columnList = "brdc_dt")
                ,@Index(name = "index_cue_brdc_pgm_nm", columnList = "brdc_pgm_nm")}
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"cueSheetItem","program"})
@Setter
@DynamicUpdate
@EntityListeners(value = {AuditingEntityListener.class})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CueSheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_id", nullable = false)
    private Long cueId;

    @Column(name = "cue_div_cd", length = 50)
    private String cueDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_div_cd)")
    private String cueDivCdNm;

    @Column(name = "ch_div_cd", length = 50)
    private String chDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = ch_div_cd)")
    private String chDivCdNm;

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

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = cue_st_cd)")
    private String cueStCdNm;

    @Column(name = "stdio_id", length = 10)
    private Long stdioId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.fclty_nm from tb_fclty_manage a where a.fclty_id = stdio_id)")
    private String stdioNm;

    @Column(name = "subrm_id", length = 10)
    private Long subrmId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.fclty_nm from tb_fclty_manage a where a.fclty_id = subrm_id)")
    private String subrmNm;

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

    @Column(name = "inputr_id", length = 50)
    private String inputrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Column(name = "delr_id", length = 50)
    private String delrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = delr_id)")
    private String delrNm;

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

    @Column(name = "lckr_id", length = 50)
    private String lckrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = lckr_id)")
    private String lckrNm;

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

    @Column(name = "remark", length = 500)
    private String remark;

    @Column(name = "brdc_run_time", length = 8)
    private String brdcRunTime;

    @Column(name = "cue_ver")
    private Integer cueVer;

    @Column(name = "cue_oder_ver")
    private Integer cueOderVer;

    @ManyToOne
    @JoinColumn(name = "brdc_pgm_id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "bas_pgmsch_id")
    private BaseProgram baseProgram;

  /*  @OneToMany(mappedBy = "cueSheet")
    @JsonManagedReference
    private List<CueSheetItem> cueSheetItem = new ArrayList<>();*/

    @PrePersist
    public void prePersist() {

        if (this.lckYn == null || this.lckYn == ""){
            this.lckYn = "N";
        }
        if (this.delYn == null || this.delYn == ""){
            this.delYn = "N";
        }

    }

}
