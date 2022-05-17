package com.gemiso.zodiac.app.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, QuerydslPredicateExecutor<Article> {

    @Query("select a from Article a " +
            "left outer join Issue f on f.issuId = a.issue.issuId and f.issuDelYn = 'N' " +
            "left outer join ArticleCap  c on c.article.artclId = a.artclId " +
            "left outer join AnchorCap h on h.article.artclId = a.artclId " +
            "left outer join CueSheet g on g.cueId = a.cueSheet.cueId and g.delYn = 'N' " +
            "where a.artclId =:articleId and a.delYn = 'N' ")
    Optional<Article> findArticle(@Param("articleId")Long articleId);


    @Query("select a from Article a where a.inputrId = :userId and a.delYn = 'N'")
    List<Article> findMyArticle(@Param("userId")String userId);

    @Query("select a from Article a where a.orgArtclId =:orgArtclId and a.delYn = 'N'")
    List<Article> findCopyArticle(@Param("orgArtclId")Long orgArtclId);

    @Query("select max(a.artclOrd) from Article a where a.orgArtclId =:orgArtclId")
    Optional<Integer> findArticleOrd(@Param("orgArtclId")Long orgArtclId);

    @Query("select count(a.cueSheet) from Article a where a.cueSheet.cueId =:cueId")
    int findArticleCount(@Param("cueId")Long cueId);

    @Query("select max(a.artclOrd) from Article a where a.orgArtclId =:orgArtclId and a.delYn = 'N'")
    List<Article> getMaxOrd(@Param("orgArtclId")Long orgArtclId);

}
