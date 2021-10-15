package com.gemiso.zodiac.core.fixEnum;

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
    CueSheetWrite("cuesheet.write"),
    CueSheetRead("cuesheet.read"),
    CueSheetOrder("cuesheet.order"),
    AdminWrite("admin.write"),
    AdminRead("admin.read"),
    PD("Pd"),
    Empty("");

    private static String ArticleFix_cd = "article.articlefix";
    private static String EditorFix_cd = "article.editorfix";
    private static String AnchorFix_cd = "article.anchorfix";
    private static String DeskFix_cd = "article.deskfix";
    private static String AdminWrite_cd = "admin.write";
    private static String PD_cd = "Pd";

    private String auth;

    AuthEnum(String ahth) {

        this.auth = ahth;
    }

    public static AuthEnum certity(List<String> certityList){

        if (certityList.contains(AdminWrite_cd))
            return AdminWrite;
        if (certityList.contains(DeskFix_cd))
            return DeskFix;
        if (certityList.contains(AnchorFix_cd))
            return AnchorFix;
        if (certityList.contains(EditorFix_cd))
            return EditorFix;
        if (certityList.contains(ArticleFix_cd))
            return ArticleFix;

        return AuthEnum.Empty;

    }
}
