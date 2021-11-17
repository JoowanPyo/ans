package com.gemiso.zodiac.app.userSet;

import com.gemiso.zodiac.app.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(
        name = "tb_user_set"
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "user")
@Setter
@DynamicUpdate
public class UserSet {

    @Id
    @Column(name = "user_set_key", length = 50, nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "user_set_val", columnDefinition = "json")
    private String userSetVal;

}
