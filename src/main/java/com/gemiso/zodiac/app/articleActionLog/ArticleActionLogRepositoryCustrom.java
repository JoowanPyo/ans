package com.gemiso.zodiac.app.articleActionLog;

import java.util.List;

public interface ArticleActionLogRepositoryCustrom {

    List<ArticleActionLog> findArticleActionLogList(Long artclId);
}
