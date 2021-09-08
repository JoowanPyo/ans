package com.gemiso.zodiac.app.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    @Query("select a from Auth a where a.userId = :userId")
    Auth findByLogin(@Param("userId")String userId);
}
