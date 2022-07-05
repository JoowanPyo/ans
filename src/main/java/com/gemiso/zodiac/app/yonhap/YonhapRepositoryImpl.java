package com.gemiso.zodiac.app.yonhap;

import com.gemiso.zodiac.app.yonhapAttchFile.QYonhapAttchFile;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Repository
public class YonhapRepositoryImpl implements YonhapRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public YonhapRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public Page<Yonhap> findByYonhapList(Date sdate, Date edate, String artclCateCd, /*List<String> region_cds,*/
                                         String search_word, String svcTyp,
                                         Pageable pageable) {

        QYonhap qYonhap = QYonhap.yonhap;
        QYonhapAttchFile qYonhapAttchFile = QYonhapAttchFile.yonhapAttchFile;

        JPAQuery jpaQuery = jpaQueryFactory.select(qYonhap).from(qYonhap)
                .leftJoin(qYonhap.yonhapAttchFiles, qYonhapAttchFile).fetchJoin().distinct();


        //날짜 조회조건이 들어온 경우
        if (ObjectUtils.isEmpty(sdate) ==false && ObjectUtils.isEmpty(edate) == false) {
            jpaQuery.where(qYonhap.trnsfDtm.between(sdate, edate));
        }
        //분류코드
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {

            jpaQuery.where(qYonhap.artclCateCd.eq(artclCateCd));
        }
        //통신사코드
        /*if (CollectionUtils.isEmpty(region_cds) == false) {
            jpaQuery.where(qYonhap.regionCd.in(region_cds));
        }*/
        //검색어
        if (search_word != null && search_word.trim().isEmpty() == false) {
            jpaQuery.where(qYonhap.artclTitl.contains(search_word));
        }
        //서비스 유형 ( 국문 AKRO, 영문 AENO )
        if (svcTyp != null && svcTyp.trim().isEmpty() == false){
            jpaQuery.where(qYonhap.svcTyp.eq(svcTyp));
        }

        jpaQuery.orderBy(qYonhap.trnsfDtm.desc());

        Long totalCount = jpaQuery.fetchCount();
        jpaQuery.offset(pageable.getOffset());
        jpaQuery.limit(pageable.getPageSize());

        List<Yonhap> yonhapList = jpaQuery.fetch();

        return new PageImpl<>(yonhapList, pageable, totalCount);
    }
}
