package com.gemiso.zodiac.app.articleActionLog.mapper;

import com.gemiso.zodiac.app.articleActionLog.ArticleActionLog;
import com.gemiso.zodiac.app.articleActionLog.dto.ArticleActionLogCreateDTO;
import com.gemiso.zodiac.core.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ArticleActionLogCreateMapper extends GenericMapper<ArticleActionLogCreateDTO, ArticleActionLog, ArticleActionLogCreateDTO> {
}
