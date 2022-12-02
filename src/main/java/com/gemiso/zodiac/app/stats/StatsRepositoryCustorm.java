package com.gemiso.zodiac.app.stats;

import java.util.List;

public interface StatsRepositoryCustorm {

    public List<Stats> findStats(String sdate, String edate);
}
