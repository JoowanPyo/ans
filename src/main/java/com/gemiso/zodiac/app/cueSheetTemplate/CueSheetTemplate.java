package com.gemiso.zodiac.app.cueSheetTemplate;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.baseProgram.BaseProgram;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.CueTmplSymbol;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_cue_tmplt")
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
    @Column(name = "cue_tmplt_id", length = 50, nullable = false)
    private Long cueTmpltId;

    @Column(name = "brdc_pgm_nm", length = 150)
    private String brdcPgmNm;

    @Column(name = "cue_tmplt_nm", length = 150)
    private String cueTmpltNm;

    @Column(name = "news_div_cd", length = 50)
    private String newsDivCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = news_div_cd)")
    private String newsDivCdNm;

    @Column(name = "rmk", length = 500)
    private String rmk;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'")
    private String delYn;

    @Column(name = "inputr_id", length = 50, nullable = false)
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

    @ManyToOne
    @JoinColumn(name = "brdc_pgm_id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "bas_pgmsch_id")
    private BaseProgram baseProgram;

    @OneToMany(mappedBy = "cueSheetTemplate")
    @JsonManagedReference
    private List<CueTmplSymbol> cueTmplSymbol = new ArrayList<>();

    @PrePersist
    public void prePersist() {

        if(this.delYn == null || this.delYn == ""){
            this.delYn = "N";
        }
    }
}
