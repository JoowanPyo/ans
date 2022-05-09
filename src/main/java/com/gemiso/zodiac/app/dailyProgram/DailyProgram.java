package com.gemiso.zodiac.app.dailyProgram;

import com.gemiso.zodiac.app.baseProgram.BaseProgram;
import com.gemiso.zodiac.app.program.Program;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Entity
@Table(name = "tb_daily_pgmsch",
        indexes = { @Index(name = "index_daily_pgmsch_brdc_dt", columnList = "brdc_dt")}
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
public class DailyProgram{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "daily_pgm_id", nullable = false)
    private Long dailyPgmId;

    /*@Column(name = "brdc_pgm_id")*/

    @Column(name = "brdc_dt", length = 10, nullable = false)
    private String brdcDt;

    @Column(name = "brdc_seq", columnDefinition = "numeric(5)", nullable = false)
    private int brdcSeq;

    @Column(name = "brdc_start_time", length = 8, nullable = false)
    private String brdcStartTime;

    @Column(name = "brdc_end_clk", length = 8, nullable = false)
    private String brdcEndClk;

    @Column(name = "brdc_div_cd", length = 50)
    private String brdcDivCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = brdc_div_cd)")
    private String brdcDivCdNm;

    @Column(name = "brdc_pgm_nm", length = 450)
    private String brdcPgmNm;

    @Column(name = "src_div_cd", length = 50)
    private String srcDivCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = src_div_cd)")
    private String srcDivCdNm;

    @Column(name = "stdio_id", length = 10)
    private Long stdioId;

    @Column(name = "subrm_id", length = 10)
    private Long subrmId;

    @Column(name = "pgmsch_yn", columnDefinition = "bpchar(1) default 'N'")
    private String pgmschYn;

    @Column(name = "rmk", length = 500)
    private String rmk;

    @Column(name = "input_dtm")
    private String inputDtm;

    @Column(name = "updt_dtm")
    private String updtDtm;

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

    @Column(name = "brdc_run_time", length = 8)
    private String brdcRunTime;

    @ManyToOne
    @JoinColumn(name = "brdc_pgm_id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "bas_pgmsch_id")
    private BaseProgram baseProgram;

}
