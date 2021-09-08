package com.gemiso.zodiac.app.user;

import com.gemiso.zodiac.core.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(
        name = "tb_user_mng",
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

    @Column(name = "dept_id", length = 21)
    private String deptId;

    @Column(name = "duty_cd", length = 50)
    private String dutyCd;

    @Column(name = "chief_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String chiefYn;

    @Column(name = "e_mail", length = 200)
    private String eMail;

    @Column(name = "tel", length = 200)
    private String tel;

    @Column(name = "tel_pub_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String telPubYn;

    @Column(name = "user_div_cd", length = 50)
    private String userDivCd;

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
    private String userStDt;

    @Column(name = "use_start_dtm")
    private Date useStartDtm;

    @Column(name = "use_end_dtm")
    private Date useEndDtm;

    @Column(name = "use_yn", columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String useYn;

    @Column(name = "del_yn",columnDefinition = "bpchar(1) default 'N'", nullable = false)
    private String delYn;

    @Column(name = "inputr_id", length = 50)
    private String inputrId;

    @Column(name = "updtr_id", length = 50)
    private String updtrId;

    @Column(name = "delr_id", length = 50)
    private String delrId;

    @LastModifiedDate
    @Column(name = "del_dtm")
    private Date delDtm;

    @Column(name = "inphon_no", length = 20)
    private String inphonNo;

    @Column(name = "salt", length = 256)
    private String salt;

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

    @OneToMany(mappedBy="user")//cascade = CascadeType.ALL은 부모가 삭제될때 자식도 같이 삭제
    private List<UserGroupUser> userGroupUser = new ArrayList<>();


    @PrePersist
    public void prePersist() {

        if (this.freeYn == null || this.freeYn == ""){
            this.freeYn = "N";
        }
        if (this.chiefYn == null || this.freeYn == ""){
            this.chiefYn = "N";
        }
        if (this.telPubYn == null || this.telPubYn == ""){
            this.telPubYn = "N";
        }
        if (this.useYn == null || this.useYn == ""){
            this.useYn = "N";
        }
        if (this.delYn == null || this.delYn == ""){
            this.delYn = "N";
        }
    }
    @PreUpdate //사실이건 필요없음
    public void preUpdate(){

        if (this.freeYn == null || this.freeYn == ""){
            this.freeYn = "N";
        }
        if (this.chiefYn == null || this.freeYn == ""){
            this.chiefYn = "N";
        }
        if (this.telPubYn == null || this.telPubYn == ""){
            this.telPubYn = "N";
        }
        if (this.useYn == null || this.useYn == ""){
            this.useYn = "N";
        }
        if (this.delYn == null || this.delYn == ""){
            this.delYn = "N";
        }

    }


    //private Set<Role> roles = new HashSet<>();

    //public void changePassword(String newPassword) {
    //    password = newPassword;
    //}

}
