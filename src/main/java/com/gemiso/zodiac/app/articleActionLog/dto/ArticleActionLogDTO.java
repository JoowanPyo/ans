package com.gemiso.zodiac.app.articleActionLog.dto;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleActionLogDTO {

    private Long id;
    private String message;
    private String action;
    private Date inputDtm;
    private String inputrId;
    private String inputrNm;
    private String artclInfo;
    private String anchorCapInfo;
    private String artclCapInfo;
    private ArticleSimpleDTO article;


}
