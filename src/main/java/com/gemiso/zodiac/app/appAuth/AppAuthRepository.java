package com.gemiso.zodiac.app.appAuth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface AppAuthRepository extends JpaRepository<AppAuth, Long>, QuerydslPredicateExecutor<AppAuth> {

    @Query("select MAX(a.ord) from AppAuth a where a.hrnkAppAuthCd =:hrnkCd ")
    int findChildOrd(@Param("hrnkCd")String hrnkCd);

    @Query("select MAX(a.ord) from AppAuth a where a.hrnkAppAuthCd = '' ")
    int findParentOrd();

   /* @Query("select a from AppAuth a where a.useYn = :userYn and a.delYn =:delYn" +
            " and a.hrnkAppAuthCd = :hrnkAppAuthCd and a.appAuthNm like %:searchWord%")
    List<AppAuth> findAllAuth(String userYn,String delYn, String hrnkAppAuthCd, String searchWord);*/
}
