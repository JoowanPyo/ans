package com.gemiso.zodiac.app.issue;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

public interface IssueRepositoy extends JpaRepository<Issue, Long>, QuerydslPredicateExecutor<Issue> {

    @Query("select max(a.issuOrd) from Issue a where a.inputDtm between :sdate and :edate")
    Optional<Integer> findByOrd(@Param("sdate") Date sdate, @Param("edate")Date edate);

    @Query("select a from Issue a where a.issuId = :issuId and a.issuDelYn = 'N'")
    Issue findByIssuId(@Param("issuId")Long issuId);

   /* @Query("select a.chDivCd , a.issuDtm, a.issuOrd, a.issuKwd, a.issuCtt, a.issuFnshYn," +
            " a.issuDelYn, a.issuFnshDtm, a.issuOrgId, a.inputrId from Issue a " +
            "where a.issuId =:issuId and a.issuDelYn ='N'")
    Issue findOrgIssue(@Param("issuId")Long issuId);*/
}
