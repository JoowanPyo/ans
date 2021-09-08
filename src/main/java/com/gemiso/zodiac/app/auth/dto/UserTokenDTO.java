package com.gemiso.zodiac.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenDTO {

    private Long id;
    private String userId;
    private String refreshToken;
    private Date expirationDtm;
}
