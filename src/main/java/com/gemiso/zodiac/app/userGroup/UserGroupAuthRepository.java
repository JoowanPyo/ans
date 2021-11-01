package com.gemiso.zodiac.app.userGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserGroupAuthRepository extends JpaRepository<UserGroupAuth, Long>, QuerydslPredicateExecutor<UserGroupAuth> {

    @Query("select a from UserGroupAuth a where a.userGroup.userGrpId = :userGrpId")
    List<UserGroupAuth> findByUserGrpId(@Param("userGrpId") Long userGrpId);

    @Query("select a from UserGroupAuth a where a.userGroup.userGrpId IN (:userGrpId)")
    List<UserGroupAuth> findByUserGrpIdArr(@Param("userGrpId") Long[] userGrpId);
}
