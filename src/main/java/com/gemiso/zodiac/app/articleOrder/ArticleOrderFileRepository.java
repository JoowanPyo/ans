package com.gemiso.zodiac.app.articleOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleOrderFileRepository extends JpaRepository<ArticleOrderFile, Long>, QuerydslPredicateExecutor<ArticleOrderFile> {

    @Query("select a from ArticleOrderFile a where a.articleOrder.orderId =:orderId")
    Optional<ArticleOrderFile> findOrderId(@Param("orderId")Long orderId);
}
