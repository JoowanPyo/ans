package com.gemiso.zodiac.core.enumeration;

public enum ActionMesg {

    articleC("Created"),
    articleU("Updated"),
    articleD("Deleted"),
    articleTM("Korean Title Modified"),
    articleTEM("English Title Modified"),
    articleCM("article contents modify"),
    anchorMM("Anchor Lead Updated"),
    articleOPEN("the article open"),
    articleCLOSE("the article close"),
    articleLOCK("article Lock"),
    articleUNLOCK("article UnLock"),
    articleForcedLock("Overriden"),
    fixM("Status Changed"),
    mediaC("Linked Video"),
    mediaD("UnLinked Video");

    String mesg;

    ActionMesg(String mesg){
        this.mesg = mesg;
    }

    public String getActionMesg(ActionMesg actionMesg){
        return this.mesg;
    }
}
