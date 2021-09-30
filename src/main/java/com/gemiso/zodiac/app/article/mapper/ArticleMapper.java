package com.gemiso.zodiac.app.article.mapper;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.articleHist.mapper.ArticleHistMapper;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    @Mapping(target = "inputr.userId", source = "inputr.userId")
    @Mapping(target = "inputr.userNm", source = "inputr.userNm")
    @Mapping(target = "updtr.userId", source = "updtr.userId")
    @Mapping(target = "updtr.userNm", source = "updtr.userNm")
    @Mapping(target = "delr.userId", source = "delr.userId")
    @Mapping(target = "delr.userNm", source = "delr.userNm")
    @Mapping(target = "apprvr.userId", source = "apprvr.userId")
    @Mapping(target = "apprvr.userNm", source = "apprvr.userNm")
    @Mapping(target = "lckr.userId", source = "lckr.userId")
    @Mapping(target = "lckr.userNm", source = "lckr.userNm")
    @Mapping(target = "rptr.userId", source = "rptr.userId")
    @Mapping(target = "rptr.userNm", source = "rptr.userNm")
    ArticleDTO toDto(Article article);

    @Mapping(target = "inputr.userId", source = "inputr.userId")
    @Mapping(target = "inputr.userNm", source = "inputr.userNm")
    @Mapping(target = "updtr.userId", source = "updtr.userId")
    @Mapping(target = "updtr.userNm", source = "updtr.userNm")
    @Mapping(target = "delr.userId", source = "delr.userId")
    @Mapping(target = "delr.userNm", source = "delr.userNm")
    @Mapping(target = "apprvr.userId", source = "apprvr.userId")
    @Mapping(target = "apprvr.userNm", source = "apprvr.userNm")
    @Mapping(target = "lckr.userId", source = "lckr.userId")
    @Mapping(target = "lckr.userNm", source = "lckr.userNm")
    @Mapping(target = "rptr.userId", source = "rptr.userId")
    @Mapping(target = "rptr.userNm", source = "rptr.userNm")
    List<ArticleDTO> toDtoList(List<Article> articleList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ArticleDTO articleDTO, @MappingTarget Article article);

}
