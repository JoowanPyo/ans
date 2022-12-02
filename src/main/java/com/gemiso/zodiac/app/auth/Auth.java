package com.gemiso.zodiac.app.auth;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "tb_user_login",
        uniqueConstraints = {
                @UniqueConstraint(name = "login_Id_unique", columnNames = "id")
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Setter
@DynamicUpdate
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "token", length = 200)
    private String token;

    @Column(name = "user_nm", length = 100)
    private String userNm;

    @Column(name = "dept_id")
    private Long deptId;

    @Column(name = "dept_nm", length = 100)
    private String deptNm;

    @Column(name = "login_dtm", nullable = false)
    private Date loginDtm;

    @Column(name = "login_ip", length = 15)
    private String loginIp;

    @Column(name = "logout_dtm")
    private Date logoutDtm;

    @Column(name = "st_cd", length = 50)
    private String stCd;

    @Formula("(select a.cd_nm from tb_cd a where a.cd = st_cd)")
    private String stCdNm;

    @Column(name = "client_ver", length = 50)
    private String clientVer;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "user_login_typ", length = 50)
    private String userLoginTyp;

}
