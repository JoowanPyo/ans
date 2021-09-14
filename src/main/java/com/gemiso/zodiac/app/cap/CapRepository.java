package com.gemiso.zodiac.app.cap;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CapRepository extends JpaRepository<Cap, Long>, QuerydslPredicateExecutor<Cap> {

    @Query("select a from Cap a where a.capTmpltId =:capTmpltId and a.delYn = 'N'")
    Optional<Cap> finByCap(@Param("capTmpltId")Long capTmpltId);

    @Query("select max(a.capTmpltOrd) from Cap a")
    Optional<Integer> findOrd();

    @Query("select a from Cap a where a.delYn = 'N' order by a.capTmpltOrd ASC ")
    List<Cap> findCapList();
}
