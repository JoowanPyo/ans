package com.gemiso.zodiac.app.cueCapTmplt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CueCapTmpltRepository extends JpaRepository<CueCapTmplt, Long>, QuerydslPredicateExecutor<CueCapTmplt> {

    @Query("select a from CueCapTmplt a where a.id = :id")
    Optional<CueCapTmplt> findCueCapTmplt(@Param("id")Long id);

    @Query("select a from CueCapTmplt a where a.program.brdcPgmId =:brdcPgmId")
    List<CueCapTmplt> findCueCapTmpltList(@Param("brdcPgmId")String brdcPgmId);

    @Query("select a from CueCapTmplt a where a.program.brdcPgmId =:brdcPgmId")
    List<CueCapTmplt> findCueCapTmpltByPgmId(@Param("brdcPgmId") String brdcPgmId);
}
