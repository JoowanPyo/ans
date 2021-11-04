package com.gemiso.zodiac.app.tag;

import com.gemiso.zodiac.app.article.dto.ArticleSimpleDTO;
import com.gemiso.zodiac.app.tag.dto.*;
import com.gemiso.zodiac.app.tag.mapper.ArticleTagCreateMapper;
import com.gemiso.zodiac.app.tag.mapper.ArticleTagMapper;
import com.gemiso.zodiac.app.tag.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleTagService {

    private final ArticleTagRepository articleTagRepository;
    private final TagRepository tagRepository;

    private final ArticleTagMapper articleTagMapper;
    private final ArticleTagCreateMapper articleTagCreateMapper;
    private final TagMapper tagMapper;

    private final TagService tagService;


    public List<ArticleTagDTO> find(Long articleId){

        List<ArticleTag> articleTagList = articleTagRepository.findArticleTag(articleId);

        List<ArticleTagDTO> articleTagDTOList = articleTagMapper.toDtoList(articleTagList);

        return articleTagDTOList;
    }

    public void create(Long artclId, List<String> tagList){


        //등록되어 있던 기사테그 List 조회
        List<ArticleTag> articleTagList = articleTagRepository.findArticleTag(artclId);

        deleteArticleTag(articleTagList); //기존에 등록되어 있던 기사테그 List 삭제


        for (String tag : tagList){ //등록할 테그List

            Long tagId = chkTags(tag);//테그 신규등록일지, 기존에 등록된 테그인지 검사후, 등록 or 클릭수 업데이트 return  tagId

            ArticleTagCreateDTO articleTagCreateDTO = new ArticleTagCreateDTO(); //기사테크 등록 DTO

            TagIdDTO tagIdDTO = TagIdDTO.builder().tagId(tagId).build(); //테그DTO 빌드 (테그 아이디) 
            ArticleSimpleDTO articleId = ArticleSimpleDTO.builder().artclId(artclId).build();//기사DTO 빌드 (기사 아이디) 
            articleTagCreateDTO.setArticle(articleId);
            articleTagCreateDTO.setTag(tagIdDTO);

            ArticleTag articleTag = articleTagCreateMapper.toEntity(articleTagCreateDTO);//기사테그 등록 엔티티 변환

            articleTagRepository.save(articleTag);//기사테그 등록
            
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

    public Long chkTags(String tag){ //테그 신규등록일지, 기존에 등록된 테그인지 검사후, 등록 or 클릭수 업데이트

        Optional<Tag> findTag = tagRepository.findTagName(tag);//테그가 기존에 등록되어 있는 테그명인지 조회

        if (findTag.isPresent()){ //등록되어 있는 테그가 있을경우
            Tag tagEntity = findTag.get(); //옵션얼로 조회된 테그 get

            TagDTO tagDTO = tagMapper.toDto(tagEntity);//테그 DTO 변환

            int tagClicked = tagDTO.getTagClicked();//조회된 테그 클릭수 get
            tagDTO.setTagClicked(tagClicked+1); //DTO에 테그 클릭수 +1set

            tagMapper.updateFromDto(tagDTO, tagEntity); //수정된 테그 클릭수 업데이트
            tagRepository.save(tagEntity); //테그 조회수 업데이트

            return tagDTO.getTagId();

        }else{
            TagCreateDTO tagCreateDTO = new TagCreateDTO();//테그등록DTO

            tagCreateDTO.setTag(tag);//테그등록DTO에 테크명 set

            return tagService.create(tagCreateDTO);//테크명 신규등록

        }

    }


}
