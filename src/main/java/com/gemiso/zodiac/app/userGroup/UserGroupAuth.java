package com.gemiso.zodiac.app.userGroup;

import com.gemiso.zodiac.app.appAuth.AppAuth;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_user_grp_auth")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"userGroup, userAppAuth"})
public class UserGroupAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


   /* @NotFound(
            action = NotFoundAction.IGNORE)*/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_GRP_ID")
    private UserGroup userGroup;


    /*@NotFound(
            action = NotFoundAction.IGNORE)*/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "APP_AUTH_ID")
    private AppAuth appAuth;
}
