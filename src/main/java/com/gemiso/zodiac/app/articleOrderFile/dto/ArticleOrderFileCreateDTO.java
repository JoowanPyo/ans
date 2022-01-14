package com.gemiso.zodiac.app.articleOrderFile.dto;

import com.gemiso.zodiac.app.articleOrder.dto.ArticleOrderSimpleDTO;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleOrderFileCreateDTO {

    //private Long id;
    @NotNull
    private ArticleOrderSimpleDTO articleOrder;
    @NotNull
    private AttachFileDTO file;
}
