package com.gemiso.zodiac.app.articleActionLog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleActionLogRepository extends JpaRepository<ArticleActionLog, Long>, QuerydslPredicateExecutor<ArticleActionLog>, ArticleActionLogRepositoryCustrom {

    @Query("select a from ArticleActionLog a where a.id = :id")
    Optional<ArticleActionLog> findArticleActionLog(@Param("id")Long id);
}
