package com.gemiso.zodiac.core.service;

import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheetItem.CueSheetItem;

import java.util.ArrayList;
import java.util.List;

public class AnsCueSheet {

    private List<CueSheet> cueSheets  = new ArrayList<CueSheet>();

    private List<CueSheetItem> cueSheetItems = new ArrayList<CueSheetItem>();

    public AnsCueSheet() {

    }

    public void SetCuesheet(List<CueSheet> in )
    {
        this.cueSheets = in;
    }

    public void SetCuesheetItem(List<CueSheetItem> in )
    {
        this.cueSheetItems = in;
    }

    public List<CueSheet> GetCuesheet(){
        return cueSheets;
    }
}
