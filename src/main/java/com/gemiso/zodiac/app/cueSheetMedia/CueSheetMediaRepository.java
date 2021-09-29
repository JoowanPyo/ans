package com.gemiso.zodiac.app.cueSheetMedia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CueSheetMediaRepository extends JpaRepository<CueSheetMedia, Long>, QuerydslPredicateExecutor<CueSheetMedia> {
}
