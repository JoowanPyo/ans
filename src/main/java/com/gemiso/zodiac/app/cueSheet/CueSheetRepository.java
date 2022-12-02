package com.gemiso.zodiac.app.cueSheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CueSheetRepository extends JpaRepository<CueSheet, Long>, QuerydslPredicateExecutor<CueSheet>, CueSheetRepositoryCustorm {

    @Query("select a from CueSheet a where a.cueId =:cueId and a.delYn ='N' ")
    Optional<CueSheet> findByCue(@Param("cueId")Long cueId);

    @Query("select a from CueSheet a where a.cueId =:cueId and a.delYn =:del_yn ")
    Optional<CueSheet> findTakerCue(@Param("cueId") Long cueId, @Param("del_yn") String del_yn);

    @Query("select count(a) from CueSheet a where a.brdcDt =:brdcDt and " +
            "((a.brdcStartTime > :brdcStartTime and a.brdcStartTime < :brdcEndTime)" +
            " or (a.brdcEndTime > :brdcStartTime and a.brdcEndTime < :brdcEndTime) or " +
            "(a.brdcStartTime <=:brdcStartTime and a.brdcEndTime >=:brdcEndTime)) and a.delYn = 'N' ")
    Integer findCueProgram(@Param("brdcDt")String brdcDt,@Param("brdcStartTime")String brdcStartTime
            , @Param("brdcEndTime")String brdcEndTime);

    @Query("select count(a) from CueSheet a where a.brdcDt =:brdcDt and a.program.brdcPgmId =:brdcPgmId and a.delYn = 'N'")
    Integer findCueProgram2(@Param("brdcDt")String brdcDt,@Param("brdcPgmId")String brdcPgmId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select a from CueSheet a where a.cueId =:cueId and a.delYn ='N' ")
    Optional<CueSheet> findCueLock(@Param("cueId")Long cueId);

    @Query("select a from CueSheet a where a.lckDtm <:formatDate and a.lckYn =:lckYn ")
    List<CueSheet> findLockCueChkList(@Param("formatDate")Date formatDate, @Param("lckYn")String lckYn);

    @Query("select a from CueSheet a where a.lckrId =:userId and a.lckYn =:lckYn ")
    List<CueSheet> findLockCueList(@Param("userId")String userId, @Param("lckYn")String lckYn);

    @Query("select a from CueSheet a where a.brdcDt =:statsDate and a.cueStCd ='end_on_air' and a.delYn ='N' ")
    List<CueSheet> findStatsCue(@Param("statsDate") String statsDate);


}
