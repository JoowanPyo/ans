package com.gemiso.zodiac.core.service;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.core.enumeration.AuthEnum;
import com.gemiso.zodiac.core.enumeration.FixEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessArticleFix {

    private String ErrorMessage;

    public String getErrorMessage() {
        return ErrorMessage;
    }

    // 기사 정보
    private Article originalArticle;

    public void setArticle(Article article) {
        originalArticle = article;
    }

    // 새로운 승인상태
    private String approveCode;

    public void setApproveCode(String code) {
        approveCode = code;
    }


    //현재 픽스상테에서 픽스가 가능한지 확인.
    public boolean getFixStatus(String userId, List<String> fixAuthList) {

        if (ObjectUtils.isEmpty(originalArticle)){
            return false;
        }
        //원본 기사 승인구분코드 get
        String orgApproveCode = originalArticle.getApprvDivCd();

        String articleFixUser = originalArticle.getArtclFixUser(); // null일경우, 에러
        String editorFixUser = originalArticle.getEditorFixUser();
        String anchorFixUser = originalArticle.getAnchorFixUser();
        String deskFixUser = originalArticle.getDeskFixUser();

        //픽스 권한 리스트로 들어온 파라미터 중 최상위 권한 get
        AuthEnum fixAuth = AuthEnum.certity(fixAuthList);

        FixEnum getOrgApprveCode =getFixEnum(orgApproveCode);
        FixEnum getNewApproveCode = getFixEnum(approveCode);

        //픽스권한 사용가능여부 체크
        boolean ret = isFix(fixAuth,
                getOrgApprveCode,
                getNewApproveCode,
                userId, articleFixUser, editorFixUser, anchorFixUser, deskFixUser);

         return ret;
    }

    public FixEnum getFixEnum(String approveCode){

        FixEnum returnFixCode = null;
        switch (approveCode){
            case "fix_none":
                returnFixCode = FixEnum.FIX_NONE;
                break;
            case "article_fix":
                returnFixCode = FixEnum.ARTICLE_FIX;
                break;
            case "editor_fix":
                returnFixCode = FixEnum.EDITOR_FIX;
                break;
            case "anchor_fix":
                returnFixCode = FixEnum.ANCHOR_FIX;
                break;
            case "desk_fix":
                returnFixCode = FixEnum.DESK_FIX;
                break;

        }
        return returnFixCode;
    }

    public boolean isFix( AuthEnum auth,  FixEnum DbApprove , FixEnum newApprove, String userId, String articleFixUser,
                          String editorFixUser, String anchorFixUser, String deskFixUser )
    {
        if( newApprove.equals(DbApprove))
            return false;

        if( auth.equals( AuthEnum.ArticleFix) )
            return articleFix(DbApprove, newApprove, articleFixUser, userId);
        else if( auth.equals( AuthEnum.EditorFix) )
            return editorFix(DbApprove, newApprove,  editorFixUser,  userId);
        else if (auth.equals(AuthEnum.AnchorFix))
            return anchorFix(DbApprove, newApprove,  anchorFixUser,  userId);
        else if (auth.equals(AuthEnum.DeskFix))
            return deskFix(DbApprove, newApprove);
        else if (auth.equals(AuthEnum.PD))
            return pdFix(DbApprove, newApprove);
        else if (auth.equals(AuthEnum.AdminWrite))
            return adminFix(DbApprove, newApprove);
        //return editorFix(DbApprove, newApprove);

        return false;


    }

    private static  boolean IsUnfix( FixEnum newf , FixEnum oldf){
        return newf.ordinal() > oldf.ordinal();
    }

    public static boolean articleFix( FixEnum DbApprove , FixEnum newApprove ,String articleFixUser, String userId )
    {
        List<FixEnum> confirmList = new ArrayList<>();

        if( IsUnfix( DbApprove, newApprove) )//비교값넣기 맵index로 FixEnum.valueOf(DbApprove.toString()) < newApprove
        {   // 해제
            if (userId.equals(articleFixUser)== false) {
                return false;
            }

            confirmList.add(FixEnum.FIX_NONE);
        }
        else
        {   // Fix 하기
            confirmList.add(FixEnum.ARTICLE_FIX);
        }

        return confirmList.contains(newApprove);
    }

    public  boolean editorFix( FixEnum DbApprove , FixEnum newApprove, String editorFixUser, String userId )
    {
        List<FixEnum> confirmList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            if (userId.equals(editorFixUser)== false) {
                return false;
            }
            confirmList.add(FixEnum.ARTICLE_FIX);
        }
        else
        {
            confirmList.add(FixEnum.EDITOR_FIX);
        }


        return confirmList.contains(newApprove);
    }

    public boolean anchorFix( FixEnum DbApprove , FixEnum newApprove, String anchorFixUser, String userId )
    {

        List<FixEnum> confirmList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            if (userId.equals(anchorFixUser)) {
                confirmList.add(FixEnum.EDITOR_FIX);
            }else {
                confirmList.add(FixEnum.FIX_NONE);
                confirmList.add(FixEnum.ARTICLE_FIX);
            }
        }
        else
        {
            confirmList.add(FixEnum.EDITOR_FIX);
            confirmList.add(FixEnum.ANCHOR_FIX);
        }

        return confirmList.contains(newApprove);

    }

    public boolean deskFix( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> confirmList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) ) //desk는 앵커포함 이전픽스도 풀기 가능.
        {
            confirmList.add(FixEnum.FIX_NONE);
            confirmList.add(FixEnum.ARTICLE_FIX);
            confirmList.add(FixEnum.EDITOR_FIX);
            confirmList.add(FixEnum.ANCHOR_FIX);
        }
        else
        {
            //    confirmList.add(ARTICLE_FIX); //desk는 에디터 이후 픽스 걸기 가능.
            confirmList.add(FixEnum.EDITOR_FIX);
            confirmList.add(FixEnum.ANCHOR_FIX);
            confirmList.add(FixEnum.DESK_FIX);
        }

        return confirmList.contains(newApprove);
    }


    public boolean pdFix( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> confirmList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )//pd는 픽스를 풀수 있는 권한만 있음.
        {
            confirmList.add(FixEnum.FIX_NONE);
            confirmList.add(FixEnum.ARTICLE_FIX);
            confirmList.add(FixEnum.EDITOR_FIX);
            confirmList.add(FixEnum.ANCHOR_FIX);
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
            confirmList.add(FixEnum.FIX_NONE);
            confirmList.add(FixEnum.ARTICLE_FIX);
            confirmList.add(FixEnum.EDITOR_FIX);
            confirmList.add(FixEnum.ANCHOR_FIX);
        }
        else
        {
            confirmList.add(FixEnum.ARTICLE_FIX);
            confirmList.add(FixEnum.EDITOR_FIX);
            confirmList.add(FixEnum.ANCHOR_FIX);
            confirmList.add(FixEnum.DESK_FIX);
        }


        return confirmList.contains(newApprove);
    }

}