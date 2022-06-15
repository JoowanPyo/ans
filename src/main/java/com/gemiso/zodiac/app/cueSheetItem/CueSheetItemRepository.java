package com.gemiso.zodiac.app.cueSheetItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CueSheetItemRepository extends JpaRepository<CueSheetItem, Long>, QuerydslPredicateExecutor<CueSheetItem>, CueSheetItemRepositoryCustorm {

    @Query("select a from CueSheetItem a left outer join Article b on b.artclId = a.article.artclId and b.delYn = 'N' " +
            "left outer join ArticleMedia c on c.article.artclId = b.artclId and c.delYn = 'N' " +
            "where a.article.artclId =:artclId and a.cueSheet.cueId =:cueId and a.delYn=:delYn and a.spareYn =:spareYn ")
    List<CueSheetItem> findAllItems(@Param("artclId")Long artclId, @Param("cueId")Long cueId,
                                    @Param("delYn")String delYn, @Param("spareYn")String spareYn);

    @Query("select a from CueSheetItem a left outer join Article b on b.artclId = a.article.artclId and b.delYn = 'N' " +
            "where a.cueSheet.cueId = :cueId  and a.delYn = 'N' and a.spareYn = 'N' order by a.cueItemOrd asc ")
    List<CueSheetItem> findByCueItemList(@Param("cueId")Long cueId);

    @Query("select a from CueSheetItem a left outer join Article b on b.artclId = a.article.artclId " +
            "where a.cueSheet.cueId = :cueId  and a.delYn = 'N' and a.spareYn = :spareYn order by a.cueItemOrd asc ")
    List<CueSheetItem> findByCueItemListSpareYn(@Param("cueId")Long cueId, @Param("spareYn")String spareYn);

    @Query("select a from CueSheetItem a where a.cueItemId = :cueItemId and a.delYn = 'N' ")
    Optional<CueSheetItem> findByCueItem(@Param("cueItemId")Long cueItemId);

    @Query("select a from CueSheetItem a where a.article.artclId = :artclId and a.delYn = 'N' ")
    Optional<CueSheetItem> findByCueItemArticle(@Param("artclId")Long artclId);


    @Query("select a from CueSheetItem a where a.delYn = 'N' and a.article.artclId =:artclId and a.article.delYn = 'N'")
    Optional<CueSheetItem> findArticleCue(@Param("artclId")Long artclId);

    @Query("select a from CueSheetItem a where a.cueSheet.cueId =:cueId and a.delYn =:del_yn and a.spareYn = 'Y' order by a.cueItemOrd asc ")
    List<CueSheetItem> findSpareCue(@Param("cueId") Long cueId, @Param("del_yn") String del_yn);

    @Query("select a from CueSheetItem a where a.cueItemId = :cueItemId and a.delYn = 'Y'")
    Optional<CueSheetItem> findDeletedCueItem(@Param("cueItemId")Long cueItemId);

    @Query("select a from CueSheetItem a where a.cueSheet.cueId = :cueId")
    List<Long> findCueItemId(@Param("cueId")Long cueId);
}
