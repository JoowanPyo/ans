package com.gemiso.zodiac.app.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    @Query("select a from UserToken a where a.userId =:userId")
    Optional<UserToken> findUserToken(@Param("userId")String userId);
}
