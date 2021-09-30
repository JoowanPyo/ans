package com.gemiso.zodiac.app.articleOrder.dto;

import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleOrderDTO {

    private Long orderId;
    private String orderCtt;
    private String orderDivCd;
    private String orderStatus;
    private int contentId;
    private String title;
    private String ordRmk;
    private UserSimpleDTO inputr;
    private UserSimpleDTO updtr;
    private String workrId;
    private String clientId;
    private Date inputDtm;
    private Date updtDtm;
    private ArticleDTO article;
}
