package com.gemiso.zodiac.app.userGroupUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserGroupUserRepository extends JpaRepository<UserGroupUser, Long>, QuerydslPredicateExecutor<UserGroupUser> {

    @Query("select a.id from UserGroupUser a where a.user = :userId")
    Long[] getOrgUserGroupUserId(@Param("userId")String userId);

    @Query("select a from UserGroupUser a left outer join  UserGroup b on b.userGrpId = a.userGroup.userGrpId " +
            "where a.user.userId = :userId ")
    List<UserGroupUser> findByUserId(@Param("userId")String userId);

   /* @Query("select a from UserGroupUser a where a.userGroup.userGrpId = :userGrpId")
    UserGroupUser findByUsergrpId(@Param("userGrpId")String userGrpId);*/


    @Query("select a from UserGroupUser a where a.user.userId = :userId and a.userGroup.userGrpId = :userGrpId")
    Optional<UserGroupUser> findAllByUserId(@Param("userId")String userId,
                                           @Param("userGrpId")Long userGrpId);

    @Query("select a from UserGroupUser a where a.user.userId=:userId")
    Optional<UserGroupUser> findByUserGroup(@Param("userId")String userId);


}
