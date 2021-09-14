package com.gemiso.zodiac.app.template;

import com.gemiso.zodiac.app.cap.Cap;
import com.gemiso.zodiac.app.userGroup.UserGroupAuth;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

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
public class Template extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tmplt_grp_id", nullable = false)
    private Long tmpltGrpId;

    @Column(name = "ch_div_cd", length = 50)
    private String chDivCd;

    @Column(name = "tmplt_grp_nm", length = 150, nullable = false)
    private String tmpltGrpNm;

    @Column(name = "brdc_pgm_id", length = 21)
    private String brdcPgmId;

    @Column(name = "inputr_id", length = 50, nullable = false)
    private String inputrId;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'")
    private String delYn;

    @OneToMany(mappedBy="template")
    private List<Cap> cap ;

    @PrePersist
    public void prePersist() {

        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
    }

}
