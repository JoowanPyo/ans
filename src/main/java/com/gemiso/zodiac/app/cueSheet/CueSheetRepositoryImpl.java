package com.gemiso.zodiac.app.cueSheet;

import com.gemiso.zodiac.app.baseProgram.QBaseProgram;
import com.gemiso.zodiac.app.program.QProgram;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Repository
public class CueSheetRepositoryImpl implements CueSheetRepositoryCustorm {

    private final JPAQueryFactory jpaQueryFactory;


    @Autowired
    public CueSheetRepositoryImpl(JPAQueryFactory jpaQueryFactory) {

        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<CueSheet> findByCueSheetList(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, Long deptCd, String searchWord) {

        QCueSheet qCueSheet = QCueSheet.cueSheet;
        QProgram qProgram = QProgram.program;
        QBaseProgram qBaseProgram = QBaseProgram.baseProgram;

        JPAQuery jpaQuery = jpaQueryFactory.select(qCueSheet).from(qCueSheet)
                .leftJoin(qCueSheet.program, qProgram).fetchJoin()
                .leftJoin(qCueSheet.baseProgram, qBaseProgram).fetchJoin().distinct();

        jpaQuery.where(qCueSheet.delYn.eq("N"));

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){
            DateChangeHelper dateChangeHelper = new DateChangeHelper();
            String StringSdate = dateChangeHelper.dateToStringNoTime(sdate);//Date To String( yyyy-MM-dd )
            String stringEdate = dateChangeHelper.dateToStringNoTime(edate);//Date To String( yyyy-MM-dd )
            jpaQuery.where(qCueSheet.brdcDt.between(StringSdate, stringEdate));
        }
        if(brdcPgmId != null && brdcPgmId.trim().isEmpty() == false){
            jpaQuery.where(qCueSheet.program.brdcPgmId.eq(brdcPgmId));
        }
        if(brdcPgmNm != null && brdcPgmNm.trim().isEmpty() == false){
            jpaQuery.where(qCueSheet.brdcPgmNm.contains(brdcPgmNm));
        }
        if(searchWord != null && searchWord.trim().isEmpty() == false){
            jpaQuery.where(qCueSheet.brdcPgmNm.contains(searchWord).or(qCueSheet.anc1Nm.contains(searchWord))
                    .or(qCueSheet.pd2Nm.contains(searchWord)));
        }
        if (ObjectUtils.isEmpty(deptCd) == false){
            jpaQuery.where(qCueSheet.deptCd.eq(deptCd));
        }

        jpaQuery.orderBy(qCueSheet.brdcDt.asc(), qCueSheet.brdcStartTime.asc());

        return jpaQuery.fetch();
    }

}
