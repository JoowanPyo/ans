package com.gemiso.zodiac.app.fileFtpInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileFtpInfoRepository extends JpaRepository<FileFtpInfo, Long> {

    @Query("select a from FileFtpInfo a where a.id = :id")
    Optional<FileFtpInfo> findFtpInfo(@Param("id") Long id);
}
