package com.gemiso.zodiac.app.yonhapWire;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YonhapWireRepository extends JpaRepository<YonhapWire, Long> , QuerydslPredicateExecutor<YonhapWire> {


    @Query("select a from YonhapWire a where a.contId = :contId")
    List<YonhapWire> findYhArtclId(@Param("contId") String contId);

}
