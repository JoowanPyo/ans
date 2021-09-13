package com.gemiso.zodiac.app.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.nio.file.OpenOption;
import java.util.Optional;

public interface AttachFileRepository extends JpaRepository<AttachFile, Long> {

    @Query("select a from AttachFile a where a.fileId =:fileId")
    Optional<AttachFile> findAttachFile(@Param("fileId")Long fileId);
}
