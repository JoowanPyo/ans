package com.gemiso.zodiac.app.program;

import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.app.dailyProgram.DailyProgram;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_brdc_pgm")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"cueSheetTemplate","dailyProgram"})
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inputr_id", nullable = false)
    private User inputr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updtr_id")
    private User updtr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delr_id")
    private User delr;

    @OneToMany(mappedBy = "program")
    private List<CueSheetTemplate> cueSheetTemplate;

    @OneToMany(mappedBy = "program")
    private List<DailyProgram> dailyProgram;

    /*@OneToMany(mappedBy = "program")
    private List<CueSheet> cueSheet;*/

    @PrePersist
    public void prePersist() {

        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
    }
}