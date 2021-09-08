package com.gemiso.zodiac.app.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    @Query("select a from UserToken a where a.userId =:userId")
    UserToken findUserToken(@Param("userId")String userId);
}
