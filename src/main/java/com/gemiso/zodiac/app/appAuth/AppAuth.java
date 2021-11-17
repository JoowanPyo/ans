package com.gemiso.zodiac.app.appAuth;

import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.userGroup.UserGroupAuth;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_app_auth")
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
    @Column(name = "APP_AUTH_ID", nullable = false)
    private Long appAuthId;

    @Column(name = "APP_AUTH_NM", length = 100)
    private String appAuthNm;

    @Column(name = "app_auth_cd", length = 50)
    private String appAuthCd;

    @Column(name = "ORD", length = 4)
    private int ord;

    @Column(name = "EXPL", length = 2000)
    private String expl;

    @Column(name = "MEMO", length = 1000)
    private String memo;

    @Column(name = "HRNK_APP_AUTH_CD", length = 21)
    private String hrnkAppAuthCd;

    @Column(name = "USE_YN", columnDefinition = "bpchar(1) default 'Y'", nullable = false)
    private String useYn;

    @Column(name = "DEL_YN", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "DEL_DTM")
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
