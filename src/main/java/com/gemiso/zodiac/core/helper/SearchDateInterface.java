package com.gemiso.zodiac.core.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SearchDateInterface {
    private Date startDate;
    private Date endDate;
    public SearchDateInterface(String startDate, String endDate) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat hourFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date sdate = simpleDateFormat.parse(startDate.trim());
        Date edate = simpleDateFormat.parse(endDate.trim());

        Calendar cal = Calendar.getInstance();

        cal.setTime(edate);
        cal.add(Calendar.HOUR, 23);
        cal.add(Calendar.MINUTE, 59);
        cal.add(Calendar.SECOND, 59);

       /* String formatSdate = hourFormat.format(sdate);
        String formatEdate = hourFormat.format(cal.getTime());*/

        // 2021-09-28 12:12:00 => 2021-09-28 00:00:00
        this.startDate = sdate;
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
