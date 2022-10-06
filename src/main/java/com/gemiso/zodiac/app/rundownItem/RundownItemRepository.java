package com.gemiso.zodiac.app.rundownItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RundownItemRepository extends JpaRepository<RundownItem, Long> ,RundownItemRepositoryCustorm {


    @Query("select a from RundownItem a where a.rundownItemId =:rundownItemId ")
    Optional<RundownItem> findRundownItem(@Param("rundownItemId") Long rundownItemId);

    @Query("select a from RundownItem a where a.rundown.rundownId =:rundownId order by a.rundownItemOrd asc ")
    List<RundownItem> findByRundownId(@Param("rundownId") Long rundownId);
}
