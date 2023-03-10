package com.gemiso.zodiac.app.cueSheetItemSymbol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CueSheetItemSymbolRepository extends JpaRepository<CueSheetItemSymbol, Long> {

    @Query("select a from CueSheetItemSymbol a where a.cueSheetItem.cueItemId = :cueItemId order by a.ord asc ")
    List<CueSheetItemSymbol> findSymbol(@Param("cueItemId")Long cueItemId);

}
