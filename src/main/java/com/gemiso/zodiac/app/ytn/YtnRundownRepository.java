package com.gemiso.zodiac.app.ytn;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YtnRundownRepository extends JpaRepository<YtnRundown, Long> {

    @Query("select a from YtnRundown a where a.contId =:contId")
    List<YtnRundown> findByYtn(@Param("contId")String contId);
}
