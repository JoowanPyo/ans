package com.gemiso.zodiac.app.articleOrder.dto;

import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleOrderFileUpdateDTO {

    private Long id;
    //private ArticleOrderSimpleDTO articleOrder;
    private AttachFileDTO file;
}
