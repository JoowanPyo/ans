package com.gemiso.zodiac.app.rundown;

import com.gemiso.zodiac.app.rundownItem.QRundownItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

public class RundownRepositoryImpl implements RundownRepositoryCustorm{

    private final JPAQueryFactory jpaQueryFactory;


    @Autowired
    public RundownRepositoryImpl(JPAQueryFactory jpaQueryFactory) {

        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Rundown> findRundowns(Date rundownDt, String rundownTime) {

        QRundown qRundown = QRundown.rundown;
        QRundownItem qRundownItem = QRundownItem.rundownItem;

        JPAQuery jpaQuery = jpaQueryFactory.select(qRundown).from(qRundown)
                .leftJoin(qRundown.rundownItems, qRundownItem).fetchJoin().distinct();


        if (ObjectUtils.isEmpty(rundownDt) == false){
            jpaQuery.where(qRundown.rundownDt.eq(rundownDt));
        }else {
            Date now = new Date();
            jpaQuery.where(qRundown.rundownDt.eq(now));
        }

        if (rundownTime != null && rundownTime.trim().isEmpty() == false){
            jpaQuery.where(qRundown.rundownTime.eq(rundownTime));
        }

        return jpaQuery.fetch();
    }
}
