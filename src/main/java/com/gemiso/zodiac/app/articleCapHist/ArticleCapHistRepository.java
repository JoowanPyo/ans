package com.gemiso.zodiac.app.articleCapHist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleCapHistRepository extends JpaRepository<ArticleCapHist, Long>, QuerydslPredicateExecutor<ArticleCapHist> {

    @Query("select a from ArticleCapHist a where a.artclCapHistId = :artclCapHistId")
    Optional<ArticleCapHist> findArticleCapHist(@Param("artclCapHistId")Long artclCapHistId);
}
