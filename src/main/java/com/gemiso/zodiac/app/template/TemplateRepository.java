package com.gemiso.zodiac.app.template;

import com.gemiso.zodiac.app.template.dto.TemplateDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> , QuerydslPredicateExecutor<Template> {

    @Query("select a from Template a where a.tmpltGrpId = :tmplGrpId and a.delYn = 'N'")
    Optional<Template> findByTemplate(@Param("tmplGrpId")Long tmplGrpId);
}
