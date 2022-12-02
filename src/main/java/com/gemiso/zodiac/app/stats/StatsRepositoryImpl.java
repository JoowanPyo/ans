package com.gemiso.zodiac.app.stats;

import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Repository
public class StatsRepositoryImpl implements StatsRepositoryCustorm{

    private final JPAQueryFactory jpaQueryFactory;


    @Autowired
    public StatsRepositoryImpl(JPAQueryFactory jpaQueryFactory) {

        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Stats> findStats(String sdate, String edate) {

        QStats qStats = QStats.stats;

        JPAQuery jpaQuery = jpaQueryFactory.select(qStats).from(qStats).distinct();

        //등록날짜 기준으로 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {

            jpaQuery.where(qStats.statsDt.between(sdate, edate));

        }else {

            DateChangeHelper dateChangeHelper = new DateChangeHelper();

            Date now = new Date();
            String startDate = dateChangeHelper.dateToStringNoTime(now);

            jpaQuery.where(qStats.statsDt.between(startDate, startDate));
        }

        jpaQuery.orderBy(qStats.statsDt.asc());


        return jpaQuery.fetch();
    }
}
