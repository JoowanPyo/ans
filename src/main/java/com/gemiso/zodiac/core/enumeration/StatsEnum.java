package com.gemiso.zodiac.core.enumeration;

public enum StatsEnum {

    ORIGINAL("원본"),
    COPY("사본");

    private String articleDiv;

    StatsEnum(String articleDiv){
        this.articleDiv = articleDiv;
    }

    public String getStatsEcum(StatsEnum articleDiv){
        return this.articleDiv;
    }
}
