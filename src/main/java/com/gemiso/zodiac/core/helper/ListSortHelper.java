package com.gemiso.zodiac.core.helper;

import com.gemiso.zodiac.app.articleCap.ArticleCap;

import java.util.Comparator;

public class ListSortHelper implements Comparator<ArticleCap> {


    @Override
    public int compare(ArticleCap cap1, ArticleCap cap2) {
        if (cap1.getLnOrd() > cap2.getLnOrd()) {
            return 1;
        } else if (cap1.getLnOrd() < cap2.getLnOrd()) {
            return -1;
        }
        return 0;
    }

    @Override
    public Comparator<ArticleCap> reversed() {
        return Comparator.super.reversed();
    }


}
