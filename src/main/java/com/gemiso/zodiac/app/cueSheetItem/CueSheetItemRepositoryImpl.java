package com.gemiso.zodiac.app.cueSheetItem;

import com.gemiso.zodiac.app.article.QArticle;
import com.gemiso.zodiac.app.cueSheet.QCueSheet;
import com.gemiso.zodiac.app.cueSheetItemCap.QCueSheetItemCap;
import com.gemiso.zodiac.app.cueSheetItemSymbol.QCueSheetItemSymbol;
import com.gemiso.zodiac.app.cueSheetMedia.QCueSheetMedia;
import com.gemiso.zodiac.app.cueSheetTemplate.QCueSheetTemplate;
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

        JPAQuery jpaQuery = jpaQueryFactory.select(qCueSheetItem).from(qCueSheetItem)
                .leftJoin(qCueSheetItem.cueSheet, qCueSheet).fetchJoin()
                .leftJoin(qCueSheetItem.article, qArticle).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetTemplate, qCueSheetTemplate).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetMedia, qCueSheetMedia).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetItemCap, qCueSheetItemCap).fetchJoin()
                .leftJoin(qCueSheetItem.cueSheetItemSymbol, qCueSheetItemSymbol).fetchJoin().distinct();


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
