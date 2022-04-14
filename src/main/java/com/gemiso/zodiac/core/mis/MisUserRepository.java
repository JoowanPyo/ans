package com.gemiso.zodiac.core.mis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MisUserRepository extends JpaRepository<MisUser, String>, QuerydslPredicateExecutor<MisUser> {
}
