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
public class ArticleTagDTO {

    private Long id;
    private TagDTO tag;
    private ArticleSimpleDTO article;
}
