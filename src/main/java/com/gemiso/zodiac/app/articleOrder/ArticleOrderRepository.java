package com.gemiso.zodiac.app.articleOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ArticleOrderRepository extends JpaRepository<ArticleOrder, Long>, QuerydslPredicateExecutor<ArticleOrder> {
}
