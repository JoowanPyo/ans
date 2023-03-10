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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateFormat = hourFormat.format(startDate);
        String endDateFormat = hourFormat.format(endDate);


        Calendar cal = Calendar.getInstance();

        cal.setTime(simpleDateFormat.parse(endDateFormat));
        cal.add(Calendar.HOUR, 23);
        cal.add(Calendar.MINUTE, 59);
        cal.add(Calendar.SECOND, 59);

        // 2021-09-28 12:12:00 => 2021-09-28 00:00:00
        this.startDate = simpleDateFormat.parse(startDateFormat);
        // 2021-09-28 12:12:00 => 2021-09-28 23:59:59
        this.endDate = cal.getTime();

    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }
}
