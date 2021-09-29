package com.gemiso.zodiac.app.ArticleOrder.dto;

import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleOrderSimpleDTO {

    private Long orderId;
    private String orderCtt;
    private String orderDivCd;
    private String orderStatus;
    private int contentId;
    private String title;
    private String ordRmk;
    private String inputrId;
    private String updtrId;
    private String workrId;
    private String clientId;
    private Date inputDtm;
    private Date updtDtm;
    //private ArticleDTO article;
}
