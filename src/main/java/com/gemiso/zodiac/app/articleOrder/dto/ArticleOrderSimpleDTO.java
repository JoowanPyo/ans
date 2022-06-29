package com.gemiso.zodiac.app.articleOrder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleOrderSimpleDTO {

    private Long orderId;
    private String orderCtt;
    private String orderDivCd;
    private String orderDivCdNm;
    private String orderStatus;
    private int contentId;
    private String title;
    private String ordRmk;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String workrId;
    private String clientId;
    private Date inputDtm;
    private Date updtDtm;
    //private ArticleDTO article;
}
