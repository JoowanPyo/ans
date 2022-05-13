package com.gemiso.zodiac.app.capTemplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.capTemplateGrp.CapTemplateGrp;
import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "tb_cap_tmplt",
        indexes = { @Index(name = "index_cap_template_nm", columnList = "cap_tmplt_nm")}
    /*    uniqueConstraints = {
                @UniqueConstraint(name = "user_userId_unique", columnNames = "user_id")
        }*/
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"capTemplateGrp", "articleCap"})
@Setter
@DynamicUpdate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CapTemplate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cap_tmplt_id", nullable = false)
    private Long capTmpltId;

    @Column(name = "cap_tmplt_nm", length = 50)
    private String capTmpltNm;

    @Column(name = "cap_tmplt_file_nm", length = 255)
    private String capTmpltFileNm;

    @Column(name = "cap_class_cd", length = 50)
    private String capClassCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = cap_class_cd)")
    private String capClassCdNm;

    @Column(name = "cap_ln_num")
    private int capLnNum;

    @Column(name = "cap_lttr_num")
    private int capLttrNum;

    @Column(name = "cap_cell_dlmtr", length = 10)
    private String capCellDlmtr;

    @Column(name = "cap_tmplt_help", length = 100)
    private String capTmpltHelp;

    @Column(name = "cap_tmplt_ord")
    private int capTmpltOrd;

    @Column(name = "var_cnt")
    private int varCnt;

    @Column(name = "var_nm", length = 100)
    private String varNm;

    @Column(name = "take_count")
    private int takeCount;

    //맵핑테이블로 대처.
    /*@Column(name = "brdc_pgm_id", length = 21)
    private Long brdcPgmId;

    @Column(name = "brdc_pgm_nm", length = 450)
    private String brdcPgmNm;*/

    @Column(name = "prvw_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String prvwYn;

    @Column(name = "use_yn", columnDefinition = "bpchar(1) default 'Y'", nullable = false)
    private String useYn;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tmplt_grp_id", nullable = false)
    private CapTemplateGrp capTemplateGrp;

    @OneToMany(mappedBy = "capTemplate")
    private List<ArticleCap> articleCap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private AttachFile attachFile;

    @PrePersist
    public void prePersist() {

        if (this.useYn == null || this.useYn == "") {
            this.useYn = "Y";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
        if (this.prvwYn == null || this.prvwYn == "") {
            this.prvwYn = "N";
        }
    }

}
