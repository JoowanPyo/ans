package com.gemiso.zodiac.app.cueSheetItem;

import java.util.List;

public interface CueSheetItemRepositoryCustorm {

    List<CueSheetItem> findByCueSheetItemList(Long artclId, Long cueId, String delYn, String spareYn);

    List<CueSheetItem> findByCueSheetItemMediaList(Long cueId);

}
