package com.gemiso.zodiac.core.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchDate {
    private Date startDate;
    private Date endDate;
    public SearchDate(Date startDate, Date endDate) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmss");
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmss");
        String startDateFormat = hourFormat.format(startDate);
        String endDateFormat = hourFormat.format(endDate);


        Calendar cal = Calendar.getInstance();

        cal.setTime(simpleDateFormat.parse(startDateFormat));
        cal.add(Calendar.HOUR, 23);
        cal.add(Calendar.MINUTE, 59);
        cal.add(Calendar.SECOND, 55);

        // 2021-09-28 12:12:00 => 2021-09-28 00:00:00
        this.startDate = cal.getTime();
        // 2021-09-28 12:12:00 => 2021-09-28 23:59:59
        this.endDate = simpleDateFormat.parse(endDateFormat);
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }
}
