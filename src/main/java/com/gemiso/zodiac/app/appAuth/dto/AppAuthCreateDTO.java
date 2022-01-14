package com.gemiso.zodiac.app.appAuth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "권한 등록 DTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppAuthCreateDTO {

    //@Schema(description = "권한 아이디")
    //private Long appAuthId;
    @Schema(description = "권한 명")
    private String appAuthNm;
    @Schema(description = "권한 코드")
    private String appAuthCd;
    @Schema(description = "순번")
    private int ord;
    @Schema(description = "설명")
    private String expl;
    @Schema(description = "메모")
    private String memo;
    @Schema(description = "상위 권한 코드")
    private String hrnkAppAuthCd;
    @Schema(description = "사용 여부")
    private String useYn;
    @Schema(description = "등록자 아이디")
    private String inputrId;


}
