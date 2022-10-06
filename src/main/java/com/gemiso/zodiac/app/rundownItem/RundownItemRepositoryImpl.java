package com.gemiso.zodiac.app.rundownItem;

import com.gemiso.zodiac.app.rundown.QRundown;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;

public class RundownItemRepositoryImpl implements  RundownItemRepositoryCustorm{


    private final JPAQueryFactory jpaQueryFactory;


    @Autowired
    public RundownItemRepositoryImpl(JPAQueryFactory jpaQueryFactory) {

        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<RundownItem> findAllItems(Long rundownId) {

        QRundown qRundown = QRundown.rundown;
        QRundownItem qRundownItem = QRundownItem.rundownItem;

        JPAQuery jpaQuery = jpaQueryFactory.select(qRundownItem).from(qRundownItem)
                .leftJoin(qRundownItem.rundown, qRundown).fetchJoin().distinct();

        if (ObjectUtils.isEmpty(rundownId) == false){
            jpaQuery.where(qRundownItem.rundown.rundownId.eq(rundownId));

            jpaQuery.orderBy(qRundownItem.rundownItemOrd.asc());

            return jpaQuery.fetch();
        }else {
            return null;
        }

    }
}
