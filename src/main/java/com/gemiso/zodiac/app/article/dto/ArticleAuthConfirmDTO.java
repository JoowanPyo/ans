package com.gemiso.zodiac.app.article.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleAuthConfirmDTO {

    private Long artclId;
    private String lckYn;
    private Date lckDtm;
    private String lckrId;
    private String lckrNm;
    private String msg;
    private String authCode;
    private HttpStatus httpStatus;
}
