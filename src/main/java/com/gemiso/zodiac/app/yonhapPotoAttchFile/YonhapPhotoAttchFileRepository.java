package com.gemiso.zodiac.app.yonhapPotoAttchFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YonhapPhotoAttchFileRepository extends JpaRepository<YonhapPhotoAttchFile, Long> {

    @Query("select a from YonhapPhotoAttchFile a where a.id = :yonhapPotoId")
    List<YonhapPhotoAttchFile> findYonhapPhoto(@Param("yonhapPotoId") Long yonhapPotoId);
}
