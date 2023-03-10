package com.gemiso.zodiac.app.userGroup;

import com.gemiso.zodiac.app.userGroupUser.UserGroupUser;
import com.gemiso.zodiac.app.userGroupAuth.UserGroupAuth;
import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "tb_user_grp",
        indexes = { @Index(name = "index_user_grp_nm", columnList = "user_grp_nm")}
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"userGroupUsers","userGroupAuths"})
@DynamicUpdate
public class UserGroup extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_grp_id", nullable = false)
    private Long userGrpId;

    @Column(name = "user_grp_nm", length = 100)
    private String userGrpNm;

    @Column(name = "memo", length = 1000)
    private String memo;

    @Column(name = "ord", length = 4)
    private int ord;

    @Column(name = "use_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String useYn;

    @Column(name = "del_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @LastModifiedDate
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

    @OneToMany(mappedBy="userGroup")
    private List<UserGroupUser> userGroupUsers ;

    @OneToMany(mappedBy="userGroup")
    private List<UserGroupAuth> userGroupAuths ;

    @PrePersist
    public void prePersist() {

        if (this.useYn == null || this.useYn == "") {
            this.useYn = "N";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
    }
    @PreUpdate //???????????? ????????????
    public void preUpdate(){

        if (this.useYn == null || this.useYn == "") {
            this.useYn = "N";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }

    }

}
