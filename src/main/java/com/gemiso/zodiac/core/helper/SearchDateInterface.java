package com.gemiso.zodiac.core.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchDateInterface {
    private String startDate;
    private String endDate;
    public SearchDateInterface(String startDate, String endDate) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmss");
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmss");

        Date sdate = simpleDateFormat.parse(startDate);
        Date edate = simpleDateFormat.parse(endDate);

        Calendar cal = Calendar.getInstance();

        cal.setTime(edate);
        cal.add(Calendar.HOUR, 24);
        cal.add(Calendar.MINUTE, 00);
        cal.add(Calendar.SECOND, 00);

        String formatSdate = hourFormat.format(sdate);
        String formatEdate = hourFormat.format(cal.getTime());

        // 2021-09-28 12:12:00 => 2021-09-28 00:00:00
        this.startDate = formatSdate;
        // 2021-09-28 12:12:00 => 2021-09-28 23:59:59
        this.endDate = formatEdate;

    }

    public String getStartDate() {
        return this.startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }
}
