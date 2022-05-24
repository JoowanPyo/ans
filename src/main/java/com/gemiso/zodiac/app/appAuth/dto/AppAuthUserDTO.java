package com.gemiso.zodiac.app.appAuth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppAuthUserDTO {

    @Schema(description = "권한 명")
    private String appAuthNm;
    @Schema(description = "권한 코드")
    private String appAuthCd;
}
