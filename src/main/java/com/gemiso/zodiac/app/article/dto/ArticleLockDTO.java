package com.gemiso.zodiac.app.article.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleLockDTO {

    @Schema(description = "잠금 여부")
    private String lckYn;
    @Schema(description = "잠금 일시")
    private Date lckDtm;
    @Schema(description = "잠금자 아이디")
    private String lckrId;

}
