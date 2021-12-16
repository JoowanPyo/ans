package com.gemiso.zodiac.app.program;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProgramRepository extends JpaRepository<Program, Long> , QuerydslPredicateExecutor<Program> {

    @Query("select a from Program a where a.brdcPgmId = :brdcPgmId and a.delYn = 'N'")
    Optional<Program> findByProgramId(@Param("brdcPgmId")String brdcPgmId);
}
