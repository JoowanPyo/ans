package com.gemiso.zodiac.app.yonhapWire;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Repository
public class YonhapWireRepositoryImpl implements YonhapWireRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public YonhapWireRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public Page<YonhapWire> findYonhapWireList(Date sdate,
                                             Date edate,
                                             String agcyCd,
                                             String agcyNm,
                                             String source,
                                             String svcTyp,
                                             String searchWord,
                                             List<String> imprtList,
                                             Pageable pageable,
                                               String mediaNo) {

        QYonhapWire qYonhapWire = QYonhapWire.yonhapWire;


        JPAQuery jpaQuery = jpaQueryFactory.select(qYonhapWire).from(qYonhapWire).distinct();



        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){
            jpaQuery.where(qYonhapWire.trnsfDtm.between(sdate, edate));
        }
        if (agcyCd != null && agcyCd.trim().isEmpty() == false){
            jpaQuery.where(qYonhapWire.agcyCd.eq(agcyCd));
        }
        if (agcyNm != null && agcyNm.trim().isEmpty() == false){
            jpaQuery.where(qYonhapWire.agcyNm.eq(agcyNm));
        }
        if (source != null && source.trim().isEmpty() == false){
            jpaQuery.where(qYonhapWire.source.eq(source));
        }
        if (svcTyp != null && svcTyp.trim().isEmpty() == false){
            jpaQuery.where(qYonhapWire.svcTyp.eq(svcTyp));
        }
        if (searchWord != null && searchWord.trim().isEmpty() == false){
            jpaQuery.where(qYonhapWire.artclTitl.contains(searchWord).or(qYonhapWire.mediaNo.eq(searchWord)));
        }
        if (ObjectUtils.isEmpty(imprtList) == false){

            jpaQuery.where(qYonhapWire.imprt.in(imprtList));

        }

        jpaQuery.orderBy(qYonhapWire.trnsfDtm.desc());

        Long totalCount = jpaQuery.fetchCount();
        jpaQuery.offset(pageable.getOffset());
        jpaQuery.limit(pageable.getPageSize());

        List<YonhapWire> yonhapWireList = jpaQuery.fetch();;

        return new PageImpl<>(yonhapWireList, pageable, totalCount);
    }
}
