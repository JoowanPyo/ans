package com.gemiso.zodiac.app.articleCap;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapCreateDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapUpdateDTO;
import com.gemiso.zodiac.app.articleCap.mapper.ArticleCapCreateMapper;
import com.gemiso.zodiac.app.articleCap.mapper.ArticleCapMapper;
import com.gemiso.zodiac.app.articleCap.mapper.ArticleCapUpdateMapper;
import com.gemiso.zodiac.app.capTemplate.CapTemplate;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleCapService {

    private final ArticleCapRepository articleCapRepository;

    private final ArticleCapMapper articleCapMapper;
    private final ArticleCapCreateMapper articleCapCreateMapper;
    private final ArticleCapUpdateMapper articleCapUpdateMapper;


    public List<ArticleCapDTO> findAll(Long artclId){

        BooleanBuilder booleanBuilder = getSearch(artclId);

        List<ArticleCap> articleCapList = (List<ArticleCap>) articleCapRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "lnNo"));

        List<ArticleCapDTO> articleCapDTOList = articleCapMapper.toDtoList(articleCapList);

        return articleCapDTOList;
    }

    public ArticleCapDTO find(Long artclCapId) {

        ArticleCap articleCap = articleCapFindOrFail(artclCapId);

        ArticleCapDTO articleCapDTO = articleCapMapper.toDto(articleCap);

        //기사정보 추가.
        //ArticleSimpleDTO articleSimpleDTO = ArticleSimpleDTO.builder().artclId(articleCap.getArticle().getArtclId()).build();

        //articleCapDTO.setArticle(articleSimpleDTO);

        return articleCapDTO;
    }

    public Long create(ArticleCapCreateDTO articleCapCreateDTO) {

        ArticleCap articleCap = articleCapCreateMapper.toEntity(articleCapCreateDTO);

        articleCap = relationshipCreate(articleCap, articleCapCreateDTO); //의존관계 Entity id를 DTO로 받아서 Entity로 변환하여 추가

        articleCapRepository.save(articleCap);

        return articleCap.getArtclCapId();
    }

    public void update(ArticleCapUpdateDTO articleCapUpdateDTO, Long articleCapId){

        ArticleCap articleCap = articleCapFindOrFail(articleCapId);

        articleCap = relationshipUpdate( articleCap,  articleCapUpdateDTO);

        articleCapUpdateMapper.updateFromDto(articleCapUpdateDTO, articleCap);

        articleCapRepository.save(articleCap);
    }

    public void delete(Long articleCapId){


    }

    //기사자막에 들어오는 기사, 템플릿, 방송아이콘 set
    public ArticleCap relationshipCreate(ArticleCap articleCap, ArticleCapCreateDTO articleCapCreateDTO) {

        Long articleId = articleCapCreateDTO.getArticleId(); //기사 아이디 get
        Long capTmpltId = articleCapCreateDTO.getCapTmpltId(); //템플릿아이디 get
        String symbolId = articleCapCreateDTO.getSymbolId(); // 방송아이콘 아이디 get


        if (ObjectUtils.isEmpty(articleId) == false) { //기사아이디가 있으면 엔엔티 빌드후 set
            Article article = Article.builder().artclId(articleId).build();
            articleCap.setArticle(article);
        }
        if (ObjectUtils.isEmpty(capTmpltId) == false){ //템플릿아이가 있으면 엔엔티 빌드후 set
            CapTemplate capTemplate = CapTemplate.builder().capTmpltId(capTmpltId).build();
            articleCap.setCapTemplate(capTemplate);
        }
        if (StringUtils.isEmpty(symbolId) == false){ //방송아이콘 아이디가 있으면 엔엔티 빌드후 set
            Symbol symbol = Symbol.builder().symbolId(symbolId).build();
            articleCap.setSymbol(symbol);
        }


        return articleCap;
    }

    public ArticleCap relationshipUpdate(ArticleCap articleCap, ArticleCapUpdateDTO articleCapUpdateDTO){

        //Long articleId = articleCapCreateDTO.getArticleId(); //기사 아이디 get
        Long capTmpltId = articleCapUpdateDTO.getCapTmpltId(); //템플릿아이디 get
        String symbolId = articleCapUpdateDTO.getSymbolId(); // 방송아이콘 아이디 get


        /*if (ObjectUtils.isEmpty(articleId) == false) { //기사아이디가 있으면 엔엔티 빌드후 set
            Article article = Article.builder().artclId(articleId).build();
            articleCap.setArticle(article);
        }*/
        if (ObjectUtils.isEmpty(capTmpltId) == false){ //템플릿아이가 있으면 엔엔티 빌드후 set
            CapTemplate capTemplate = CapTemplate.builder().capTmpltId(capTmpltId).build();
            articleCap.setCapTemplate(capTemplate);
        }
        else
        {
            articleCap.setCapTemplate(null);//null이나 공백으로 들어올 경우 데이터 삭제
        }
        if (StringUtils.isEmpty(symbolId) == false){ //방송아이콘 아이디가 있으면 엔엔티 빌드후 set
            Symbol symbol = Symbol.builder().symbolId(symbolId).build();
            articleCap.setSymbol(symbol);
        }
        else
        {
            articleCap.setSymbol(null);//null이나 공백으로 들어올 경우 데이터 삭제
        }

        return articleCap;
    }

    public ArticleCap articleCapFindOrFail(Long artclCapId) {

        return articleCapRepository.findById(artclCapId)
                .orElseThrow(() -> new ResourceNotFoundException("ArticleCapId not found. ArticleCapId : " + artclCapId));

    }

    public BooleanBuilder getSearch(Long artclId){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticleCap qArticleCap = QArticleCap.articleCap;

        if (!StringUtils.isEmpty(artclId)){
            booleanBuilder.and(qArticleCap.article.artclId.eq(artclId));
        }

        return booleanBuilder;
    }
}
