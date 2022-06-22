package com.gemiso.zodiac.app.ArticleTag;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleRepository;
import com.gemiso.zodiac.app.article.ArticleService;
import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.elasticsearch.ElasticSearchArticleRepository;
import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasticSearchArticle;
import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasticSearchArticleTags;
import com.gemiso.zodiac.app.tag.Tag;
import com.gemiso.zodiac.app.tag.TagRepository;
import com.gemiso.zodiac.app.tag.TagService;
import com.gemiso.zodiac.app.tag.dto.*;
import com.gemiso.zodiac.app.ArticleTag.mapper.ArticleTagCreateMapper;
import com.gemiso.zodiac.app.ArticleTag.mapper.ArticleTagMapper;
import com.gemiso.zodiac.app.tag.mapper.TagMapper;
import com.gemiso.zodiac.app.ArticleTag.dto.ArticleTagCreateDTO;
import com.gemiso.zodiac.app.ArticleTag.dto.ArticleTagDTO;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleTagService {

    private final ArticleTagRepository articleTagRepository;
    private final TagRepository tagRepository;
    private final ElasticSearchArticleRepository elasticSearchArticleRepository;
    private final ArticleRepository articleRepository;

    private final ArticleTagMapper articleTagMapper;
    private final ArticleTagCreateMapper articleTagCreateMapper;
    private final TagMapper tagMapper;

    private final TagService tagService;
    private final ArticleService articleService;

    private final DateChangeHelper dateChangeHelper;


    public List<ArticleTagDTO> findAll(Long artclId){

        BooleanBuilder booleanBuilder = getSearch(artclId);

        List<ArticleTag> articleTagList = (List<ArticleTag>) articleTagRepository.findAll(booleanBuilder);

        List<ArticleTagDTO> articleTagDTOList = articleTagMapper.toDtoList(articleTagList);

        return articleTagDTOList;
    }

    public BooleanBuilder getSearch(Long artclId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QArticleTag qArticleTag = QArticleTag.articleTag;

        if (ObjectUtils.isEmpty(artclId) == false){
            booleanBuilder.and(qArticleTag.article.artclId.eq(artclId));
        }

        return booleanBuilder;
    }

    public List<ArticleTagDTO> find(Long articleId){

        List<ArticleTag> articleTagList = articleTagRepository.findArticleTag(articleId);

        List<ArticleTagDTO> articleTagDTOList = articleTagMapper.toDtoList(articleTagList);

        return articleTagDTOList;
    }

    public List<ElasticSearchArticleTags> create(Long artclId, List<String> tagList){


        //등록되어 있던 기사테그 List 조회
        List<ArticleTag> articleTagList = articleTagRepository.findArticleTag(artclId);

        deleteArticleTag(articleTagList); //기존에 등록되어 있던 기사테그 List 삭제

        List<ElasticSearchArticleTags> tags = new ArrayList<>();

        for (String tag : tagList){ //등록할 테그List

            Tag tagEntity = chkTags(tag);//테그 신규등록일지, 기존에 등록된 테그인지 검사후, 등록 or 클릭수 업데이트 return  tagId

            ArticleTagCreateDTO articleTagCreateDTO = new ArticleTagCreateDTO(); //기사테크 등록 articleDTO

            TagIdDTO tagIdDTO = TagIdDTO.builder().tagId(tagEntity.getTagId()).build(); //테그DTO 빌드 (테그 아이디)
            ArticleSimpleDTO articleId = ArticleSimpleDTO.builder().artclId(artclId).build();//기사DTO 빌드 (기사 아이디)
            articleTagCreateDTO.setArticle(articleId);
            articleTagCreateDTO.setTag(tagIdDTO);

            ArticleTag articleTag = articleTagCreateMapper.toEntity(articleTagCreateDTO);//기사테그 등록 엔티티 변환

            articleTagRepository.save(articleTag);//기사테그 등록

            ElasticSearchArticleTags elasticTag = new ElasticSearchArticleTags();
            elasticTag.setTag(tagEntity.getTag());
            elasticTag.setTagId(tagEntity.getTagId());

            tags.add(elasticTag);
            
        }

        return tags;

    }

    //엘라스틱서치 기사 테크 업데이트
    public void updateElasticArticle(List<ElasticSearchArticleTags> tags, Long artclId){

        Optional<Article> articleEntity = articleRepository.findArticle(artclId);

        if (articleEntity.isPresent()) {

            Article article = articleEntity.get();

            Date getInputDtm = article.getInputDtm();

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
            article = articleService.setCode(article);

            article = articleService.setUser(article);

            article = articleService.setDept(article);


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
                    .tags(tags)
                    .build();

            elasticSearchArticleRepository.save(entity);
        }

    }



    public void deleteArticleTag(List<ArticleTag> articleTagList){ //기사테그 삭제

        //기존에 등록되어 있던 기사테그 List 삭제
        if (CollectionUtils.isEmpty(articleTagList) == false){
            for (ArticleTag articleTag : articleTagList){
                Long articleTagId = articleTag.getId();

                articleTagRepository.deleteById(articleTagId);
            }
        }
    }

    public Tag chkTags(String tag){ //테그 신규등록일지, 기존에 등록된 테그인지 검사후, 등록 or 클릭수 업데이트

        Optional<Tag> findTag = tagRepository.findTagName(tag);//테그가 기존에 등록되어 있는 테그명인지 조회

        if (findTag.isPresent()){ //등록되어 있는 테그가 있을경우
            Tag tagEntity = findTag.get(); //옵션얼로 조회된 테그 get

            TagDTO tagDTO = tagMapper.toDto(tagEntity);//테그 articleDTO 변환

            int tagClicked = tagDTO.getTagClicked();//조회된 테그 클릭수 get
            tagDTO.setTagClicked(tagClicked+1); //DTO에 테그 클릭수 +1set

            tagMapper.updateFromDto(tagDTO, tagEntity); //수정된 테그 클릭수 업데이트
            tagRepository.save(tagEntity); //테그 조회수 업데이트

            return tagEntity;

        }else{
            TagCreateDTO tagCreateDTO = new TagCreateDTO();//테그등록DTO

            tagCreateDTO.setTag(tag);//테그등록DTO에 테크명 set

            return tagService.create(tagCreateDTO);//테크명 신규등록

        }

    }


}
