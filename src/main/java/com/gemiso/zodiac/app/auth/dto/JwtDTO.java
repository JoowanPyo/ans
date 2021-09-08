package com.gemiso.zodiac.app.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtDTO {

    private String accessToken;
    private String refreshToken;
    private int expirationIn;
}
