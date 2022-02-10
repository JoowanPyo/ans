package com.gemiso.zodiac.app.yonhapPhoto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface YonhapPhotoRepository extends JpaRepository<YonhapPhoto, Long>, QuerydslPredicateExecutor<YonhapPhoto> {

    @Query("select a from YonhapPhoto a where a.contId =:condId")
    List<YonhapPhoto> findByYonhapPost(@Param("condId")String condId);
}
