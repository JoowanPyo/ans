package com.gemiso.zodiac.app.articleMedia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleMediaRepository extends JpaRepository<ArticleMedia, Long>, QuerydslPredicateExecutor<ArticleMedia> {

    @Query("select a from ArticleMedia a where a.artclMediaId = :artclMediaId and a.delYn = 'N'")
    Optional<ArticleMedia> findByArticleMedia(@Param("artclMediaId")Long artclMediaId);
}
