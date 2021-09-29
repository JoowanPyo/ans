package com.gemiso.zodiac.app.dailyProgram;

import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tb_daily_pgmsch"
        /*uniqueConstraints = {
                @UniqueConstraint(name = "file_fileId_unique", columnNames = "file_id")
        }*/)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "brdc_pgm_id")
@DynamicUpdate
public class DailyProgram extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    /*@Column(name = "brdc_pgm_id")*/

    @Column(name = "brdc_dt", length = 8, nullable = false)
    private String brdcDt;

    @Column(name = "brdc_seq", columnDefinition = "numeric(5)", nullable = false)
    private String brdcSeq;

    @Column(name = "brdc_start_time", length = 6, nullable = false)
    private String brdcStartTime;

    @Column(name = "brdc_end_clk", columnDefinition = "bpchar(6)", nullable = false)
    private String brdcEndClk;

    @Column(name = "brdc_div_cd", length = 50)
    private String brdcDivCd;

    @Column(name = "brdc_pgm_nm", length = 450)
    private String brdcPgmNm;

    @Column(name = "src_div_cd", length = 50)
    private String srcDivCd;

    @Column(name = "stdio_id", length = 10)
    private String stdioId;

    @Column(name = "subrm_id", length = 10)
    private String subrmId;

    @Column(name = "pgmsch_yn", columnDefinition = "bpchar(1) default 'N'")
    private String pgmschYn;

    @Column(name = "rmk", length = 500)
    private String rmk;

    @Column(name = "inputr_id", length = 50, nullable = false)
    private String inputrId;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    /*@Column(name = "brdc_pgm_id")*/
    @ManyToOne
    @JoinColumn(name = "brdc_pgm_id")
    private Program program;

}
