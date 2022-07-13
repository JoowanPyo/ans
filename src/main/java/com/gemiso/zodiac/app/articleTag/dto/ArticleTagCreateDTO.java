package com.gemiso.zodiac.app.articleTag.dto;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.tag.dto.TagIdDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTagCreateDTO {

    //private Long id;
    private TagIdDTO tag;
    private ArticleSimpleDTO article;
}
