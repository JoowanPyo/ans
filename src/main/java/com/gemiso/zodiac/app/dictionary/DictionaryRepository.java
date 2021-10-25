package com.gemiso.zodiac.app.dictionary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DictionaryRepository extends JpaRepository<Dictionary, Long>, QuerydslPredicateExecutor<Dictionary> {

    @Query("select a from Dictionary a where a.id=:id and a.delYn = 'N'")
    Optional<Dictionary> findByDicId(@Param("id")Long id);
}
