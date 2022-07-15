package com.gemiso.zodiac.app.scrollNewsFtpInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ScrollNewsFtpInfoRepository extends JpaRepository<ScrollNewsFtpInfo, Long> {

    @Query("select a from ScrollNewsFtpInfo a where a.id =:id ")
    Optional<ScrollNewsFtpInfo> findScrollFtpInfo(@Param("id")Long id);
}
