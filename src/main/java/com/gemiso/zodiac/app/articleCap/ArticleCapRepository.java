package com.gemiso.zodiac.app.articleCap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleCapRepository extends JpaRepository<ArticleCap, Long>, QuerydslPredicateExecutor<ArticleCap> {

    @Query("select a from ArticleCap a where a.article.artclId = :artclId")
    List<ArticleCap> findArticleCap(@Param("artclId") Long artclId);
}
