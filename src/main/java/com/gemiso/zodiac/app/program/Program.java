package com.gemiso.zodiac.app.program;

import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.app.dailyProgram.DailyProgram;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

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

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = ch_div_cd)")
    private String chDivCdNm;

    @Column(name = "brdc_pgm_div_cd", length = 50)
    private String brdcPgmDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = brdc_pgm_div_cd)")
    private String brdcPgmDivCdNm;

    @Column(name = "gne_div_cd", length = 50)
    private String gneDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = gne_div_cd)")
    private String gneDivCdNm;

    @Column(name = "prd_div_cd", length = 50)
    private String prdDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = prd_div_cd)")
    private String prdDivCdNm;

    @Column(name = "brdc_start_time", length = 8)
    private String brdcStartTime;

    @Column(name = "sch_time", length = 8)
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

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = updtr_id)")
    private String updtrNm;

    @Column(name = "delr_id", length = 50)
    private String delrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = delr_id)")
    private String delrNm;

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