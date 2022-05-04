package com.gemiso.zodiac.app.appAuth;

import com.gemiso.zodiac.app.userGroupAuth.UserGroupAuth;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_app_auth",
indexes = {@Index(name = "index_app_auth_hrnk_cd", columnList = "hrnk_app_auth_cd"),
        @Index(name = "index_app_auth_app_auth_nm", columnList = "app_auth_nm")})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "userGroupAuth")
@DynamicUpdate
public class AppAuth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_auth_id", nullable = false)
    private Long appAuthId;

    @Column(name = "app_auth_nm", length = 100)
    private String appAuthNm;

    @Column(name = "app_auth_cd", length = 50)
    private String appAuthCd;

    @Column(name = "ord", length = 4)
    private int ord;

    @Column(name = "expl", length = 2000)
    private String expl;

    @Column(name = "memo", length = 1000)
    private String memo;

    @Column(name = "hrnk_app_auth_cd", length = 21)
    private String hrnkAppAuthCd;

    @Column(name = "use_yn", columnDefinition = "bpchar(1) default 'Y'", nullable = false)
    private String useYn;

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

    @OneToMany(mappedBy="appAuth")
    private List<UserGroupAuth> userGroupAuth = new ArrayList<>();

    @PrePersist
    public void prePersist() {

        if (this.useYn == null || this.useYn == "") {
            this.useYn = "Y";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
    }

    @PreUpdate
    public void preUpdate() {

        if (this.useYn == null || this.useYn == "") {
            this.useYn = "Y";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
    }
}
