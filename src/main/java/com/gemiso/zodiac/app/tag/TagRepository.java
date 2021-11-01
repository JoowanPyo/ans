package com.gemiso.zodiac.app.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> , QuerydslPredicateExecutor<Tag> {

    @Query("select a from Tag a where a.tagId = :tagId")
    Optional<Tag> findByTag(@Param("tagId")Long tagId);

    @Query("select a from Tag a where a.tag = :tag")
    Optional<Tag> findTagName(@Param("tag")String tag);
}
