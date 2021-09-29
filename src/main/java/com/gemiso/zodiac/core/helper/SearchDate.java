package com.gemiso.zodiac.core.helper;

import java.util.Date;

public class SearchDate {
    private Date startDate;
    private Date endDate;
    public SearchDate(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }
}
