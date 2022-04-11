package com.gemiso.zodiac.app.articleHist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleHistRepository extends JpaRepository<ArticleHist, Long>, QuerydslPredicateExecutor<ArticleHist> {

    @Query("select a from ArticleHist a where a.artclHistId = :artclHistId")
    Optional<ArticleHist> findByArticleHist(@Param("artclHistId")Long artclHistId);

    @Query("select count(a.ver) from ArticleHist a where a.article.artclId = :artclId")
    int findArticleHistByArticle(@Param("artclId")Long artclId);
}
