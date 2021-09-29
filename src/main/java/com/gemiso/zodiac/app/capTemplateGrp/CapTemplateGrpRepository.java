package com.gemiso.zodiac.app.capTemplateGrp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CapTemplateGrpRepository extends JpaRepository<CapTemplateGrp, Long> , QuerydslPredicateExecutor<CapTemplateGrp> {

    @Query("select a from CapTemplateGrp a where a.tmpltGrpId = :tmplGrpId and a.delYn = 'N'")
    Optional<CapTemplateGrp> findByTemplate(@Param("tmplGrpId")Long tmplGrpId);
}
