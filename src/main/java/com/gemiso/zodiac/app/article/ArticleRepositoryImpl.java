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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustorm{

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
                                           Long deptCd,
                                           String artclCateCd,
                                           String artclTypDtlCd,
                                           String delYn,
                                           Long artclId,
                                           String copyYn,
                                           Long orgArtclId,
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
        //????????? ?????? ?????? ?????? ??????
        //jpaQuery.where(qArticle.delYn.eq("N"));

        //???????????? ???????????? ??????
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            jpaQuery.where(qArticle.inputDtm.between(sdate, edate)
                    /*.or(qArticle.embgDtm.between(sdate, edate)
                            .or(qArticle.brdcSchdDtm.between(sdate, edate)))*/);
        }
        //?????? ?????? ???????????? ??????
        if (ObjectUtils.isEmpty(rcvDt) == false) {

            //rcvDt(????????????)????????? ?????? +1 days
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rcvDt);
            calendar.add(Calendar.DATE, 1);
            Date rcvDtTomerrow = calendar.getTime();

            jpaQuery.where(qArticle.inputDtm.between(rcvDt, rcvDtTomerrow));
        }
        //?????? ???????????? ??????
        if (rptrId != null && rptrId.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.rptrId.eq(rptrId));
        }
        //????????? ???????????? ??????
        if (inputrId != null && inputrId.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.inputrId.eq(inputrId));
        }
        //?????? ???????????? ???????????? ??????
        if (brdcPgmId != null && brdcPgmId.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.brdcPgmId.eq(brdcPgmId));
        }
        //?????? ?????? ????????? ??????
        if (artclDivCd != null && artclDivCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclDivCd.eq(artclDivCd));
        }
        //?????? ?????? ????????? ??????
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTypCd.eq(artclTypCd));
        }
        //???????????? = ?????? ??????
        if (delYn != null && delYn.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.delYn.eq(delYn));
        } else {
            jpaQuery.where(qArticle.delYn.eq("N")); //??????????????? ????????? ?????? ????????? 'N'
        }
        //???????????? ??????
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            //?????????????????? 01 ?????? ?????? ???????????? ??????
            if ("01".equals(searchDivCd)) {
                jpaQuery.where(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
            }
            //?????????????????? 02 ?????? ?????????????????? ??????
            else if ("02".equals(searchDivCd)) {
                jpaQuery.where(qArticle.rptrId.eq(String.valueOf(qUser.userNm.contains(searchWord))));
            }
            //?????????????????? ??????????????? ??????
            else if (searchDivCd == null || searchDivCd.trim().isEmpty()) {
                jpaQuery.where(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
            }

        }

        //??????????????????[???????????? or???????????? ??????]
        if (CollectionUtils.isEmpty(apprvDivCdList) == false) {
            jpaQuery.where(qArticle.apprvDivCd.in(apprvDivCdList));
        }

        //????????????
        if (deptCd != null && deptCd != 0) {
            jpaQuery.where(qArticle.deptCd.eq(deptCd));
        }
        //????????????????????????
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclCateCd.eq(artclCateCd));
        }
        //????????????????????????
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTypDtlCd.eq(artclTypDtlCd));
        }
        //?????? ?????????
        if (ObjectUtils.isEmpty(artclId) == false) {
            jpaQuery.where(qArticle.artclId.eq(artclId));
        }
        //???????????? ?????????
        if (ObjectUtils.isEmpty(orgArtclId) == false){
            jpaQuery.where(qArticle.orgArtclId.eq(orgArtclId));
        }
        //?????? ?????? ??? ????????? ?????? ????????????
        if (copyYn != null && copyYn.trim().isEmpty() == false) {

            if ("N".equals(copyYn)) {
                jpaQuery.where(qArticle.artclOrd.eq(0));
            } else {
                jpaQuery.where(qArticle.artclOrd.ne(0));
            }
        }

        jpaQuery.offset(pageable.getOffset());
        jpaQuery.limit(pageable.getPageSize());

        jpaQuery.orderBy(qArticle.orgArtclId.desc(), qArticle.artclOrd.asc());

        Long totalCount = jpaQuery.fetchCount();


        List<Article> articleList = jpaQuery.fetch();

        return new PageImpl<>(articleList, pageable, totalCount);

    }

    @Override
    public Page<Article> findByArticleIssue(Date sdate, Date edate, String issuKwd, String artclDivCd, String artclTypCd,
                                            String artclTypDtlCd, String artclCateCd, Long deptCd, String inputrId,
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

        //?????? ????????? ??????
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            jpaQuery.where(qArticle.issue.issuDtm.between(sdate, edate));
        }
        //?????? ????????? ??????
        if (issuKwd != null && issuKwd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.issue.issuKwd.eq(issuKwd));
        }
        //?????? ????????????[???????????? ????????????????????? ????????? article_issue ??????????????????]
        if (artclDivCd != null && artclDivCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclDivCd.eq(artclDivCd));
        }
        //???????????? = ?????? ?????? ??????
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTypCd.eq(artclTypCd));
        }
        //???????????? = ?????? ?????? ?????? ??????
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTypDtlCd.eq(artclTypDtlCd));
        }
        //???????????? = ?????? ???????????? ??????
        if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclCateCd.eq(artclCateCd));
        }
        //???????????? = ?????? ??????
        if (deptCd != null && deptCd != 0) {
            jpaQuery.where(qArticle.deptCd.eq(deptCd));
        }
        //???????????? = ?????? ??????
        if (inputrId != null && inputrId.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.inputrId.eq(inputrId));
        }
        //???????????? = ?????? ???????????? ?????????
        if (ObjectUtils.isEmpty(brdcPgmId) == false) {
            jpaQuery.where(qArticle.brdcPgmId.eq(brdcPgmId));
        }
        //???????????? = ?????? ?????? ?????????
        if (ObjectUtils.isEmpty(orgArtclId) == false) {
            jpaQuery.where(qArticle.orgArtclId.eq(orgArtclId));
        }
        //???????????? = ?????? ??????
        if (delYn != null && delYn.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.delYn.eq(delYn));
        } else {
            jpaQuery.where(qArticle.delYn.eq("N")); //??????????????? ????????? ?????? ????????? 'N'
        }
        //???????????? ??????
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            //?????????????????? 01 ?????? ?????? ???????????? ??????
            if ("01".equals(searchDivCd)) {
                jpaQuery.where(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
            }
            //?????????????????? 02 ?????? ?????????????????? ??????
            else if ("02".equals(searchDivCd)) {
                jpaQuery.where(qArticle.rptrId.eq(String.valueOf(qUser.userNm.contains(searchWord))));
            }
            //?????????????????? ??????????????? ??????
            else if (searchDivCd == null || searchDivCd.trim().isEmpty()) {
                jpaQuery.where(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
            }

        }

        //??????????????????[???????????? or???????????? ??????]
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
                                          String artclTypDtlCd, String copyYn, Long deptCd, Long orgArtclId, Pageable pageable) {


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

        //???????????? ???????????? ??????
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            jpaQuery.where(qArticle.inputDtm.between(sdate, edate));
        }

        //???????????? ???????????? ??????
        if (searchWord != null && searchWord.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTitl.contains(searchWord).or(qArticle.artclTitlEn.contains(searchWord)));
        }

        //???????????? = ?????? ???????????? ?????????
        if (ObjectUtils.isEmpty(brdcPgmId) == false) {
            jpaQuery.where(qArticle.brdcPgmId.eq(brdcPgmId));
        }

        //?????? ?????? ??????
        if (artclTypCd != null && artclTypCd.trim().isEmpty() == false){
            jpaQuery.where(qArticle.artclTypCd.eq(artclTypCd));
        }

        //???????????? = ?????? ?????? ?????? ??????
        if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {
            jpaQuery.where(qArticle.artclTypDtlCd.eq(artclTypDtlCd));
        }

        //???????????? = ?????? ??????
        if (deptCd != null && deptCd != 0) {
            jpaQuery.where(qArticle.deptCd.eq(deptCd));
        }

        //???????????? ?????????
        if (ObjectUtils.isEmpty(orgArtclId) == false){
            jpaQuery.where(qArticle.orgArtclId.eq(orgArtclId));
        }

        //?????? ?????? ??? ????????? ?????? ????????????
        if (copyYn != null && copyYn.trim().isEmpty() == false) {

            if ("N".equals(copyYn)) {
                jpaQuery.where(qArticle.artclOrd.eq(0));
            } else {
                jpaQuery.where(qArticle.artclOrd.ne(0));
            }
        }

        jpaQuery.orderBy(qArticle.inputDtm.desc(), qArticle.orgArtclId.asc());

        Long totalCount = jpaQuery.fetchCount();
        jpaQuery.offset(pageable.getOffset());
        jpaQuery.limit(pageable.getPageSize());

        List<Article> articleList = jpaQuery.fetch();

        return new PageImpl<>(articleList, pageable, totalCount);
    }

    @Override
    public Page<Article> findByArticleListElastic(Date sdate, Date edate,Pageable pageable) {

        QArticle qArticle = QArticle.article;
        QUser qUser = QUser.user;
        //QArticleCap qArticleCap = QArticleCap.articleCap;
        //QAnchorCap qAnchorCap = QAnchorCap.anchorCap;
        QCueSheet qCueSheet = QCueSheet.cueSheet;
        //QIssue qIssue = QIssue.issue;
        //QArticleMedia qArticleMedia = QArticleMedia.articleMedia;

        JPAQuery jpaQuery = jpaQueryFactory.select(qArticle).from(qArticle)
                .leftJoin(qArticle.cueSheet, qCueSheet).fetchJoin().distinct();
                /*.leftJoin(qArticle.issue, qIssue).fetchJoin()
                .leftJoin(qArticle.articleMedia, qArticleMedia).fetchJoin()
                .leftJoin(qArticle.articleCap, qArticleCap).fetchJoin()
                .leftJoin(qArticle.anchorCap, qAnchorCap).fetchJoin().distinct();*/

        //???????????? ???????????? ??????
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            jpaQuery.where(qArticle.inputDtm.between(sdate, edate)
                    /*.or(qArticle.embgDtm.between(sdate, edate)
                            .or(qArticle.brdcSchdDtm.between(sdate, edate)))*/);
        }

        jpaQuery.orderBy(qArticle.artclId.asc());

        jpaQuery.offset(pageable.getOffset());
        jpaQuery.limit(pageable.getPageSize());

        List<Article> articleList = jpaQuery.fetch();

        return new PageImpl<>(articleList, pageable, 0);
    }
}
