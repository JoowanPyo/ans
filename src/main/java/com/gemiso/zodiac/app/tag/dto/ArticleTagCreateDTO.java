package com.gemiso.zodiac.app.tag.dto;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTagCreateDTO {

    //private Long id;
    private TagIdDTO tag;
    private ArticleSimpleDTO article;
}
