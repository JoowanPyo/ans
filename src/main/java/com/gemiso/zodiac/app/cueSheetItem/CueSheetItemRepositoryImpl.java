package com.gemiso.zodiac.app.cueSheetItem;

import com.gemiso.zodiac.app.anchorCap.QAnchorCap;
import com.gemiso.zodiac.app.article.QArticle;
import com.gemiso.zodiac.app.articleCap.QArticleCap;
import com.gemiso.zodiac.app.articleMedia.QArticleMedia;
import com.gemiso.zodiac.app.cueSheet.QCueSheet;
import com.gemiso.zodiac.app.cueSheetItemCap.QCueSheetItemCap;
import com.gemiso.zodiac.app.cueSheetItemSymbol.QCueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetMedia.QCueSheetMedia;
import com.gemiso.zodiac.app.cueSheetTemplate.QCueSheetTemplate;
import com.gemiso.zodiac.app.file.QAttachFile;
import com.gemiso.zodiac.app.symbol.QSymbol;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Repository
public class CueSheetItemRepositoryImpl implements CueSheetItemRepositoryCustorm{

    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public CueSheetItemRepositoryImpl(JPAQueryFactory jpaQueryFactory){
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<CueSheetItem> findByCueSheetItemList(Long artclId, Long cueId, String delYn, String spareYn){

        QCueSheetItem qCueSheetItem = QCueSheetItem.cueSheetItem;
        QCueSheet qCueSheet = QCueSheet.cueSheet;
        QArticle qArticle = QArticle.article;
        QCueSheetTemplate qCueSheetTemplate = QCueSheetTemplate.cueSheetTemplate;
        QCueSheetMedia qCueSheetMedia = QCueSheetMedia.cueSheetMedia;
        QCueSheetItemCap qCueSheetItemCap = QCueSheetItemCap.cueSheetItemCap;
        QCueSheetItemSymbol qCueSheetItemSymbol = QCueSheetItemSymbol.cueSheetItemSymbol;
        QAttachFile qAttachFile = QAttachFile.attachFile;
        QSymbol qSymbol = QSymbol.symbol;
        QArticleMedia qArticleMedia = QArticleMedia.articleMedia;
        QArticleCap qArticleCap = QArticleCap.articleCap;
        QAnchorCap qAnchorCap = QAnchorCap.anchorCap;



        JPAQuery jpaQuery = jpaQueryFactory.select(qCueSheetItem).from(qCueSheetItem)
                .leftJoin(qCueSheetItem.cueSheet, qCueSheet).fetchJoin()
                .leftJoin(qCueSheetItem.article, qArticle).fetchJoin()
                .leftJoin(qArticle.articleMedia, qArticleMedia).fetchJoin()
                .leftJoin(qArticle.articleCap, qArticleCap).fetchJoin()
                .leftJoin(qArticleCap.symbol, qSymbol).fetchJoin()
                .leftJoin(qArticle.anchorCap, qAnchorCap).fetchJoin()
                .leftJoin(qAnchorCap.symbol, qSymbol).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetTemplate, qCueSheetTemplate).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetMedia, qCueSheetMedia).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetItemCap, qCueSheetItemCap).fetchJoin()
                .leftJoin(qCueSheetItemCap.symbol, qSymbol).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetItemSymbol, qCueSheetItemSymbol).fetchJoin()
                .leftJoin(qCueSheetItemSymbol.symbol, qSymbol).fetchJoin()
                .leftJoin(qSymbol.attachFile, qAttachFile).fetchJoin().distinct();


        if (delYn != null && delYn.trim().isEmpty() == false) {
            jpaQuery.where(qCueSheetItem.delYn.eq(delYn));
        } else {
            jpaQuery.where(qCueSheetItem.delYn.eq("N"));
        }
        //쿼리 where 조건 추가.
        if (ObjectUtils.isEmpty(artclId) == false) {
            jpaQuery.where(qCueSheetItem.article.artclId.eq(artclId));
        }

        //쿼리 where 조건 추가.
        if (ObjectUtils.isEmpty(cueId) == false) {
            jpaQuery.where(qCueSheetItem.cueSheet.cueId.eq(cueId));
        }

        //스페어 여부
        if (spareYn != null && spareYn.trim().isEmpty() == false) {
            jpaQuery.where(qCueSheetItem.spareYn.eq(spareYn));
        }

        jpaQuery.orderBy(qCueSheetItem.cueItemOrd.asc());

        return jpaQuery.fetch();

    }

    @Override
    public List<CueSheetItem> findByCueSheetItemMediaList(Long cueId) {


        QCueSheetItem qCueSheetItem = QCueSheetItem.cueSheetItem;
        QCueSheet qCueSheet = QCueSheet.cueSheet;
        QArticle qArticle = QArticle.article;
        QCueSheetTemplate qCueSheetTemplate = QCueSheetTemplate.cueSheetTemplate;
        QCueSheetMedia qCueSheetMedia = QCueSheetMedia.cueSheetMedia;
        QCueSheetItemCap qCueSheetItemCap = QCueSheetItemCap.cueSheetItemCap;
        QCueSheetItemSymbol qCueSheetItemSymbol = QCueSheetItemSymbol.cueSheetItemSymbol;

        JPAQuery jpaQuery = jpaQueryFactory.select(qCueSheetItem).from(qCueSheetItem)
                .leftJoin(qCueSheetItem.cueSheet, qCueSheet).fetchJoin()
                .leftJoin(qCueSheetItem.article, qArticle).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetMedia, qCueSheetMedia).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetTemplate, qCueSheetTemplate).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetItemCap, qCueSheetItemCap).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetItemSymbol, qCueSheetItemSymbol).fetchJoin().distinct();

        //쿼리 where 조건 추가.
        if (ObjectUtils.isEmpty(cueId) == false) {
            jpaQuery.where(qCueSheetItem.cueSheet.cueId.eq(cueId));
        }

        jpaQuery.where(qCueSheetItem.delYn.eq("N"));

        jpaQuery.orderBy(qCueSheetItem.cueItemOrd.asc());

        return jpaQuery.fetch();
    }
}
