package com.gemiso.zodiac.app.cueSheetTemplateMedia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CueTmpltMediaRepository extends JpaRepository<CueTmpltMedia, Long>, QuerydslPredicateExecutor<CueTmpltMedia> {

    @Query("select a from CueTmpltMedia a where a.cueTmpltMediaId =:cueTmpltMediaId ")
    Optional<CueTmpltMedia> findCueTmpltMedia(@Param("cueTmpltMediaId")Long cueTmpltMediaId);
}
