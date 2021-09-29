package com.gemiso.zodiac.app.cueSheetItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CueSheetItemRepository extends JpaRepository<CueSheetItem, Long>, QuerydslPredicateExecutor<CueSheetItem> {

    @Query("select a from CueSheetItem a where a.cueId = :cueId and a.delYn = 'N' order by a.cueItemOrd asc")
    List<CueSheetItem> findByCueItemList(@Param("cueId")Long cueId);

    @Query("select a from CueSheetItem a where a.cueItemId = :cueItemId and a.delYn = 'N'")
    Optional<CueSheetItem> findByCueItem(@Param("cueItemId")Long cueItemId);
}
