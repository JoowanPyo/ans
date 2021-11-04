package com.gemiso.zodiac.app.facilityManage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface FacilityManageRepository extends JpaRepository<FacilityManage, Long>, QuerydslPredicateExecutor<FacilityManage> {
}
