package com.gemiso.zodiac.app.cueSheetTemplateItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CueTmpltItemRepository extends JpaRepository<CueTmpltItem, Long>, QuerydslPredicateExecutor<CueTmpltItem> {

    @Query("select a from CueTmpltItem a left outer join CueTmpltMedia b on b.cueTmpltItem.cueTmpltItemId = a.cueTmpltItemId and b.delYn = 'N' where a.cueTmpltItemId = :cueTmpltItemId and a.delYn = 'N' ")
    Optional<CueTmpltItem> findCueTmplItem(@Param("cueTmpltItemId")Long cueTmpltItemId);

    @Query("select a from CueTmpltItem a where a.cueSheetTemplate.cueTmpltId=:cueTmpltId and a.delYn = 'N'")
    List<CueTmpltItem> findCueTmplItemList(@Param("cueTmpltId")Long cueTmpltId);

    @Query("select max(a.cueItemOrd) from  CueTmpltItem a where a.cueSheetTemplate.cueTmpltId =:cueTmpltId and a.delYn = 'N' ")
    Integer findOrd(@Param("cueTmpltId")Long cueTmpltId);
}
