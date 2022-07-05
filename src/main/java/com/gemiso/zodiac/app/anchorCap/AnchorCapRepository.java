package com.gemiso.zodiac.app.anchorCap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnchorCapRepository extends JpaRepository<AnchorCap, Long>, QuerydslPredicateExecutor<AnchorCap> {

    @Query("select a from AnchorCap a where a.anchorCapId=:anchorCapId")
    Optional<AnchorCap> findAnchorCap(@Param("anchorCapId")Long anchorCapId);

    @Query("select a from AnchorCap a where a.article.artclId =:artclId order by a.lnOrd asc ")
    List<AnchorCap> findAnchorCapList(@Param("artclId")Long artclId);
}
