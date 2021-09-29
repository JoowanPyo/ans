package com.gemiso.zodiac.app.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, QuerydslPredicateExecutor<Article> {

    @Query("select a from Article a " +
            "left outer join Issue f on f.issuId = a.issue.issuId " +
            "left outer join ArticleHist b on b.article.artclId = a.artclId " +
            "left outer join ArticleCap  c on c.article.artclId = a.artclId " +
            "left outer join ArticleMedia d on d.article.artclId = a.artclId " +
            "left outer join ArticleOrder e on e.article.artclId = a.artclId " +
            "where a.artclId =:articleId")
    Optional<Article> findArticle(@Param("articleId")Long articleId);
}
