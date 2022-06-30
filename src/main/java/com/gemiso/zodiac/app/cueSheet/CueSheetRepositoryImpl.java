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

import java.text.SimpleDateFormat;
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

    @Override
    public List<CueSheet> findNodCue(Date sdate, Date edate, String cueDivCd) {

        QCueSheet qCueSheet = QCueSheet.cueSheet;
        QProgram qProgram = QProgram.program;
        QBaseProgram qBaseProgram = QBaseProgram.baseProgram;

        JPAQuery jpaQuery = jpaQueryFactory.select(qCueSheet).from(qCueSheet)
                .leftJoin(qCueSheet.program, qProgram).fetchJoin()
                .leftJoin(qCueSheet.baseProgram, qBaseProgram).fetchJoin().distinct();

        jpaQuery.where(qCueSheet.delYn.eq("N"));

        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false){
            DateChangeHelper dateChangeHelper = new DateChangeHelper();
            /*String stringSdate = dateChangeHelper.dateToStringNoTime(sdate);//Date To String( yyyy-MM-dd )
            String stringEdate = dateChangeHelper.dateToStringNoTime(edate);//Date To String( yyyy-MM-dd )
*/
            String stime = dateChangeHelper.dateToStringNormal(sdate);
            String etime = dateChangeHelper.dateToStringNormal(edate);

            /*String newStime = new SimpleDateFormat("HH:mm:ss").format(sdate); // 09:0:00
            String newEtime = new SimpleDateFormat("HH:mm:ss").format(edate); // 09:0:00*/


            //방송날짜 조회
           /* jpaQuery.where(qCueSheet.brdcDt.eq(stringSdate).and(qCueSheet.brdcStartTime.goe(newStime))
                    .and(qCueSheet.brdcDt.eq(stringEdate).and(qCueSheet.brdcStartTime.loe(newEtime))));*/

            //방송일자 + ' ' + 방송시작시간 between 검색조건 1 "yyyy-MM-dd HH:mm:ss" and 검색조건2 "yyyy-MM-dd HH:mm:ss"
            jpaQuery.where(qCueSheet.brdcDt.append(" ").append(qCueSheet.brdcStartTime).between(stime, etime));

            //방송시간 조회
            /*jpaQuery.where(qCueSheet.brdcStartTime.loe(newStime));
            jpaQuery.where(qCueSheet.brdcStartTime.goe(newEtime));*/
            /*jpaQuery.where(qCueSheet.brdcStartTime.goe(newStime));
            jpaQuery.where(qCueSheet.brdcStartTime.loe(newEtime));*/
        }

        //if (cueDivCd != null && cueDivCd.trim().isEmpty() == false){
        //    jpaQuery.where(qCueSheet.cueDivCd.eq(cueDivCd));
        //}

        jpaQuery.orderBy(qCueSheet.brdcDt.asc(), qCueSheet.brdcStartTime.asc());

        return jpaQuery.fetch();
    }

}
