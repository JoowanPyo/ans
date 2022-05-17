package com.gemiso.zodiac.app.cueSheetItemCap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CueSheetItemCapRepotitory extends JpaRepository<CueSheetItemCap, Long>, QuerydslPredicateExecutor<CueSheetItemCap> {

    @Query("delete from CueSheetItemCap a where a.cueSheetItem.cueItemId =:cueItemId and a.cueItemCapDivCd =:cueItemCapDivCd and a.delYn = 'N' ")
    void deleteCueSheeItemCap(@Param("cueItemId")Long cueItemId,
                              @Param("cueItemCapDivCd")String cueItemCapDivCd);

    @Query("select a from CueSheetItemCap a where a.cueItemCapId =:cueItemCapId and a.delYn = 'N' and a.delYn = 'N' ")
    Optional<CueSheetItemCap> findByCueItemCap(@Param("cueItemCapId")Long cueItemCapId);

    @Query("delete from CueSheetItemCap a where a.cueSheetItem.cueItemId =:cueItemId and a.cueItemCapId =:cueItemCapId and a.delYn = 'N' ")
    void deleteCueSheeItemCapId(@Param("cueItemId")Long cueItemId,
                                @Param("cueItemCapId")Long cueItemCapId);

    @Query("select a from CueSheetItemCap a where a.cueSheetItem.cueItemId = :cueItemId and a.delYn = 'N' ")
    List<CueSheetItemCap> findCueSheetItemCapList(@Param("cueItemId")Long cueItemId);
}
