package com.gemiso.zodiac.app.yonhapWire;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface YonhapWireRepository extends JpaRepository<YonhapWire, Long> , QuerydslPredicateExecutor<YonhapWire>, YonhapWireRepositoryCustom {


    @Query("select a from YonhapWire a where a.contId =:contId")
    List<YonhapWire> findYhArtclId(@Param("contId") String contId);

    //에딧넘버 + namContId로 기존에 등록된 REUTER정보가 있는지 확인 있으면 UPDATE 없으면 CREATE
    @Query("select a from YonhapWire a where a.mediaNo =:mediaNo and a.contId = :mamContId")
    List<YonhapWire> findReuter(@Param("mediaNo") String mediaNo, @Param("mamContId")String mamContId);

    @Query("select a from YonhapWire a where a.wireId =:yhWireId")
    Optional<YonhapWire> findYhWire(@Param("yhWireId")Long yhWireId);

}
