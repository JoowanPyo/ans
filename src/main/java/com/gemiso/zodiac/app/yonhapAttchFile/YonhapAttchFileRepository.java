package com.gemiso.zodiac.app.yonhapAttchFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface YonhapAttchFileRepository extends JpaRepository<YonhapAttchFile, Long> {

    @Query("select a from YonhapAttchFile a where a.yonhap.yonhapId = :id")
    List<YonhapAttchFile> findId(@Param("id")Long id);

    @Query("select a from YonhapAttchFile a where a.attachFile.fileId = :fileId")
    List<YonhapAttchFile> findFile(@Param("fileId")Long fileId);
}
