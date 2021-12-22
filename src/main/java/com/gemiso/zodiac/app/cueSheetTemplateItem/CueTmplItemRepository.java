package com.gemiso.zodiac.app.cueSheetTemplateItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CueTmplItemRepository extends JpaRepository<CueTmplItem, Long>, QuerydslPredicateExecutor<CueTmplItem> {

    @Query("select a from CueTmplItem a where a.cueTmpltItemId = :cueTmpltItemId and a.delYn = 'N'")
    Optional<CueTmplItem> findCueTmplItem(@Param("cueTmpltItemId")Long cueTmpltItemId);

    @Query("select a from CueTmplItem a where a.cueSheetTemplate.cueTmpltId=:cueTmpltId")
    List<CueTmplItem> findCueTmplItemList(@Param("cueTmpltId")Long cueTmpltId);
}
