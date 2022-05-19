package com.gemiso.zodiac.app.articleActionLog.dto;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
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
