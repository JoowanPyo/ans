package com.gemiso.zodiac.app.anchorCapHist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnchorCapHistRepository extends JpaRepository<AnchorCapHist, Long>, QuerydslPredicateExecutor<AnchorCapHist> {

    @Query("select a from AnchorCapHist a where a.ancCapHistId = :ancCapHistId")
    Optional<AnchorCapHist> findAnchorCapHist(@Param("ancCapHistId")Long ancCapHistId);
}
