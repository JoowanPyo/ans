package com.gemiso.zodiac.app.appAuth.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Schema(description = "권한 DTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppAuthDTO {

    @Schema(description = "권한 아이디")
    private Long appAuthId;
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
    @Schema(description = "삭제 여부")
    private String delYn;
    @Schema(description = "사용 여부")
    private String useYn;
    @Schema(description = "등록 일시")
    private Date inputDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "수정 일시")
    private Date updtDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @Schema(description = "삭제 일시")
    private Date delDtm;
    @Schema(description = "등록자 아이디")
    private String inputrId;
    @Schema(description = "수정자 아이디")
    private String updtrId;
    @Schema(description = "삭제자 아이디")
    private String delrId;
    @Schema(description = "등록자 명")
    private String inputrNm;
    @Schema(description = "수정자 명")
    private String updtrNm;
    @Schema(description = "삭제자 명")
    private String delrNm;

}
