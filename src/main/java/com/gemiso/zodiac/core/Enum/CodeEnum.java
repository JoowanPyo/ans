package com.gemiso.zodiac.core.Enum;

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
