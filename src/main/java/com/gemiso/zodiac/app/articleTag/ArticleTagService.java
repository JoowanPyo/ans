package com.gemiso.zodiac.app.articleTag;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.articleTag.dto.ArticleTagCreateDTO;
import com.gemiso.zodiac.app.articleTag.dto.ArticleTagDTO;
import com.gemiso.zodiac.app.articleTag.mapper.ArticleTagCreateMapper;
import com.gemiso.zodiac.app.articleTag.mapper.ArticleTagMapper;
import com.gemiso.zodiac.app.tag.Tag;
import com.gemiso.zodiac.app.tag.TagRepository;
import com.gemiso.zodiac.app.tag.TagService;
import com.gemiso.zodiac.app.tag.dto.TagCreateDTO;
import com.gemiso.zodiac.app.tag.dto.TagDTO;
import com.gemiso.zodiac.app.tag.dto.TagIdDTO;
import com.gemiso.zodiac.app.tag.mapper.TagMapper;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleTagService {

    private final ArticleTagRepository articleTagRepository;
    private final TagRepository tagRepository;
    //private final ElasticSearchArticleRepository elasticSearchArticleRepository;
    //private final ArticleRepository articleRepository;

    private final ArticleTagMapper articleTagMapper;
    private final ArticleTagCreateMapper articleTagCreateMapper;
    private final TagMapper tagMapper;

    private final TagService tagService;
    //private final ArticleService articleService;

    //private final DateChangeHelper dateChangeHelper;


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

    public void create(Long artclId, List<String> tagList){


        //???????????? ?????? ???????????? List ??????
        List<ArticleTag> articleTagList = articleTagRepository.findArticleTag(artclId);

        deleteArticleTag(articleTagList); //????????? ???????????? ?????? ???????????? List ??????

        if (CollectionUtils.isEmpty(tagList) == false) {
            for (String tag : tagList) { //????????? ??????List

                Tag tagEntity = chkTags(tag);//?????? ??????????????????, ????????? ????????? ???????????? ?????????, ?????? or ????????? ???????????? return  tagId

                ArticleTagCreateDTO articleTagCreateDTO = new ArticleTagCreateDTO(); //???????????? ?????? articleDTO

                TagIdDTO tagIdDTO = TagIdDTO.builder().tagId(tagEntity.getTagId()).build(); //??????DTO ?????? (?????? ?????????)
                ArticleSimpleDTO articleId = ArticleSimpleDTO.builder().artclId(artclId).build();//??????DTO ?????? (?????? ?????????)
                articleTagCreateDTO.setArticle(articleId);
                articleTagCreateDTO.setTag(tagIdDTO);

                ArticleTag articleTag = articleTagCreateMapper.toEntity(articleTagCreateDTO);//???????????? ?????? ????????? ??????

                articleTagRepository.save(articleTag);//???????????? ??????

            }
        }


    }

    public void deleteArticleTag(List<ArticleTag> articleTagList){ //???????????? ??????

        //????????? ???????????? ?????? ???????????? List ??????
        if (CollectionUtils.isEmpty(articleTagList) == false){
            for (ArticleTag articleTag : articleTagList){

                Tag tag = articleTag.getTag();
                Integer clicked = tag.getTagClicked();
                if (clicked != 0) {
                    tag.setTagClicked(clicked - 1);
                    tagRepository.save(tag);
                }

                Long articleTagId = articleTag.getId();

                articleTagRepository.deleteById(articleTagId);
            }
        }
    }

    public Tag chkTags(String tag){ //?????? ??????????????????, ????????? ????????? ???????????? ?????????, ?????? or ????????? ????????????

        Optional<Tag> findTag = tagRepository.findTagName(tag);//????????? ????????? ???????????? ?????? ??????????????? ??????

        if (findTag.isPresent()){ //???????????? ?????? ????????? ????????????
            Tag tagEntity = findTag.get(); //???????????? ????????? ?????? get

            TagDTO tagDTO = tagMapper.toDto(tagEntity);//?????? articleDTO ??????

            int tagClicked = tagDTO.getTagClicked();//????????? ?????? ????????? get
            tagDTO.setTagClicked(tagClicked+1); //DTO??? ?????? ????????? +1set

            tagMapper.updateFromDto(tagDTO, tagEntity); //????????? ?????? ????????? ????????????
            tagRepository.save(tagEntity); //?????? ????????? ????????????

            return tagEntity;

        }else{
            TagCreateDTO tagCreateDTO = new TagCreateDTO();//????????????DTO

            tagCreateDTO.setTag(tag);//????????????DTO??? ????????? set

            return tagService.create(tagCreateDTO);//????????? ????????????

        }

    }


}
