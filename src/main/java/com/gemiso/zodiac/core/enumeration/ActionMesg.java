package com.gemiso.zodiac.core.enumeration;

public enum ActionMesg {

    articleC("article create"),
    articleU("article update"),
    articleD("article delete"),
    articleTM("article title modify"),
    articleTEM("article english title modify"),
    articleCM("article contents modify"),
    anchorMM("anchor ment contents modify"),
    articleOPEN("the article open"),
    articleCLOSE("the article close"),
    fixM("the news fix staus changed");

    String mesg;

    ActionMesg(String mesg){
        this.mesg = mesg;
    }

    public String getActionMesg(ActionMesg actionMesg){
        return this.mesg;
    }
}
