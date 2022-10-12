package com.gemiso.zodiac.app.rundownItem;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.rundown.QRundown;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.ObjectUtils;

import java.util.Date;
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

    @Override
    public Page<RundownItem> findMyRundown(Date sdate, Date edate, String rptrId, Pageable pageable) {

        QRundown qRundown = QRundown.rundown;
        QRundownItem qRundownItem = QRundownItem.rundownItem;

        JPAQuery jpaQuery = jpaQueryFactory.select(qRundownItem).from(qRundownItem)
                .leftJoin(qRundownItem.rundown, qRundown).fetchJoin().distinct();

        //등록날짜 기준으로 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            jpaQuery.where(qRundownItem.brdcDt.between(sdate, edate)
                    /*.or(qArticle.embgDtm.between(sdate, edate)
                            .or(qArticle.brdcSchdDtm.between(sdate, edate)))*/);
        }

        if (rptrId != null && rptrId.trim().isEmpty() == false){
            jpaQuery.where(qRundownItem.rptrId.eq(rptrId));
        }

        jpaQuery.offset(pageable.getOffset());
        jpaQuery.limit(pageable.getPageSize());

        jpaQuery.orderBy(qRundownItem.rundownItemOrd.asc());

        Long totalCount = jpaQuery.fetchCount();


        List<RundownItem> rundownItems = jpaQuery.fetch();

        return new PageImpl<>(rundownItems, pageable, totalCount);
    }

}
