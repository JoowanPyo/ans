package com.gemiso.zodiac.core.enumeration;

public enum CodeEnum {

    NORMER("01"),
    ISSUE("02"),
    VIDEO("video_icons"),
    AUDIO("audio_icons")
    ;

    private String code;

    CodeEnum(String code){
        this.code = code;
    }

    public String getCode(CodeEnum codeEnum){
        return code;
    }
}
