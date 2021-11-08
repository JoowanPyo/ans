package com.gemiso.zodiac.app.code;

import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(
        name = "tb_cd"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
public class Code extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cd_id", nullable = false)
    private Long cdId;

    @Column(name = "cd", length = 50)
    private String cd;

    @Column(name = "cd_nm", length = 100)
    private String cdNm;

    @Column(name = "cd_expl", length = 500)
    private String cdExpl;

    @Column(name = "hrnk_cd_id", length = 21)
    private String hrnkCdId;

    @Column(name = "use_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String useYn;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "cd_ord")
    private Integer cdOrd;

    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "inputr_id")
    private String inputrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = inputr_id)")
    private String inputrNm;

    @Column(name = "updtr_id")
    private String updtrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = updtr_id)")
    private String updtrNm;

    @Column(name = "delr_id")
    private String delrId;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.user_nm from tb_user_mng a where a.user_id = delr_id)")
    private String delrNm;

    @PrePersist
    public void prePersist() {

        if (this.useYn == null || this.useYn == "") {
            this.useYn = "Y";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }

    }

}
