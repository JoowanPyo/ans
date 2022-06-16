package com.gemiso.zodiac.app.dailyProgram;

import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

public class DailyProgramRepositoryImpl implements DailyProgramRepositoryCustorn{

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public DailyProgramRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<DailyProgram> findByDailyProgramList(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm,
                                                     String brdcDivCd, Long stdioId, Long subrmId, String searchWord) {


        QDailyProgram qDailyProgram = QDailyProgram.dailyProgram;

        JPAQuery jpaQuery = jpaQueryFactory.select(qDailyProgram).from(qDailyProgram).distinct();

        //날짜조회시 방송일자 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){

            DateChangeHelper dateChangeHelper = new DateChangeHelper();

            String StringSdate = dateChangeHelper.dateToStringNoTime(sdate); //Date To String( yyyy-MM-dd )
            String stringEdate = dateChangeHelper.dateToStringNoTime(edate); //Date To String( yyyy-MM-dd )
            jpaQuery.where(qDailyProgram.brdcDt.between(StringSdate, stringEdate));
        }
        //조회조건이 방송구분 코드로 들어온 경우
        if (brdcDivCd != null && brdcDivCd.trim().isEmpty() == false){
            jpaQuery.where(qDailyProgram.brdcDivCd.eq(brdcDivCd));
        }
        if(brdcPgmId != null && brdcPgmId.trim().isEmpty() == false){
            jpaQuery.where(qDailyProgram.program.brdcPgmId.eq(brdcPgmId));
        }
        if(brdcPgmNm != null && brdcPgmNm.trim().isEmpty() == false){
            jpaQuery.where(qDailyProgram.brdcPgmNm.contains(brdcPgmNm));
        }
        //스튜디어 아이디가 조회조건으로 들어온경우
        if (ObjectUtils.isEmpty(stdioId) == false){
            jpaQuery.where(qDailyProgram.stdioId.eq(stdioId));
        }
        //부조 아이디가 조회조건으로 들어온 경우
        if (ObjectUtils.isEmpty(subrmId) == false){
            jpaQuery.where(qDailyProgram.subrmId.eq(subrmId));
        }
        //방송프로그램명이 조회조건으로 들어온 경우
        if (searchWord != null && searchWord.trim().isEmpty() == false){
            jpaQuery.where(qDailyProgram.brdcPgmNm.contains(searchWord));
        }

        jpaQuery.orderBy(qDailyProgram.brdcDt.desc(), qDailyProgram.brdcStartTime.asc());


        return jpaQuery.fetch();
    }
}
