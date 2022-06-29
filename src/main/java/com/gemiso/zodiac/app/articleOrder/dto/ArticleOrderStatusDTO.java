package com.gemiso.zodiac.app.articleOrder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleOrderStatusDTO {

    private String orderStatus;
    private String workrId;
}
