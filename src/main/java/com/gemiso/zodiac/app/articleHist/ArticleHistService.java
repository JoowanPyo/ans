package com.gemiso.zodiac.app.articleHist;

import com.gemiso.zodiac.app.articleHist.dto.ArticleHistDTO;
import com.gemiso.zodiac.app.articleHist.mapper.ArticleHistMapper;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleHistService {

    private final ArticleHistRepository articleHistRepository;

    private final ArticleHistMapper articleHistMapper;

    //private final UserAuthService userAuthService;


    public List<ArticleHistDTO> findAll(Long artclId, Long orgArtclId, String searchWord){

        BooleanBuilder booleanBuilder = getSearch(artclId, orgArtclId, searchWord);

        List<ArticleHist> articleHistList = (List<ArticleHist>) articleHistRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        List<ArticleHistDTO> articleHistDTOList = articleHistMapper.toDtoList(articleHistList);

        return articleHistDTOList;
    }

    public ArticleHistDTO find(Long artclHistId){

        ArticleHist articleHist = articleHistFindOrFail(artclHistId);

        ArticleHistDTO articleHistDTO = articleHistMapper.toDto(articleHist);

        return articleHistDTO;
    }

    private ArticleHist articleHistFindOrFail(Long artclHistId){

        Optional<ArticleHist> articleHist = articleHistRepository.findByArticleHist(artclHistId);

        if (!articleHist.isPresent()){
            throw new ResourceNotFoundException("기사 이력을 찾을 수 없습니다. 기사이력 아이디 : " + artclHistId);
        }

        return articleHist.get();

    }

    public BooleanBuilder getSearch(Long artclId, Long orgArtclId, String searchWord){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticleHist qArticleHist = QArticleHist.articleHist;

        if (ObjectUtils.isEmpty(artclId) == false){
            booleanBuilder.and(qArticleHist.article.artclId.eq(artclId));
        }
        if (ObjectUtils.isEmpty(orgArtclId) == false){
            booleanBuilder.and(qArticleHist.orgArtclId.eq(orgArtclId));
        }
        if (searchWord != null && searchWord.trim().isEmpty() == false){
            booleanBuilder.and(qArticleHist.artclTitl.contains(searchWord).or(qArticleHist.artclTitlEn.contains(searchWord)));
        }

        return booleanBuilder;
    }
}
