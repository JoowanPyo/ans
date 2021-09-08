package com.gemiso.zodiac.app.yonhap;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface YonhapRepository extends JpaRepository<Yonhap, Long> , QuerydslPredicateExecutor<Yonhap> {

    @Query("select a from Yonhap a where a.contId = :condId")
    List<Yonhap> findByYonhap(@Param("condId")String condId);
}
