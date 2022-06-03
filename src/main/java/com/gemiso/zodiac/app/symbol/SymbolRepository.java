package com.gemiso.zodiac.app.symbol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SymbolRepository extends JpaRepository<Symbol, Long>, QuerydslPredicateExecutor<Symbol> {

    @Query("select a from Symbol a left outer join AttachFile b on b.fileId = a.attachFile.fileId " +
            "where a.symbolId = :symbolId and a.delYn = 'N'")
    Optional<Symbol> findBySymbolId(@Param("symbolId")String symbolId);

    @Query("select a from Symbol a where a.typCd =:typCd and a.delYn = 'N' order by a.symbolOrd asc ")
    List<Symbol> findSymbolList(@Param("typCd")String typCd);
}
