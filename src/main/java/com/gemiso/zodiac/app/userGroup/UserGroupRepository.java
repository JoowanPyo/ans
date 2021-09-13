package com.gemiso.zodiac.app.userGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> , QuerydslPredicateExecutor<UserGroup> {

    @Query("select a from UserGroup a where a.userGrpId = :userGrpId and a.delYn = 'N'")
    Optional<UserGroup> findByUserGroupId(@Param("userGrpId")Long userGrpId);
}
