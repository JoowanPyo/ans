package com.gemiso.zodiac.app.nod;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NodRepository extends JpaRepository<Nod, Long> {

    @Query("select a from Nod a where a.nodId =:nodId ")
    Optional<Nod> findNod(@Param("nodId")Long nodId);


    @Query("select a from Nod a where a.cueId =:cueId")
    Optional<Nod> findNodByCue(@Param("cueId")Long cueId);
}
