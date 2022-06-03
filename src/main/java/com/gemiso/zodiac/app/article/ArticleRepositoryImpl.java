package com.gemiso.zodiac.app.article;

import com.gemiso.zodiac.app.anchorCap.QAnchorCap;
import com.gemiso.zodiac.app.articleCap.QArticleCap;
import com.gemiso.zodiac.app.articleMedia.QArticleMedia;
import com.gemiso.zodiac.app.cueSheet.QCueSheet;
import com.gemiso.zodiac.app.issue.QIssue;
import com.gemiso.zodiac.app.user.QUser;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustorm {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ArticleRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }


    @Override
    public Page<Article> findByArticleList(Date sdate,
                                           Date edate,
                                           Date rcvDt,
                                           String rptrId,
                                           String inputrId,
                                           String brdcPgmId,
                                           String artclDivCd,
                                           String artclTypCd,
                                           String searchDivCd,
                                           String searchWord,
                                           List<String> apprvDivCdList,
                                           Integer deptCd,
                                           String artclCateCd,
                                           String artclTypDtlCd,
                                           String delYn,
                                           Long artclId,
                                           String copyYn,
                                           Pageable pageable) {

        QArticle qArticle = QArticle.article;
        QUser qUser = QUser.user;
        QArticleCap qArticleCap = QArticleCap.articleCap;
        QAnchorCap qAnchorCap = QAnchorCap.anchorCap;
        QCueSheet qCueSheet = QCueSheet.cueSheet;
        QIssue qIssue = QIssue.issue;
        QArticleMedia qArticleMedia = QArticleMedia.articleMedia;

        JPAQuery jpaQuery = jpaQueryFactory.select(qArticle).from(qArticle)
                .leftJoin(qArticle.cueSheet, qCueSheet).fetchJoin()
                .leftJoin(qArticle.issue, qIssue).fetchJoin()
                .leftJoin(qArticle.articleMedia, qArticleMedia).fetchJoin()
                .leftJoin(qArticle.articleCap, qArticleCap).fetchJoin()
                .leftJoin(qArticle.anchorCap, qAnchorCap).fetchJoin().distinct();
        //삭제가 되지 않은 기사 조회
        //jpaQuery.where(qArticle.delYn.eq("N"));

        //등록날짜 기준으로 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            jpaQuery.where(qArticle.inputDtm.between(sdate, edate)
                    /*.or(qArticle.embgDtm.between(sdate, edate)
                            .or(qArticle.brdcSchdDtm.between(sdate, edate)))*/);
        }
        //수신 일자 기준으로 조회
        if (ObjectUtils.isEmpty(rcvDt) == false) {

            //rcvDt(수신일자)검색을 위해 +1 days
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rcvDt);
            calendar.add(Calendar.DATE, 1);
            Date rcvDtTomerrow = calendar.getTime();

            jpaQuery.where(qArticle.inputDtm.between(rcvDt, rcvDtTomerrow));
        }
        //기자 아이디로 조회
        if (rptrId != null && rptrId.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.rptrId.eq(rptrId));
        }
        //등록자 아이디로 조회
        if (inputrId != null && inputrId.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.inputrId.eq(inputrId));
        }
        //방송 프로그램 아이디로 조회
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.brdcPgmId.eq(brdcPgmId));
        }
        //기사 구분 코드로 조회
        if (artclDivCd != null && artclDivCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclDivCd.eq(artclDivCd));
        }
        //기사 타입 코드로 조회
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTypCd.eq(artclTypCd));
        }
        //검색조건 = 삭제 여부
        if (delYn != null && delYn.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.delYn.eq(delYn));
        } else {
            jpaQuery.where(qArticle.delYn.eq("N")); //삭제여부값 안들어 올시 디폴트 'N'
        }
        //검색어로 조회
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            //검색구분코드 01 일때 기사 제목으로 검색
            if ("01".equals(searchDivCd)) {
                jpaQuery.where(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
            }
            //검색구분코드 02 일때 기자이름으로 검색
            else if ("02".equals(searchDivCd)) {
                jpaQuery.where(qArticle.rptrId.eq(String.valueOf(qUser.userNm.contains(searchWord))));
            }
            //검색구분코드 안들어왔을 경우
            else if (searchDivCd == null || searchDivCd.trim().isEmpty()) {
                jpaQuery.where(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
            }

        }

        //픽스구분코드[여러개의 or조건으로 가능]
        if (CollectionUtils.isEmpty(apprvDivCdList) == false) {
            jpaQuery.where(qArticle.apprvDivCd.in(apprvDivCdList));
        }

        //부서코드
        if (deptCd != null && deptCd != 0) {
            jpaQuery.where(qArticle.deptCd.eq(deptCd));
        }
        //기사카테고리코드
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclCateCd.eq(artclCateCd));
        }
        //기사유형상세코드
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTypDtlCd.eq(artclTypDtlCd));
        }
        //기사 아이디
        if (ObjectUtils.isEmpty(artclId) == false) {
            jpaQuery.where(qArticle.artclId.eq(artclId));
        }
        //원본 기사 및 복사된 기사 검색조건
        if (copyYn != null && copyYn.trim().isEmpty() == false) {

            if ("N".equals(copyYn)) {
                jpaQuery.where(qArticle.orgArtclId.eq(qArticle.artclId));
            } else {
                jpaQuery.where(qArticle.orgArtclId.ne(qArticle.artclId));
            }
        }

        jpaQuery.orderBy(qArticle.orgArtclId.desc(), qArticle.artclOrd.asc());

        Long totalCount = jpaQuery.fetchCount();
        jpaQuery.offset(pageable.getOffset());
        jpaQuery.limit(pageable.getPageSize());

        List<Article> articleList = jpaQuery.fetch();

        return new PageImpl<>(articleList, pageable, totalCount);

    }

    @Override
    public Page<Article> findByArticleIssue(Date sdate, Date edate, String issuKwd, String artclDivCd, String artclTypCd,
                                            String artclTypDtlCd, String artclCateCd, Integer deptCd, String inputrId,
                                            String brdcPgmId, Long orgArtclId, String delYn, String searchDivCd,
                                            String searchWord, List<String> apprvDivCdList, Pageable pageable) {


        QArticle qArticle = QArticle.article;
        QUser qUser = QUser.user;
        QArticleCap qArticleCap = QArticleCap.articleCap;
        QAnchorCap qAnchorCap = QAnchorCap.anchorCap;
        QCueSheet qCueSheet = QCueSheet.cueSheet;
        QIssue qIssue = QIssue.issue;
        QArticleMedia qArticleMedia = QArticleMedia.articleMedia;

        JPAQuery jpaQuery = jpaQueryFactory.select(qArticle).from(qArticle)
                .leftJoin(qArticle.cueSheet, qCueSheet).fetchJoin()
                .leftJoin(qArticle.issue, qIssue).fetchJoin()
                .leftJoin(qArticle.articleMedia, qArticleMedia).fetchJoin()
                .leftJoin(qArticle.articleCap, qArticleCap).fetchJoin()
                .leftJoin(qArticle.anchorCap, qAnchorCap).fetchJoin().distinct();

        //이슈 검색일 조건
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            jpaQuery.where(qArticle.issue.issuDtm.between(sdate, edate));
        }
        //이슈 키워드 검색
        if (issuKwd != null && issuKwd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.issue.issuKwd.eq(issuKwd));
        }
        //기사 구분코드[이슈기사 검색이기때문에 무조건 article_issue 들어와야함함]
        if (artclDivCd != null && artclDivCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclDivCd.eq(artclDivCd));
        }
        //검색조건 = 기사 유형 코드
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTypCd.eq(artclTypCd));
        }
        //검색조건 = 기상 유형 상세 코드
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTypDtlCd.eq(artclTypDtlCd));
        }
        //검색조건 = 기사 카테고리 코드
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclCateCd.eq(artclCateCd));
        }
        //검색조건 = 부서 코드
        if (deptCd != null && deptCd != 0) {
            jpaQuery.where(qArticle.deptCd.eq(deptCd));
        }
        //검색조건 = 부서 코드
        if (inputrId != null && inputrId.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.inputrId.eq(inputrId));
        }
        //검색조건 = 방송 프로그램 아이디
        if (ObjectUtils.isEmpty(brdcPgmId) == false) {
            jpaQuery.where(qArticle.brdcPgmId.eq(brdcPgmId));
        }
        //검색조건 = 원본 기사 아이디
        if (ObjectUtils.isEmpty(orgArtclId) == false) {
            jpaQuery.where(qArticle.orgArtclId.eq(orgArtclId));
        }
        //검색조건 = 삭제 여부
        if (delYn != null && delYn.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.delYn.eq(delYn));
        } else {
            jpaQuery.where(qArticle.delYn.eq("N")); //삭제여부값 안들어 올시 디폴트 'N'
        }
        //검색어로 조회
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            //검색구분코드 01 일때 기사 제목으로 검색
            if ("01".equals(searchDivCd)) {
                jpaQuery.where(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
            }
            //검색구분코드 02 일때 기자이름으로 검색
            else if ("02".equals(searchDivCd)) {
                jpaQuery.where(qArticle.rptrId.eq(String.valueOf(qUser.userNm.contains(searchWord))));
            }
            //검색구분코드 안들어왔을 경우
            else if (searchDivCd == null || searchDivCd.trim().isEmpty()) {
                jpaQuery.where(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
            }

        }

        //픽스구분코드[여러개의 or조건으로 가능]
        if (CollectionUtils.isEmpty(apprvDivCdList) == false) {
            jpaQuery.where(qArticle.apprvDivCd.in(apprvDivCdList));
        }

        jpaQuery.orderBy(qArticle.orgArtclId.desc(), qArticle.artclOrd.asc());

        Long totalCount = jpaQuery.fetchCount();
        jpaQuery.offset(pageable.getOffset());
        jpaQuery.limit(pageable.getPageSize());

        List<Article> articleList = jpaQuery.fetch();

        return new PageImpl<>(articleList, pageable, totalCount);
    }

    @Override
    public Page<Article> findByArticleCue(Date sdate, Date edate, String searchWord, Long cueId, String brdcPgmId, String artclTypCd,
                                          String artclTypDtlCd, String copyYn, Integer deptCd, Pageable pageable) {


        QArticle qArticle = QArticle.article;
        QUser qUser = QUser.user;
        QArticleCap qArticleCap = QArticleCap.articleCap;
        QAnchorCap qAnchorCap = QAnchorCap.anchorCap;
        QCueSheet qCueSheet = QCueSheet.cueSheet;
        QIssue qIssue = QIssue.issue;
        QArticleMedia qArticleMedia = QArticleMedia.articleMedia;

        JPAQuery jpaQuery = jpaQueryFactory.select(qArticle).from(qArticle)
                .leftJoin(qArticle.cueSheet, qCueSheet).fetchJoin()
                .leftJoin(qArticle.issue, qIssue).fetchJoin()
                .leftJoin(qArticle.articleMedia, qArticleMedia).fetchJoin()
                .leftJoin(qArticle.articleCap, qArticleCap).fetchJoin()
                .leftJoin(qArticle.anchorCap, qAnchorCap).fetchJoin().distinct();

        jpaQuery.where(qArticle.delYn.eq("N"));

        //등록날짜 기준으로 조회
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            jpaQuery.where(qArticle.inputDtm.between(sdate, edate));
        }

        //기사제목 기준으로 조회
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
        }

        //검색조건 = 방송 프로그램 아이디
        if (ObjectUtils.isEmpty(brdcPgmId) == false) {
            jpaQuery.where(qArticle.brdcPgmId.eq(brdcPgmId));
        }

        //기사 유형 코드
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false){
            jpaQuery.where(qArticle.artclTypCd.eq(artclTypCd));
        }

        //검색조건 = 기상 유형 상세 코드
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTypDtlCd.eq(artclTypDtlCd));
        }

        //검색조건 = 부서 코드
        if (deptCd != null && deptCd != 0) {
            jpaQuery.where(qArticle.deptCd.eq(deptCd));
        }

        //원본 기사 및 복사된 기사 검색조건
        if (copyYn != null && copyYn.trim().isEmpty() == false) {

            if ("N".equals(copyYn)) {
                jpaQuery.where(qArticle.orgArtclId.eq(qArticle.artclId));
            } else {
                jpaQuery.where(qArticle.orgArtclId.ne(qArticle.artclId));
            }
        }

        jpaQuery.orderBy(qArticle.inputDtm.desc(), qArticle.orgArtclId.asc());

        Long totalCount = jpaQuery.fetchCount();
        jpaQuery.offset(pageable.getOffset());
        jpaQuery.limit(pageable.getPageSize());

        List<Article> articleList = jpaQuery.fetch();

        return new PageImpl<>(articleList, pageable, totalCount);
    }
}
