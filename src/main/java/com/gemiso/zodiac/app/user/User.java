package com.gemiso.zodiac.app.user;

import com.gemiso.zodiac.app.userGroupUser.UserGroupUser;
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
@Table(
        name = "tb_user_mng",
        indexes = {@Index(name = "index_user_mng_user_nm", columnList = "user_nm")},
        uniqueConstraints = {
                @UniqueConstraint(name = "user_userId_unique", columnNames = "user_id")
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "userGroupUser")
@Setter
@DynamicUpdate
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "user_nm", length = 100, nullable = false)
    private String userNm;

    @Column(name = "pwd", length = 256, nullable = false)
    private String pwd;

    @Column(name = "empl_no", length = 20)
    private String emplNo;

    @Column(name = "free_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String freeYn;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "duty_cd", length = 50)
    private String dutyCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = duty_cd)")
    private String dutyCdNm;

    @Column(name = "chief_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String chiefYn;

    @Column(name = "email", length = 200)
    private String email;

    @Column(name = "tel", length = 20)
    private String tel;

    @Column(name = "tel_pub_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String telPubYn;

    @Column(name = "user_div_cd", length = 50)
    private String userDivCd;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select a.cd_nm from tb_cd a where a.cd = user_div_cd)")
    private String userDivCdNm;

    @Column(name = "memo", length = 1000)
    private String memo;

    @Column(name = "rmk", length = 500)
    private String rmk;

    @LastModifiedDate
    @Column(name = "last_login_dtm")
    private Date lastLoginDtm;

    @Column(name = "login_err_cnt", length = 4)
    private int loginErrCnt;

    @Column(name = "pwd_chg_dtm")
    private Date pwdChgDtm;

    @Column(name = "user_st_cd", length = 50)
    private String userStCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = user_st_cd)")
    private String userStCdNm;

    @Column(name = "use_start_dtm")
    private Date useStartDtm;

    @Column(name = "use_end_dtm")
    private Date useEndDtm;

    @Column(name = "use_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String useYn;

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

    @LastModifiedDate
    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "inphon_no", length = 20)
    private String inphonNo;

    @Column(name = "salt", length = 256)
    private String salt;

    @Column(name = "dept_cd", length = 50)
    private String deptCd;

    /*    @JoinTable(
            name = "tb_user_grp_user",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    foreignKey = @ForeignKey(name = "USER_ID")
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "USER_GRP_ID",
                    foreignKey = @ForeignKey(name = "USER_GRP_ID")
            )
    )*/

    @OneToMany(mappedBy = "user")//cascade = CascadeType.ALL은 부모가 삭제될때 자식도 같이 삭제
    private List<UserGroupUser> userGroupUser = new ArrayList<>();


    @PrePersist
    public void prePersist() {

        if (this.freeYn == null || this.freeYn == "") {
            this.freeYn = "N";
        }
        if (this.chiefYn == null || this.freeYn == "") {
            this.chiefYn = "N";
        }
        if (this.telPubYn == null || this.telPubYn == "") {
            this.telPubYn = "N";
        }
        if (this.useYn == null || this.useYn == "") {
            this.useYn = "N";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }
    }

    @PreUpdate //사실이건 필요없음
    public void preUpdate() {

        if (this.freeYn == null || this.freeYn == "") {
            this.freeYn = "N";
        }
        if (this.chiefYn == null || this.freeYn == "") {
            this.chiefYn = "N";
        }
        if (this.telPubYn == null || this.telPubYn == "") {
            this.telPubYn = "N";
        }
        if (this.useYn == null || this.useYn == "") {
            this.useYn = "N";
        }
        if (this.delYn == null || this.delYn == "") {
            this.delYn = "N";
        }

    }

}
