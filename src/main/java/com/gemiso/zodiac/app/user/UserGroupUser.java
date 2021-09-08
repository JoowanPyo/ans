package com.gemiso.zodiac.app.user;

import com.gemiso.zodiac.app.userGroup.UserGroup;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_user_grp_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "user, userGroup")
public class UserGroupUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@NotFound(
           action = NotFoundAction.IGNORE)*/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;

    /*@NotFound(
           action = NotFoundAction.IGNORE)*/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_GRP_ID")
    private UserGroup userGroup;
}
