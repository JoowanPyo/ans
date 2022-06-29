package com.gemiso.zodiac.app.article;

import com.gemiso.zodiac.app.article.dto.ArticleElasticLockInfoDTO;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, QuerydslPredicateExecutor<Article>, ArticleRepositoryCustorm {

    @Query("select a from Article a where a.artclId =:articleId and a.delYn = 'N' ")
    Optional<Article> findArticle(@Param("articleId") Long articleId);

    @Query("select a from Article a where a.artclId =:articleId and a.delYn = 'Y' ")
    Optional<Article> findDeleteArticle(@Param("articleId") Long articleId);

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

    @Query("select a from Article a where a.lckrId =:userId and a.lckYn =:lckYn and a.delYn = 'N'")
    List<Article> findLockArticleList(@Param("userId") String userId, @Param("lckYn") String lckYn);

    @Query("select a from Article a where a.lckDtm <:formatDate and a.lckYn =:lckYn ")
    List<Article> findLockChkList(@Param("formatDate") Date formatDate, @Param("lckYn")String lckYn);


    @Query("select new com.gemiso.zodiac.app.article.dto.ArticleElasticLockInfoDTO(a.artclId, a.lckYn, a.lckDtm, a.lckrId, a.lckrNm)  from Article a where a.artclId in(:artclIds) ")
    List<ArticleElasticLockInfoDTO> findLockArticleListElastic(@Param("artclIds") List<Long> artclIds);

    //@Query(value = "SELECT NEXT VALUE FOR seq_org_artclId", nativeQuery = true)
    //@Query("select schema.sequence.NEXTVAL.seq_org_artclId ")
    //@Query(value = "SELECT seq_org_artclId.nextval from dual", nativeQuery = true)
    @Query(value = "SELECT nextval('seq_org_artclId')", nativeQuery = true)
    Long findOrgArticleId(); //할수없이 네이티브 쿼리 사용...

}
