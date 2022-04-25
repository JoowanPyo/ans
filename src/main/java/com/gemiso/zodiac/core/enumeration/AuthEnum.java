package com.gemiso.zodiac.core.enumeration;

import java.util.List;

public enum AuthEnum {

    IssueRead("issue.read"),
    IssueWrite("issue.write"),
    IssueOrder("issue.order"),
    IssueDelete("issue.delete"),
    ArticleFix("article.articlefix"),
    EditorFix("article.editorfix"),
    AnchorFix("article.anchorfix"),
    DeskFix("article.deskfix"),
    ArticleWrite("article.write"),
    ArticleRead("article.read"),
    CueShhetCopy("cuesheet.copy"),
    CueSheetCreate("cuesheet.create"),
    CueSheetDelete("cuesheet.delete"),
    CueSheetEdit("cuesheet.edit"),
    CheSheetTemplate("cuesheet.templete"),
    CheSheetView("cuesheet.view"),
    CueSheetDetailOrder("cuesheetDetail.order"),
    AdminMode("admin.mode"),
    Empty(""),;

    private static String ArticleFix_cd = "article.articlefix";
    private static String EditorFix_cd = "article.editorfix";
    private static String AnchorFix_cd = "article.anchorfix";
    private static String DeskFix_cd = "article.deskfix";
    private static String Admin_cd = "admin.mode";
    private static String PD_cd = "Pd";

    private String auth;

    AuthEnum(String ahth) {

        this.auth = ahth;
    }

    public String getAuth(){
        return auth;
    }

    public static AuthEnum certity(List<String> certityList){

        //if (certityList.contains(Admin_cd))
            //return AdminMode;
        if (certityList.contains(DeskFix_cd))
            return DeskFix;
        if (certityList.contains(AnchorFix_cd))
            return AnchorFix;
        if (certityList.contains(EditorFix_cd))
            return EditorFix;
        if (certityList.contains(ArticleFix_cd))
            return ArticleFix;
       // if (certityList.contains(PD))
       //     return PD;

        return AuthEnum.Empty;

    }
}
