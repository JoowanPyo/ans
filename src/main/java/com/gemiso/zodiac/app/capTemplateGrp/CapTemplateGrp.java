package com.gemiso.zodiac.app.capTemplateGrp;

import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.user.User;
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
public class CapTemplateGrp extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inputr_id", nullable = false)
    private User inputr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updtr_id")
    private User updtr;

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
