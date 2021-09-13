package com.gemiso.zodiac.app.program;

import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_brdc_pgm")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@DynamicUpdate
public class Program extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "brdc_pgm_id", nullable = false)
    private Long brdcPgmId;

    @Column(name = "brdc_pgm_nm", length = 450)
    private String brdcPgmNm;

    @Column(name = "ch_div_cd", length = 50)
    private String chDivCd;

    @Column(name = "brdc_pgm_div_cd", length = 50)
    private String brdcPgmDivCd;

    @Column(name = "gne_div_cd", length = 50)
    private String gneDivCd;

    @Column(name = "prd_div_cd", length = 50)
    private String prdDivCd;

    @Column(name = "brdc_start_time", length = 6)
    private String brdcStartTime;

    @Column(name = "sch_time", length = 6)
    private String schTime;

    //@Column(name = "input_dtm")
    //private Date inputDtm;

    //@Column(name = "updt_dtm")
    //private Date updtDtm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "inputr_id", length = 50)
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