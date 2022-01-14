package com.gemiso.zodiac.app.articleActionLog.dto;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleActionLogCreateDTO {

    //private Long id;
    private String message;
    private String action;
    private String artclInfo;
    private String anchorCapInfo;
    private String artclCapInfo;
    private ArticleSimpleDTO article;
}
