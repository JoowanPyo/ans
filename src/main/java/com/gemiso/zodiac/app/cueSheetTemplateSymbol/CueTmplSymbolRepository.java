package com.gemiso.zodiac.app.cueSheetTemplateSymbol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CueTmplSymbolRepository extends JpaRepository<CueTmplSymbol, Long>, QuerydslPredicateExecutor<CueTmplSymbol> {

    @Query("select a from CueTmplSymbol a where a.cueTmpltItem.cueTmpltItemId = :cueTmpltItemId order by a.ord asc ")
    List<CueTmplSymbol> findCueTmplSymbol(@Param("cueTmpltItemId")Long cueTmpltItemId);

    @Query("select a from CueTmplSymbol a where a.id = :id")
    Optional<CueTmplSymbol> findByCueTmplSymbol(@Param("id")Long id);


}
