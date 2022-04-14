package com.gemiso.zodiac.app.dept;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeptsRepository extends JpaRepository<Depts, Long> {

    @Query("select a from Depts a where a.code = :misUpprDtcd")
    Optional<Depts> findDeptByParentCode(@Param("misUpprDtcd") String misUpprDtcd);

    @Query("select a from Depts a where a.code = :misDeptCode")
    Optional<Depts> findByCode(@Param("misDeptCode") String misDeptCode);

}
