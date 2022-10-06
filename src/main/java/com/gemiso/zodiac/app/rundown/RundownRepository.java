package com.gemiso.zodiac.app.rundown;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RundownRepository extends JpaRepository<Rundown, Long> , RundownRepositoryCustorm{

    @Query("select a from Rundown a where a.rundownId =:rundownId")
    Optional<Rundown> findRundown(@Param("rundownId")Long rundownId);
}
