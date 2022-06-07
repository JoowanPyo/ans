package com.gemiso.zodiac.app.baseProgram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BaseProgramRepository extends JpaRepository<BaseProgram, Long>, QuerydslPredicateExecutor<BaseProgram> {

    @Query("select a from BaseProgram a where a.basePgmschId = :basePgmschId and a.delYn = 'N'")
    Optional<BaseProgram> findBasePgm(@Param("basePgmschId")Long basePgmschId);

    @Query("select a from BaseProgram a where a.program.brdcPgmId=:brdcPgmId and a.brdcStartClk =:formatBoradHm")
    Optional<BaseProgram> findByBasePropram(@Param("brdcPgmId")String brdcPgmId, @Param("formatBoradHm")String formatBoradHm);
}
