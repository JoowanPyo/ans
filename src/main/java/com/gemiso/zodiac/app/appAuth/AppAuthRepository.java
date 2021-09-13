package com.gemiso.zodiac.app.appAuth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppAuthRepository extends JpaRepository<AppAuth, Long>, QuerydslPredicateExecutor<AppAuth> {

    @Query("select MAX(a.ord) from AppAuth a where a.hrnkAppAuthCd =:hrnkCd ")
    Optional<Integer> findChildOrd(@Param("hrnkCd")String hrnkCd);

    @Query("select MAX(a.ord) from AppAuth a where a.hrnkAppAuthCd = '' ")
    Optional<Integer> findParentOrd();

    @Query("select a from AppAuth a where a.appAuthId = :appAuthId and a.delYn = 'N'")
    Optional<AppAuth> findByAppAuthId(@Param("appAuthId")Long appAuthId);

   /* @Query("select a from AppAuth a where a.useYn = :userYn and a.delYn =:delYn" +
            " and a.hrnkAppAuthCd = :hrnkAppAuthCd and a.appAuthNm like %:searchWord%")
    List<AppAuth> findAllAuth(String userYn,String delYn, String hrnkAppAuthCd, String searchWord);*/
}
