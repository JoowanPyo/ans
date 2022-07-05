package com.gemiso.zodiac.app.elasticsearch;

import com.gemiso.zodiac.app.ArticleTag.ArticleTag;
import com.gemiso.zodiac.app.ArticleTag.ArticleTagRepository;
import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.articleMedia.ArticleMedia;
import com.gemiso.zodiac.app.articleMedia.ArticleMediaRepository;
import com.gemiso.zodiac.app.code.Code;
import com.gemiso.zodiac.app.code.CodeRepository;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.dept.Depts;
import com.gemiso.zodiac.app.dept.DeptsRepository;
import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasricSearchArticleMedia;
import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasticSearchArticle;
import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasticSearchArticleTags;
import com.gemiso.zodiac.app.tag.Tag;
import com.gemiso.zodiac.app.tag.TagRepository;
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.UserRepository;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ElasticSearchArticleService {

    private final CodeRepository codeRepository;
    private final DeptsRepository deptsRepository;
    private final UserRepository userRepository;
    private final ArticleTagRepository articleTagRepository;
    private final ArticleMediaRepository articleMediaRepository;
    private final TagRepository tagRepository;
    private final ElasticSearchArticleRepository elasticSearchArticleRepository;

    private final DateChangeHelper dateChangeHelper;

    //엘라스틱 서치 업데이트
    public void elasticPush(Article article) throws ParseException {

        try {

            Date getInputDtm = article.getInputDtm();

            log.info("ElasticSearch Date Format : " + getInputDtm);

            String inputDtm = null;
            if (ObjectUtils.isEmpty(getInputDtm) == false) {

                inputDtm = dateChangeHelper.dateToStringNormal(getInputDtm);
            }

            CueSheet cueSheet = article.getCueSheet();

            String brdcPgmNm = null;
            Long cueId = null;
            Long subrmId = null;
            if (ObjectUtils.isEmpty(cueSheet) == false) {

                brdcPgmNm = Optional.ofNullable(cueSheet.getBrdcPgmNm()).orElse(null);
                cueId = Optional.ofNullable(cueSheet.getCueId()).orElse(null);
                subrmId = Optional.ofNullable(cueSheet.getSubrmId()).orElse(null);
            }

            //코드네임 셋팅
            article = setCode(article);

            article = setUser(article);

            article = setDept(article);

            Long articleId = article.getArtclId();

            List<ElasticSearchArticleTags> tags = findTags(articleId);

            List<ElasricSearchArticleMedia> medias = findMedia(articleId);

            ElasticSearchArticle entity = ElasticSearchArticle.builder()
                    .ancMentCtt(article.getAncMentCtt())
                    .apprvDivCd(article.getApprvDivCd())
                    .apprvDivCdNm(article.getApprvDivCdNm())
                    .artclCateCd(article.getArtclCateCd())
                    .artclCateCdNm(article.getArtclCateCdNm())
                    .artclDivCd(article.getArtclDivCd())
                    .artclId(article.getArtclId())
                    .artclOrd(article.getArtclOrd())
                    .artclTitl(article.getArtclTitl())
                    .artclTitlEn(article.getArtclTitlEn())
                    .artclTypCd(article.getArtclTypCd())
                    .artclTypCdNm(article.getArtclTypCdNm())
                    .artclTypDtlCd(article.getArtclTypDtlCd())
                    .artclTypDtlCdNm(article.getArtclTypDtlCdNm())
                    .artclCtt(article.getArtclCtt())
                    .brdcPgmId(article.getBrdcPgmId())
                    .delYn(article.getDelYn())
                    .deptCd(article.getDeptCd())
                    .deptNm(article.getDeptNm())
                    .embgYn(article.getEmbgYn())
                    .inputDtm(inputDtm)
                    .inputrId(article.getInputrId())
                    .inputrNm(article.getInputrNm())
                    .lckYn(article.getLckYn())
                    .orgArtclId(article.getOrgArtclId())
                    .rptrId(article.getRptrId())
                    .rptrNm(article.getRptrNm())
                    .brdcPgmNm(brdcPgmNm)
                    .cueId(cueId)
                    .subrmId(subrmId)
                    .ancMentCttTime(article.getAncMentCttTime())
                    .artclCttTime(article.getArtclCttTime())
                    .artclExtTime(article.getArtclExtTime())
                    .editorFixUser(article.getEditorFixUser())
                    .editorFixUserNm(article.getEditorFixUserNm())
                    .tags(tags)
                    .articleMedias(medias)
                    .build();

            elasticSearchArticleRepository.save(entity);
        }catch (Exception e){
            log.error("ElasticSearch Error : "+e.getMessage());
        }
    }

    public List<ElasricSearchArticleMedia> findMedia(Long articleId){

        List<ElasricSearchArticleMedia> mediaList = new ArrayList<>();

        List<ArticleMedia> articleMediaList = articleMediaRepository.findArticleMediaList(articleId);

        for (ArticleMedia articleMedia : articleMediaList){

            ElasricSearchArticleMedia elasricSearchArticleMedia = new ElasricSearchArticleMedia();
            elasricSearchArticleMedia.setArtclMediaId(articleMedia.getArtclMediaId());
            elasricSearchArticleMedia.setMediaDurtn(articleMedia.getMediaDurtn());
            mediaList.add(elasricSearchArticleMedia);
        }

        return mediaList;
    }

    public List<ElasticSearchArticleTags> findTags(Long articleId){

        List<ElasticSearchArticleTags> tagsList = new ArrayList<>();

        List<ArticleTag>  articleTagList = articleTagRepository.findArticleTag(articleId);

        for (ArticleTag articleTag : articleTagList){

            Tag tag = articleTag.getTag();

            if (ObjectUtils.isEmpty(tag) == false) {

                Long tagId = tag.getTagId();

                Optional<Tag> tagEntity = tagRepository.findByTag(tagId);

                if (tagEntity.isPresent()) {

                    Tag getTag = tagEntity.get();

                    ElasticSearchArticleTags elasticSearchArticleTags = new ElasticSearchArticleTags();
                    elasticSearchArticleTags.setTag(getTag.getTag());
                    elasticSearchArticleTags.setTagId(getTag.getTagId());

                    tagsList.add(elasticSearchArticleTags);
                }
            }
        }

        return tagsList;
    }

    public Article setCode(Article article) {


        List<Code> codeList = codeRepository.findAll();

        for (Code code : codeList) {

            String cd = code.getCd();
            String cdNm = code.getCdNm();

            String apprvDivCd = article.getApprvDivCd();
            if (apprvDivCd != null && apprvDivCd.trim().isEmpty() == false) {

                if (apprvDivCd.equals(cd)) {
                    article.setApprvDivCdNm(cdNm);
                }
            }

            String artclCateCd = article.getArtclCateCd();
            if (artclCateCd != null && artclCateCd.trim().isEmpty() == false) {

                if (artclCateCd.equals(cd)) {
                    article.setArtclCateCdNm(cdNm);
                }
            }

            String artclTypCd = article.getArtclTypCd();
            if (artclTypCd != null && artclTypCd.trim().isEmpty() == false) {

                if (artclTypCd.equals(cd)) {
                    article.setArtclTypCdNm(cdNm);
                }
            }

            String artclTypDtlCd = article.getArtclTypDtlCd();
            if (artclTypDtlCd != null && artclTypDtlCd.trim().isEmpty() == false) {

                if (artclTypDtlCd.equals(cd)) {
                    article.setArtclTypDtlCdNm(cdNm);
                }
            }


        }

        return article;
    }

    public Article setUser(Article article) {

        String inputrId = article.getInputrId();
        String rptrId = article.getRptrId();
        String editorFixUser = article.getEditorFixUser();

        List<User> userList = userRepository.findAll();

        for (User user : userList) {

            String userId = user.getUserId();
            String userNm = user.getUserNm();

            if (userId.equals(inputrId)) {
                article.setInputrNm(userNm);
            }

            if (userId.equals(rptrId)) {
                article.setRptrNm(userNm);
            }
            if (userId.equals(editorFixUser)){
                article.setEditorFixUserNm(userNm);
            }
        }

        if (inputrId == null || inputrId.isEmpty()){
            article.setInputrNm(null);
        }
        if (rptrId == null || rptrId.isEmpty()){
            article.setRptrNm(null);
        }
        if (editorFixUser == null || editorFixUser.isEmpty()){
            article.setEditorFixUserNm(null);
        }

        return article;
    }

    public Article setDept(Article article) {

        Long deptCd = article.getDeptCd();

        if (ObjectUtils.isEmpty(deptCd) == false) {

            Optional<Depts> depts = deptsRepository.findDept(deptCd);

            if (depts.isPresent()) {
                Depts dept = depts.get();
                String deptNm = dept.getName();

                article.setDeptNm(deptNm);
            }
        }

        return article;
    }
}
