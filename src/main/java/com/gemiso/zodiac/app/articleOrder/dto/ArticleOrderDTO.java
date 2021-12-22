package com.gemiso.zodiac.app.articleOrder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapSimpleDTO;
import com.gemiso.zodiac.app.articleOrder.ArticleOrderFile;
import com.gemiso.zodiac.app.code.dto.CodeSimpleDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleOrderDTO {

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
    private String workrNm;
    private String clientId;
    private String clientNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updtDtm;
    private ArticleSimpleDTO article;
    private List<ArticleOrderFileDTO> articleOrderFile = new ArrayList<>();
}
