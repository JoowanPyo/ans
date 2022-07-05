package com.gemiso.zodiac.app.cueSheet;

import java.util.Date;
import java.util.List;

public interface CueSheetRepositoryCustorm {

    List<CueSheet> findByCueSheetList(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, Long deptCd, String searchWord);

    List<CueSheet> findNodCue(Date sdate, Date edate, String cueDivCd);

}
