package com.gemiso.zodiac.core.enumeration;

public enum ActionEnum {

    READ("read"),
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete"),
    OPEN("open"),
    CLOSE("close"),
    FIX("fix");

    String action;

    ActionEnum(String action){

        this.action = action;

    }

    public String getAction(ActionEnum actionEnum){
        return this.action;
    }
}
