package com.gemiso.zodiac.app.dailyProgram;

import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface DailyProgramRepositoryCustorn {

    List<DailyProgram> findByDailyProgramList(Date sdate, Date edate, String brdcPgmId, String brdcPgmNm, String brdcDivCd,
                                              Long stdioId, Long subrmId, String searchWord);
}
