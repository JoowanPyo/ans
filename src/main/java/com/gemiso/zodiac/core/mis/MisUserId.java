package com.gemiso.zodiac.core.mis;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MisUserId implements Serializable {

    @Column(name = "USER_IDXX", length = 40)
    private String userIdxx;

    @Column(name = "USDN_CODE", length = 2)
    private String usdnCode;

}
