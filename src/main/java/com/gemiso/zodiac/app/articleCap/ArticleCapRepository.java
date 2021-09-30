package com.gemiso.zodiac.app.articleCap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ArticleCapRepository extends JpaRepository<ArticleCap, Long>, QuerydslPredicateExecutor<ArticleCap> {
}
