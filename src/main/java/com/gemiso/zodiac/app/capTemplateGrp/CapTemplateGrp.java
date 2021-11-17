package com.gemiso.zodiac.app.capTemplateGrp;

import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "tb_cap_tmplt_grp")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "cap")
@DynamicUpdate
public class CapTemplateGrp extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tmplt_grp_id", nullable = false)
    private Long tmpltGrpId;

    @Column(name = "ch_div_cd", length = 50)
    private String chDivCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = ch_div_cd)")
    private String chDivCdNm;

    @Column(name = "tmplt_grp_nm", length = 150, nullable = false)
    private String tmpltGrpNm;

    @Column(name = "brdc_pgm_id", length = 21)
    private String brdcPgmId;

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

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'")
    private String delYn;

    @OneToMany(mappedBy="capTemplateGrp")
    private List<CapTemplate> capTemplate ;

    @PrePersist
    public void prePersist() {

        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
    }

}
