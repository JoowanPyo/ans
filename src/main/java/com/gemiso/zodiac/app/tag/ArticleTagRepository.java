package com.gemiso.zodiac.app.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleTagRepository extends JpaRepository<ArticleTag, Long> {

    @Query("select a from ArticleTag a where a.article.artclId = :articleId")
    List<ArticleTag> findArticleTag(@Param("articleId")Long articleId);
}
