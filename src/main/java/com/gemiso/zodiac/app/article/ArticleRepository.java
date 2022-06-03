package com.gemiso.zodiac.app.article;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, QuerydslPredicateExecutor<Article>, ArticleRepositoryCustorm {

    @Query("select a from Article a where a.artclId =:articleId and a.delYn = 'N' ")
    Optional<Article> findArticle(@Param("articleId") Long articleId);


    @Query("select a from Article a where a.inputrId = :userId and a.delYn = 'N'")
    List<Article> findMyArticle(@Param("userId") String userId);

    @Query("select a from Article a where a.orgArtclId =:orgArtclId and a.delYn = 'N'")
    List<Article> findCopyArticle(@Param("orgArtclId") Long orgArtclId);

    @Query("select max(a.artclOrd) from Article a where a.orgArtclId =:orgArtclId")
    Optional<Integer> findArticleOrd(@Param("orgArtclId") Long orgArtclId);

    @Query("select count(a.cueSheet) from Article a where a.cueSheet.cueId =:cueId and a.delYn = 'N'")
    int findArticleCount(@Param("cueId") Long cueId);

    @Query("select max(a.artclOrd) from Article a where a.orgArtclId =:orgArtclId and a.delYn = 'N'")
    List<Article> getMaxOrd(@Param("orgArtclId") Long orgArtclId);


    @Query("select distinct a from Article a left join a.articleCap left join a.articleMedia left join a.anchorCap ")
    Page<Article> findAllArticle(BooleanBuilder booleanBuilder, PageRequest pageRequest);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from Article a where a.artclId=:artclId and a.delYn = 'N'")
    Optional<Article> findLockArticle(@Param("artclId") Long artclId);


}
