package com.gemiso.zodiac.core.helper;

import com.gemiso.zodiac.app.cueSheetItemCap.CueSheetItemCap;

import java.util.Comparator;

public class CueItemListSortHelper implements Comparator<CueSheetItemCap>{

    @Override
    public int compare(CueSheetItemCap cap1, CueSheetItemCap cap2) {
        if (cap1.getCapOrd() > cap2.getCapOrd()) {
            return 1;
        } else if (cap1.getCapOrd() < cap2.getCapOrd()) {
            return -1;
        }
        return 0;
    }

    @Override
    public Comparator<CueSheetItemCap> reversed() {
        return Comparator.super.reversed();
    }

}

