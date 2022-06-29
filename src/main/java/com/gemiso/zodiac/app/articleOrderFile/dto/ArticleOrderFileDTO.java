package com.gemiso.zodiac.app.articleOrderFile.dto;

import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderSimpleDTO;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleOrderFileDTO {

    private Long id;
    private ArticleOrderSimpleDTO articleOrder;
    private AttachFileDTO file;
    private String url;
}
