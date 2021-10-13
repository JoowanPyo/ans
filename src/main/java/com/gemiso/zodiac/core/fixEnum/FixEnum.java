package com.gemiso.zodiac.core.fixEnum;

import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public enum FixEnum {

    FIX_NONE(0, "none"),
    ARTICLE_FIX(1, "articlefix"),
    EDITOR_FIX(2, "editorfix"),
    ANCHOR_FIX(3, "anchorfix"),
    DESK_FIX(4, "deskfix");

    private static final HashMap<String, Integer> enumFix = new HashMap<>();
    static {
        for (FixEnum e: values()){
            enumFix.put(e.approveCode, e.index);
        }
    }

    private Integer index;
    private String approveCode;

    FixEnum(Integer id, String code){
        this.index = id;
        this.approveCode = code;
    }

    public static Integer valueOfLabel(String label){
        return enumFix.get(label);
    }

   /* public boolean isFix( FixAuth auth,  FixEnum DbApprove , FixEnum newApprove, String apprvrId, String userId )
    {
        if( DbApprove.equals(newApprove))
            return false;

        if( auth.equals( FixAuth.REPORTER) )
            return isReporter(DbApprove, newApprove, apprvrId, userId);
        else if( auth.equals( FixAuth.EDITOR) )
            return isEditor(DbApprove, newApprove);
        else if (auth.equals(FixAuth.ANCHOR))
            return isAnchor(DbApprove, newApprove);
        else if (auth.equals(FixAuth.DESK))
            return isDesk(DbApprove, newApprove);
        else if (auth.equals(FixAuth.PD))
            return isPD(DbApprove, newApprove);
        else if (auth.equals(FixAuth.ADMIN))
            return isAdmin(DbApprove, newApprove);
            //return isEditor(DbApprove, newApprove);

        return false;


    }

    private static  boolean IsUnfix( FixEnum newf , FixEnum oldf){
        return enumFix.get(newf.approveCode) > enumFix.get(oldf.approveCode);
    }

    public static boolean isReporter( FixEnum DbApprove , FixEnum newApprove ,String apprvrId, String userId )
    {
        List<FixEnum> articleList = new ArrayList<>();

        if( IsUnfix( DbApprove, newApprove) )//비교값넣기 맵index로 FixEnum.valueOf(DbApprove.toString()) < newApprove
        {   // 해제
            if (apprvrId.equals(userId)== false) {
             return false;
            }

            articleList.add(FIX_NONE);
        }
        else
        {   // Fix 하기
            articleList.add(ARTICLE_FIX);
        }

        return articleList.contains(newApprove);
    }

    public  boolean isEditor( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> articleList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            articleList.add(ARTICLE_FIX);
        }
        else
        {
            articleList.add(EDITOR_FIX);
        }


        return articleList.contains(newApprove);
    }

    public boolean isAnchor( FixEnum DbApprove , FixEnum newApprove )
    {

        List<FixEnum> articleList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            articleList.add(FIX_NONE);
            articleList.add(ARTICLE_FIX);
            articleList.add(EDITOR_FIX);
        }
        else
        {
            articleList.add(EDITOR_FIX);
            articleList.add(ANCHOR_FIX);
        }

        return articleList.contains(newApprove);

    }

    public boolean isDesk( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> articleList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            articleList.add(FIX_NONE);
            articleList.add(ARTICLE_FIX);
            articleList.add(EDITOR_FIX);
            articleList.add(ANCHOR_FIX);
        }
        else
        {
        //    articleList.add(ARTICLE_FIX);
            articleList.add(EDITOR_FIX);
            articleList.add(ANCHOR_FIX);
            articleList.add(DESK_FIX);
        }

        return articleList.contains(newApprove);
    }


    public boolean isPD( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> articleList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            articleList.add(FIX_NONE);
            articleList.add(ARTICLE_FIX);
            articleList.add(EDITOR_FIX);
            articleList.add(ANCHOR_FIX);
        }
        else
        {
            return false;
        }

        return articleList.contains(newApprove);
    }

    public boolean isAdmin( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> articleList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            articleList.add(FIX_NONE);
            articleList.add(ARTICLE_FIX);
            articleList.add(EDITOR_FIX);
            articleList.add(ANCHOR_FIX);
        }
        else
        {
            articleList.add(ARTICLE_FIX);
            articleList.add(EDITOR_FIX);
            articleList.add(ANCHOR_FIX);
            articleList.add(DESK_FIX);
        }


        return articleList.contains(newApprove);
    }*/



}
