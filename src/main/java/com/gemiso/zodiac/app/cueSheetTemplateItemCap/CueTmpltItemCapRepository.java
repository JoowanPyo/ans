package com.gemiso.zodiac.app.cueSheetTemplateItemCap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CueTmpltItemCapRepository extends JpaRepository<CueTmpltItemCap, Long>, QuerydslPredicateExecutor<CueTmpltItemCap> {

    @Query("select a from CueTmpltItemCap a where a.cueItemCapId =:cueItemCapId and a.delYn ='N'")
    Optional<CueTmpltItemCap> findCueTmpltItemCap(@Param("cueItemCapId")Long cueItemCapId);

    @Query("select a from CueTmpltItemCap a where a.cueTmpltItem.cueTmpltItemId = :cueTmpltItemId and a.delYn ='N'")
    List<CueTmpltItemCap> findCueTmpltItemCapList(@Param("cueTmpltItemId")Long cueTmpltItemId);
}
