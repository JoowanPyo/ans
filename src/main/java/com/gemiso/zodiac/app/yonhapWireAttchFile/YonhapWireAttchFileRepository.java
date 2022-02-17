package com.gemiso.zodiac.app.yonhapWireAttchFile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YonhapWireAttchFileRepository extends JpaRepository<YonhapWireAttchFile, Long> {

    @Query("select a from YonhapWireAttchFile a where a.yonhapWire.wireId = :yhWireId")
    List<YonhapWireAttchFile> findByYonhapWireFile(@Param("yhWireId")Long yhWireId);
}
