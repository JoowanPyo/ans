package com.gemiso.zodiac.core.enumeration;

public enum CodeEnum {

    NORMER("01"),
    ISSUE("02");

    private String code;

    CodeEnum(String code){
        this.code = code;
    }

    public String getCode(){
        return code;
    }
}
