package com.gemiso.zodiac.app.articleActionLog;

import com.gemiso.zodiac.app.article.QArticle;
import com.gemiso.zodiac.app.articleActionLog.dto.ArticleActionLogQueryDslDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ArticleActionLogRepositoryImpl implements ArticleActionLogRepositoryCustrom {

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public ArticleActionLogRepositoryImpl(JPAQueryFactory jpaQueryFactory) {

        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<ArticleActionLog> findArticleActionLogList(Long artclId) {


        QArticleActionLog qArticleActionLog = QArticleActionLog.articleActionLog;
        QArticle qArticle = QArticle.article;

        JPAQuery jpaQuery = jpaQueryFactory.select(qArticleActionLog).from(qArticleActionLog)
                .leftJoin(qArticleActionLog.article, qArticle).fetchJoin().distinct();

        if (ObjectUtils.isEmpty(artclId) == false){
            jpaQuery.where(qArticleActionLog.article.artclId.eq(artclId)); //기사아이디 조회조건 추가
        }

        jpaQuery.orderBy(qArticleActionLog.inputDtm.desc());

        List<ArticleActionLog> articleActionLogs =  jpaQuery.fetch();

        /*List<ArticleActionLog> articleActionLogs = new ArrayList<>();

        for (ArticleActionLogQueryDslDTO Rundown : articleActionLogQueryDslDTOS){

            ArticleActionLog articleActionLog = new ArticleActionLog();

            articleActionLog.setId(Rundown.getId());
            articleActionLog.setMessage(Rundown.getMessage());
            articleActionLog.setAction(Rundown.getAction());
            articleActionLog.setInputDtm(Rundown.getInputDtm());
            articleActionLog.setInputrId(Rundown.getInputrId());
            articleActionLog.setInputrNm(Rundown.getInputrNm());
            //articleActionLog.setArtclInfo();
            //articleActionLog.setAnchorCapInfo();
            //articleActionLog.setArtclCapInfo();
            articleActionLog.setArticle(Rundown.getArticle());
        }*/

        return articleActionLogs;
    }
}
