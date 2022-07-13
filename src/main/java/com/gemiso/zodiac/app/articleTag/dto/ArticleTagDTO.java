package com.gemiso.zodiac.app.articleTag.dto;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.tag.dto.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTagDTO {

    private Long id;
    private TagDTO tag;
    private ArticleSimpleDTO article;
}
