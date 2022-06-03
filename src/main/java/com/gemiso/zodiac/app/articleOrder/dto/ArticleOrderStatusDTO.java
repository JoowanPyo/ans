package com.gemiso.zodiac.app.articleOrder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleOrderStatusDTO {

    private String orderStatus;
    private String workrId;
}
