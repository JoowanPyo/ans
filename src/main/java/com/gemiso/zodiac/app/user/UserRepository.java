package com.gemiso.zodiac.app.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> , QuerydslPredicateExecutor<User> {

    @Query("select a, c.userGroupAuths from User a left outer join UserGroupUser bb on bb.user = a.userId " +
            "left outer join UserGroup c on c.userGrpId = bb.userGroup.userGrpId where a.userId = :userId " +
            "and a.delYn = 'N'")
    User getUserWithGroup(@Param("userId") String userId);

    @Query("select a from User a where a.userId =:userId and a.delYn = 'N'")
    Optional<User> findByUserId(@Param("userId")String userId);

    @Query("select a from User a where a.userId =:userId and a.delYn ='Y'")
    Optional<User> findDeleteUser(@Param("userId")String userId);

    @Query("select b from UserGroupUser a left outer join User b on b.userId = a.user.userId " +
            "where a.userGroup.userGrpId = :userGrpId")
    List<User> findByUser(@Param("userGrpId")Long userGrpId);
}
