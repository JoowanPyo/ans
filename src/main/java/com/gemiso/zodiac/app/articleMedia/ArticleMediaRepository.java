package com.gemiso.zodiac.app.articleMedia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleMediaRepository extends JpaRepository<ArticleMedia, Long>, QuerydslPredicateExecutor<ArticleMedia>, ArticleMediaRepositoryCustorm {

    @Query("select a from ArticleMedia a where a.artclMediaId = :artclMediaId and a.delYn = 'N'")
    Optional<ArticleMedia> findByArticleMedia(@Param("artclMediaId")Long artclMediaId);

    @Query("select a from ArticleMedia a where a.article.artclId = :artclId and a.delYn='N' order by a.artclMediaId desc ")
    List<ArticleMedia> findArticleMediaList(@Param("artclId")Long artclId);

    @Query("select a from ArticleMedia a where a.article.artclId = :artclId and a.delYn='Y' order by a.artclMediaId desc ")
    List<ArticleMedia> findDeleteArticleMediaList(@Param("artclId")Long artclId);

    @Query("select a from ArticleMedia a where a.contId =:contentId and a.delYn='N' order by a.artclMediaId desc ")
    List<ArticleMedia> findArticleMediaListByContentId(@Param("contentId")Integer contentId);


}
