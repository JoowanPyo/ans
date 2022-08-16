package com.gemiso.zodiac.app.articleActionLog.dto;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import lombok.*;
import org.json.simple.JSONObject;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleActionLogQueryDslDTO {

    private Long id;
    private String message;
    private String action;
    private Date inputDtm;
    private String inputrId;
    private String inputrNm;
    private String artclInfo;
    private String anchorCapInfo;
    private String artclCapInfo;
    private Article article;
}
