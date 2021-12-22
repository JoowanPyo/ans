package com.gemiso.zodiac.app.articleCapHist;

import com.gemiso.zodiac.app.articleCapHist.dto.ArticleCapHistDTO;
import com.gemiso.zodiac.app.articleCapHist.mapper.ArticleCapHistMapper;
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
public class ArticleCapHistService {

    private final ArticleCapHistRepository articleCapHistRepository;

    private final ArticleCapHistMapper articleCapHistMapper;


    public List<ArticleCapHistDTO> findAll(Long artclHistId){

        BooleanBuilder booleanBuilder = getSearch(artclHistId);

        List<ArticleCapHist> articleCapHistList = (List<ArticleCapHist>) articleCapHistRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "lnNo"));

        List<ArticleCapHistDTO> articleCapHistDTOList = articleCapHistMapper.toDtoList(articleCapHistList);

        return articleCapHistDTOList;
    }

    public BooleanBuilder getSearch(Long artclHistId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticleCapHist qArticleCapHist = QArticleCapHist.articleCapHist;

        if (ObjectUtils.isEmpty(artclHistId)){
            booleanBuilder.and(qArticleCapHist.articleHist.artclHistId.eq(artclHistId));
        }

        return booleanBuilder;
    }

    public ArticleCapHistDTO find(Long artclCapHistId){

        ArticleCapHist articleCapHist = findArticleCapHist(artclCapHistId);

        ArticleCapHistDTO articleCapHistDTO = articleCapHistMapper.toDto(articleCapHist);

        return articleCapHistDTO;
    }

    public ArticleCapHist findArticleCapHist(Long artclCapHistId){

        Optional<ArticleCapHist> articleCapHist = articleCapHistRepository.findArticleCapHist(artclCapHistId);

        if (articleCapHist.isPresent() == false){
            throw new ResourceNotFoundException("기사자막 이력을 찾을수 없습니다. 기사자막 이력 아이디 : " +artclCapHistId);
        }

        return articleCapHist.get();
    }
}
