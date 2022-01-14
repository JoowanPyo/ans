package com.gemiso.zodiac.app.articleOrder.dto;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleOrderCreateDTO {

    //private Long orderId;
    private String orderCtt;
    private String orderDivCd;
    //private String orderDivCdNm;
    private String orderStatus;
    private int contentId;
    private String title;
    private String ordRmk;
    private String inputrId;
    //private String inputrNm;
    //private String updtrId;
    //private String updtrNm;
    private String workrId;
    private String clientId;
    private Date inputDtm;
    //private Date updtDtm;
    @NotNull
    private ArticleSimpleDTO article;
}
