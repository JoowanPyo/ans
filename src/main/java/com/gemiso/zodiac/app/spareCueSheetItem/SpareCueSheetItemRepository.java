package com.gemiso.zodiac.app.spareCueSheetItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpareCueSheetItemRepository extends JpaRepository<SpareCueSheetItem, Long>, QuerydslPredicateExecutor<SpareCueSheetItem> {

    @Query("select a from SpareCueSheetItem a where a.spareCueItemId =:spareCueItemId and a.delYn = 'N'")
    Optional<SpareCueSheetItem> findSareCueItem(@Param("spareCueItemId")Long spareCueItemId);

    @Query("select a from SpareCueSheetItem a where a.cueSheet.cueId=:cueId and a.delYn = 'N'")
    List<SpareCueSheetItem> findSareCueItemList(@Param("cueId")Long cueId);
}
