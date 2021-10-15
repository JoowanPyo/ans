package com.gemiso.zodiac.core.service;

import com.gemiso.zodiac.app.appAuth.AppAuth;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.core.fixEnum.AuthEnum;
import com.gemiso.zodiac.core.fixEnum.FixAuth;
import com.gemiso.zodiac.core.fixEnum.FixEnum;
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


    public boolean getFixStatus(String userId, List<String> fixAuthList) {

        if (ObjectUtils.isEmpty(originalArticle)){
            return false;
        }
        //원본 기사 승인구분코드 get
        String orgApproveCode = originalArticle.getApprvDivCd();
        String apprvrId = originalArticle.getApprvr().getUserId();

        //픽스 권한 리스트로 들어온 파라미터 중 최상위 권한 get
        AuthEnum fixAuth = AuthEnum.certity(fixAuthList);

        FixEnum getOrgApprveCode =getFixEnum(orgApproveCode);
        FixEnum getNewApproveCode = getFixEnum(approveCode);

        //픽스권한 사용가능여부 체크
        boolean ret = isFix(fixAuth,
                getOrgApprveCode,
                getNewApproveCode,
                apprvrId,
                userId);

        return ret;
    }

    public FixEnum getFixEnum(String approveCode){

        FixEnum returnFixCode = null;
        switch (approveCode){
            case "none":
                returnFixCode = FixEnum.FIX_NONE;
                break;
            case "articlefix":
                returnFixCode = FixEnum.ARTICLE_FIX;
                break;
            case "editorfix":
                returnFixCode = FixEnum.EDITOR_FIX;
                break;
            case "anchorfix":
                returnFixCode = FixEnum.ANCHOR_FIX;
                break;
            case "deskfix":
                returnFixCode = FixEnum.DESK_FIX;
                break;

        }
        return returnFixCode;
    }

    public boolean isFix( AuthEnum auth,  FixEnum DbApprove , FixEnum newApprove, String apprvrId, String userId )
    {
        if( DbApprove.equals(newApprove))
            return false;

        if( auth.equals( AuthEnum.ArticleFix) )
            return isReporter(DbApprove, newApprove, apprvrId, userId);
        else if( auth.equals( AuthEnum.EditorFix) )
            return isEditor(DbApprove, newApprove,  apprvrId,  userId);
        else if (auth.equals(AuthEnum.AnchorFix))
            return isAnchor(DbApprove, newApprove,  apprvrId,  userId);
        else if (auth.equals(AuthEnum.DeskFix))
            return isDesk(DbApprove, newApprove);
        else if (auth.equals(AuthEnum.PD))
            return isPD(DbApprove, newApprove);
        else if (auth.equals(AuthEnum.AdminWrite))
            return isAdmin(DbApprove, newApprove);
        //return isEditor(DbApprove, newApprove);

        return false;


    }

    private static  boolean IsUnfix( FixEnum newf , FixEnum oldf){
        return newf.ordinal() > oldf.ordinal();
    }

    public static boolean isReporter( FixEnum DbApprove , FixEnum newApprove ,String apprvrId, String userId )
    {
        List<FixEnum> articleList = new ArrayList<>();

        if( IsUnfix( DbApprove, newApprove) )//비교값넣기 맵index로 FixEnum.valueOf(DbApprove.toString()) < newApprove
        {   // 해제
            if (apprvrId.equals(userId)== false) {
                return false;
            }

            articleList.add(FixEnum.FIX_NONE);
        }
        else
        {   // Fix 하기
            articleList.add(FixEnum.ARTICLE_FIX);
        }

        return articleList.contains(newApprove);
    }

    public  boolean isEditor( FixEnum DbApprove , FixEnum newApprove, String apprvrId, String userId )
    {
        List<FixEnum> articleList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            if (apprvrId.equals(userId)== false) {
                return false;
            }
            articleList.add(FixEnum.ARTICLE_FIX);
        }
        else
        {
            articleList.add(FixEnum.EDITOR_FIX);
        }


        return articleList.contains(newApprove);
    }

    public boolean isAnchor( FixEnum DbApprove , FixEnum newApprove, String apprvrId, String userId )
    {

        List<FixEnum> articleList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            if (apprvrId.equals(userId)) {
                articleList.add(FixEnum.EDITOR_FIX);
            }else {
                articleList.add(FixEnum.FIX_NONE);
                articleList.add(FixEnum.ARTICLE_FIX);
            }
        }
        else
        {
            articleList.add(FixEnum.EDITOR_FIX);
            articleList.add(FixEnum.ANCHOR_FIX);
        }

        return articleList.contains(newApprove);

    }

    public boolean isDesk( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> articleList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            articleList.add(FixEnum.FIX_NONE);
            articleList.add(FixEnum.ARTICLE_FIX);
            articleList.add(FixEnum.EDITOR_FIX);
            articleList.add(FixEnum.ANCHOR_FIX);
        }
        else
        {
            //    articleList.add(ARTICLE_FIX);
            articleList.add(FixEnum.EDITOR_FIX);
            articleList.add(FixEnum.ANCHOR_FIX);
            articleList.add(FixEnum.DESK_FIX);
        }

        return articleList.contains(newApprove);
    }


    public boolean isPD( FixEnum DbApprove , FixEnum newApprove )
    {
        List<FixEnum> articleList = new ArrayList<>();

        if (IsUnfix( DbApprove, newApprove) )
        {
            articleList.add(FixEnum.FIX_NONE);
            articleList.add(FixEnum.ARTICLE_FIX);
            articleList.add(FixEnum.EDITOR_FIX);
            articleList.add(FixEnum.ANCHOR_FIX);
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
            articleList.add(FixEnum.FIX_NONE);
            articleList.add(FixEnum.ARTICLE_FIX);
            articleList.add(FixEnum.EDITOR_FIX);
            articleList.add(FixEnum.ANCHOR_FIX);
        }
        else
        {
            articleList.add(FixEnum.ARTICLE_FIX);
            articleList.add(FixEnum.EDITOR_FIX);
            articleList.add(FixEnum.ANCHOR_FIX);
            articleList.add(FixEnum.DESK_FIX);
        }


        return articleList.contains(newApprove);
    }

}