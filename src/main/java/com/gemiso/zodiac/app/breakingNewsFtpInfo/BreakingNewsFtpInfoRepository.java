package com.gemiso.zodiac.app.breakingNewsFtpInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BreakingNewsFtpInfoRepository extends JpaRepository<BreakingNewsFtpInfo, Long> {

    @Query("select a from BreakingNewsFtpInfo a where a.id =:id ")
    Optional<BreakingNewsFtpInfo> findFtpInfo(@Param("id")Long id);
}
