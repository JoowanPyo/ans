package com.gemiso.zodiac.app.cueSheetTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.baseProgram.BaseProgram;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;
import com.gemiso.zodiac.app.cueSheetTemplateItem.CueTmpltItem;
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
@Table(name = "tb_cue_tmplt",
        indexes = { @Index(name = "index_cue_template_brdc_pgm_nm", columnList = "brdc_pgm_nm"),
                @Index(name = "index_cue_template_cue_tmplt_nm", columnList = "cue_tmplt_nm")})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "program")
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CueSheetTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cue_tmplt_id", nullable = false)
    private Long cueTmpltId;

    @Column(name = "brdc_pgm_nm", length = 450)
    private String brdcPgmNm;

    @Column(name = "cue_tmplt_nm", length = 150)
    private String cueTmpltNm;

    @Column(name = "news_div_cd", length = 50)
    private String newsDivCd;

    @Basic(fetch = FetchType.LAZY)
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

    @Column(name = "pd_1_id", length = 50)
    private String pd1Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = pd_1_id)")
    private String pd1Nm;

    @Column(name = "pd_2_id")
    private String pd2Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = pd_2_id)")
    private String pd2Nm;

    @Column(name = "anc_1_id")
    private String anc1Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = anc_1_id)")
    private String anc1Nm;

    @Column(name = "anc_2_id")
    private String anc2Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = anc_2_id)")
    private String anc2Nm;

    @Column(name = "td_1_id")
    private String td1Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = td_1_id)")
    private String td1Nm;

    @Column(name = "td_2_id")
    private String td2Id;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = td_2_id)")
    private String td2Nm;

    @Column(name = "stdio_id")
    private Long stdioId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.fclty_nm from tb_fclty_manage a where a.fclty_id = stdio_id)")
    private String stdioNm;

    @Column(name = "subrm_id")
    private Long subrmId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.fclty_nm from tb_fclty_manage a where a.fclty_id = subrm_id)")
    private String subrmNm;

    @ManyToOne
    @JoinColumn(name = "brdc_pgm_id")
    private Program program;

    @ManyToOne
    @JoinColumn(name = "bas_pgmsch_id")
    private BaseProgram baseProgram;

    @OneToMany(mappedBy = "cueSheetTemplate")
    @JsonManagedReference
    private List<CueTmpltItem> cueTmpltItem = new ArrayList<>();

    /*@OneToMany(mappedBy = "cueSheetTemplate")
    @JsonManagedReference
    private List<CueTmplSymbol> cueTmplSymbol = new ArrayList<>();*/

    @PrePersist
    public void prePersist() {

        if(this.delYn == null || this.delYn == ""){
            this.delYn = "N";
        }
    }
}
