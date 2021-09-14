package com.gemiso.zodiac.app.CueItemCap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CueItemCapRepotitory extends JpaRepository<CueItemCap, Long>, QuerydslPredicateExecutor<CueItemCap> {

    @Query("delete from CueItemCap a where a.cueItemId =:cueItemId and a.cueItemCapDivCd =:cueItemCapDivCd")
    void deleteCueSheeItemCap(@Param("cueItemId")Long cueItemId,
                              @Param("cueItemCapDivCd")String cueItemCapDivCd);

    @Query("select a from CueItemCap a where a.cueItemCapId =:cueItemCapId and a.delYn = 'N'")
    Optional<CueItemCap> findByCueItemCap(@Param("cueItemCapId")Long cueItemCapId);

    @Query("delete from CueItemCap a where a.cueItemId =:cueItemId and a.cueItemCapId =:cueItemCapId")
    void deleteCueSheeItemCapId(@Param("cueItemId")Long cueItemId,
                                @Param("cueItemCapId")Long cueItemCapId);
}
