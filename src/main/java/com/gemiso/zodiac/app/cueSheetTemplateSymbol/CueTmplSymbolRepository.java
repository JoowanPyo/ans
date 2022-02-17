package com.gemiso.zodiac.app.cueSheetTemplateSymbol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CueTmplSymbolRepository extends JpaRepository<CueTmplSymbol, Long> {

    @Query("select a from CueTmplSymbol a where a.cueTmpltItem.cueTmpltItemId = :cueTmpltItemId")
    List<CueTmplSymbol> findCueTmplSymbol(@Param("cueTmpltItemId")Long cueTmpltItemId);

}
