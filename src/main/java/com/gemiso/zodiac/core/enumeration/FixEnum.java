package com.gemiso.zodiac.core.enumeration;

import java.util.HashMap;

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

    public String getFixeum(FixEnum fixEnum){
        return this.approveCode;
    }

   /* public boolean isFix( FixAuth auth,  FixEnum DbApprove , FixEnum newApprove, String apprvrId, String userId )
    {
        if( DbApprove.equals(newApprove))
            return false;

        if( auth.equals( FixAuth.REPORTER) )
            return articleFix(DbApprove, newApprove, apprvrId, userId);
        else if( auth.equals( FixAuth.EDITOR) )
            return editorFix(DbApprove, newApprove);
        else if (auth.equals(FixAuth.ANCHOR))
            return anchorFix(DbApprove, newApprove);
        else if (auth.equals(FixAuth.DESK))
            return destFix(DbApprove, newApprove);
        else if (auth.equals(FixAuth.PD))
            return pdFix(DbApprove, newApprove);
        else if (auth.equals(FixAuth.ADMIN))
            return adminFix(DbApprove, newApprove);
            //return editorFix(DbApprove, newApprove);

        return false;


    }

    private static  boolean IsUnfix( FixEnum newf , FixEnum oldf){
        return enumFix.get(newf.approveCode) > enumFix.get(oldf.approveCode);
    }

    public static boolean articleFix( FixEnum DbApprove , FixEnum newApprove ,String apprvrId, String userId )
    {
        List<FixEnum> confirmList = new ArrayList<>();

        if( IsUnfix( DbApprove, newApprove) )//비교값넣기 맵index로 FixEnum.valueOf(DbApprove.toString()) < newApprove
        {   // 해제
            if (apprvrId.equals(userId)== false) {
             return false;
            }

            confirmList.add(FIX_NONE);
        }
        else
        {   // Fix 하기
            confirmList.add(ARTICLE_FIX);
        }

        return confirmList.contains(newApprove);
    }

    public  boolean editorFix( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> confirmList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            confirmList.add(ARTICLE_FIX);
        }
        else
        {
            confirmList.add(EDITOR_FIX);
        }


        return confirmList.contains(newApprove);
    }

    public boolean anchorFix( FixEnum DbApprove , FixEnum newApprove )
    {

        List<FixEnum> confirmList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            confirmList.add(FIX_NONE);
            confirmList.add(ARTICLE_FIX);
            confirmList.add(EDITOR_FIX);
        }
        else
        {
            confirmList.add(EDITOR_FIX);
            confirmList.add(ANCHOR_FIX);
        }

        return confirmList.contains(newApprove);

    }

    public boolean destFix( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> confirmList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            confirmList.add(FIX_NONE);
            confirmList.add(ARTICLE_FIX);
            confirmList.add(EDITOR_FIX);
            confirmList.add(ANCHOR_FIX);
        }
        else
        {
        //    confirmList.add(ARTICLE_FIX);
            confirmList.add(EDITOR_FIX);
            confirmList.add(ANCHOR_FIX);
            confirmList.add(DESK_FIX);
        }

        return confirmList.contains(newApprove);
    }


    public boolean pdFix( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> confirmList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            confirmList.add(FIX_NONE);
            confirmList.add(ARTICLE_FIX);
            confirmList.add(EDITOR_FIX);
            confirmList.add(ANCHOR_FIX);
        }
        else
        {
            return false;
        }

        return confirmList.contains(newApprove);
    }

    public boolean adminFix( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> confirmList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            confirmList.add(FIX_NONE);
            confirmList.add(ARTICLE_FIX);
            confirmList.add(EDITOR_FIX);
            confirmList.add(ANCHOR_FIX);
        }
        else
        {
            confirmList.add(ARTICLE_FIX);
            confirmList.add(EDITOR_FIX);
            confirmList.add(ANCHOR_FIX);
            confirmList.add(DESK_FIX);
        }


        return confirmList.contains(newApprove);
    }*/



}
