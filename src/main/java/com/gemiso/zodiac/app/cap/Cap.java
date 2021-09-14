package com.gemiso.zodiac.app.cap;

import com.gemiso.zodiac.app.template.Template;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "tb_cap_tmplt"
    /*    uniqueConstraints = {
                @UniqueConstraint(name = "user_userId_unique", columnNames = "user_id")
        }*/
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "template")
@Setter
@DynamicUpdate
public class Cap extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cap_tmplt_id", nullable = false)
    private Long capTmpltId;

    @Column(name = "cap_tmplt_nm", length = 50)
    private String capTmpltNm;

    @Column(name = "cap_tmplt_file_nm", length = 1000)
    private String capTmpltFileNm;

    @Column(name = "cap_class_cd", length = 50)
    private String capClassCd;

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

    @Column(name = "brdc_pgm_id", length = 21)
    private Long brdcPgmId;

    @Column(name = "brdc_pgm_nm", length = 450)
    private String brdcPgmNm;

    @Column(name = "prvw_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String prvwYn;

    @Column(name = "use_yn", columnDefinition = "bpchar(1) default 'Y'", nullable = false)
    private String useYn;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "inputr_id", length = 50, nullable = false)
    private String inputrId;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Column(name = "delr_Id", length = 50)
    private String delrId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tmplt_grp_id")
    private Template template;

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
