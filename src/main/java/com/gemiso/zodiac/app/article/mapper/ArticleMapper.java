package com.gemiso.zodiac.app.article.mapper;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.articleHist.mapper.ArticleHistMapper;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "inputr.userId", source = "inputr.userId")
    @Mapping(target = "inputr.userNm", source = "inputr.userNm")
    @Mapping(target = "updtr.userId", source = "updtr.userId")
    @Mapping(target = "updtr.userNm", source = "updtr.userNm")
    @Mapping(target = "issue.issuId", source = "issue.issuId")
    @Mapping(target = "issue.issuKwd", source = "issue.issuKwd")
    ArticleDTO toDto(Article article);

    List<ArticleDTO> toDtoList(List<Article> articleList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ArticleDTO articleDTO, @MappingTarget Article article);

}
