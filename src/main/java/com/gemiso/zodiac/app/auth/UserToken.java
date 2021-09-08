package com.gemiso.zodiac.app.auth;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        name = "tb_user_token",
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
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "refresh_token", length = 256, nullable = false)
    private String refreshToken;

    @Column(name = "expiration_dtm", nullable = false)
    private Date expirationDtm;

}
