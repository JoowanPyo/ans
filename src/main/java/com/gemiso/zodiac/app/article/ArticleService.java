package com.gemiso.zodiac.app.article;

import com.gemiso.zodiac.app.ArticleOrder.dto.ArticleOrderSimpleDTO;
import com.gemiso.zodiac.app.ArticleOrder.mapper.ArticleOrderSimpleMapper;
import com.gemiso.zodiac.app.article.dto.ArticleCreateDTO;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.article.dto.ArticleUpdateDTO;
import com.gemiso.zodiac.app.article.mapper.ArticleCreateMapper;
import com.gemiso.zodiac.app.article.mapper.ArticleMapper;
import com.gemiso.zodiac.app.article.mapper.ArticleUpdateMapper;
import com.gemiso.zodiac.app.articleCap.ArticleCap;
import com.gemiso.zodiac.app.articleCap.ArticleCapRepository;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapDTO;
import com.gemiso.zodiac.app.articleCap.dto.ArticleCapSimpleDTO;
import com.gemiso.zodiac.app.articleCap.mapper.ArticleCapMapper;
import com.gemiso.zodiac.app.articleCap.mapper.ArticleCapSimpleMapper;
import com.gemiso.zodiac.app.articleHist.ArticleHist;
import com.gemiso.zodiac.app.articleHist.ArticleHistRepository;
import com.gemiso.zodiac.app.articleHist.dto.ArticleHistDTO;
import com.gemiso.zodiac.app.articleHist.dto.ArticleHistSimpleDTO;
import com.gemiso.zodiac.app.articleHist.mapper.ArticleHistMapper;
import com.gemiso.zodiac.app.articleHist.mapper.ArticleHistSimpleMapper;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaSimpleDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaSimpleMapper;
import com.gemiso.zodiac.app.user.QUser;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleCapRepository articleCapRepository;
    private final ArticleHistRepository articleHistRepository;

    private final ArticleMapper articleMapper;
    private final ArticleCreateMapper articleCreateMapper;
    private final ArticleCapMapper articleCapMapper;
    private final ArticleCapSimpleMapper articleCapSimpleMapper;
    private final ArticleHistSimpleMapper articleHistSimpleMapper;
    private final ArticleMediaSimpleMapper articleMediaSimpleMapper;
    private final ArticleOrderSimpleMapper articleOrderSimpleMapper;
    private final ArticleUpdateMapper articleUpdateMapper;

    private final UserAuthService userAuthService;


    public List<ArticleDTO> findAll(Date sdate, Date edate, Date rcvDt, String rptrId, Long brdcPgmId,
                                    String artclDivCd, String artclTypCd, String searchDivCd, String searchWord){

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, rcvDt, rptrId, brdcPgmId, artclDivCd, artclTypCd, searchDivCd, searchWord);

        List<Article> articleList = (List<Article>) articleRepository.findAll(booleanBuilder);

        List<ArticleDTO> articleDTOList = articleMapper.toDtoList(articleList);

        return articleDTOList;
    }

    public ArticleDTO find(Long artclId){

        Article article = articleFindOrFail(artclId);

        ArticleDTO articleDTO = articleMapper.toDto(article);

        //기사정보를 불러와 맵퍼스트럭트 사용시 스텍오버플로우에러[기사에 포함된 리스트에 기사정보포함되어이써 문제 발생] 때문에 따로 DTO변환.
        List<ArticleHistSimpleDTO> articleHistDTOList = articleHistSimpleMapper.toDtoList(article.getArticleHist());//기사이력정보 DTO변환
        List<ArticleCapSimpleDTO> articleCapDTOList = articleCapSimpleMapper.toDtoList(article.getArticleCap()); //기사자막정보 DTO변환
        List<ArticleMediaSimpleDTO> articleMediaSimpleDTOList = articleMediaSimpleMapper.toDtoList(article.getArticleMedia()); //기사미디어정보 DTO변환
        List<ArticleOrderSimpleDTO> articleOrderSimpleDTOList = articleOrderSimpleMapper.toDtoList(article.getArticleOrder()); //기사의뢰정보 DTO변환
        
        //기사이력, 자막, 미디어, 의뢰 정보 set
        articleDTO.setArticleHistDTO(articleHistDTOList);
        articleDTO.setArticleCapDTO(articleCapDTOList);
        articleDTO.setArticleMediaDTO(articleMediaSimpleDTOList);
        articleDTO.setArticleOrderDTO(articleOrderSimpleDTOList);


        return articleDTO;

    }


    public Long create(ArticleCreateDTO articleCreateDTO) { // 기사등록[기사 이력, 자막]

        String userId = userAuthService.authUser.getUserId();
        UserSimpleDTO userSimpleDTO = UserSimpleDTO.builder().userId(userId).build();
        articleCreateDTO.setInputr(userSimpleDTO); //등록자 아이디 추가.

        Article article = articleCreateMapper.toEntity(articleCreateDTO);
        articleRepository.save(article);

        ArticleDTO articleDTO = articleMapper.toDto(article);


        //기사 자막 create
        ArrayList<ArticleCapDTO> articleCapDTOS = new ArrayList<ArticleCapDTO>(articleCreateDTO.getArticleCap());

        if (!ObjectUtils.isEmpty(articleCapDTOS)) {
            for (ArticleCapDTO articleCapDTO : articleCapDTOS) {

                articleCapDTO.setArticle(articleDTO);
                ArticleCap articleCap = articleCapMapper.toEntity(articleCapDTO);
                articleCapRepository.save(articleCap);
            }
        }
        //기사 이력 create
        createArticleHist(article ,articleDTO);

        return article.getArtclId();
    }

    public void update(ArticleUpdateDTO articleUpdateDTO, Long artclId){

        Article article = articleFindOrFail(artclId);

        String userId = userAuthService.authUser.getUserId();
        UserSimpleDTO userSimpleDTO = UserSimpleDTO.builder().userId(userId).build();

        articleUpdateDTO.setUpdtr(userSimpleDTO);

        articleUpdateMapper.updateFromDto(articleUpdateDTO, article);

        articleRepository.save(article);

        //기사이력 등록.
        updateArticleHist(article ,articleUpdateDTO);

    }

    public void delete(Long artclId){

        Article article = articleFindOrFail(artclId);

        ArticleDTO articleDTO = articleMapper.toDto(article);

        //삭제정보 등록
        articleDTO.setDelDtm(new Date());
        String userId = userAuthService.authUser.getUserId();
        articleDTO.setDelrId(userId);
        articleDTO.setDelYn("Y");

        articleMapper.updateFromDto(articleDTO, article);

        articleRepository.save(article);

    }

    public Article articleFindOrFail(Long artclId){

        Optional<Article> article = articleRepository.findArticle(artclId);

        if (!article.isPresent()){
            throw new ResourceNotFoundException("ArticleId not found. ArticleID : " + artclId);
        }

        return article.get();

    }

    public BooleanBuilder getSearch(Date sdate, Date edate, Date rcvDt, String rptrId, Long brdcPgmId,
                                    String artclDivCd, String artclTypCd, String searchDivCd, String searchWord){

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticle qArticle = QArticle.article;
        QUser qUser = QUser.user;

        if (!StringUtils.isEmpty(sdate) && !StringUtils.isEmpty(edate)){
            booleanBuilder.and(qArticle.inputDtm.between(sdate, edate)
                    .or(qArticle.embgDtm.between(sdate, edate)
                    .or(qArticle.brdcSchdDtm.between(sdate, edate))));
        }
        if (!StringUtils.isEmpty(rcvDt)){

            //rcvDt(수신일자)검색을 위해 +1 days
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(rcvDt);
            calendar.add(Calendar.DATE, 1);
            Date rcvDtTomerrow = calendar.getTime();

            booleanBuilder.and(qArticle.inputDtm.between(rcvDt, rcvDtTomerrow));
        }
        if (!StringUtils.isEmpty(rptrId)){
            booleanBuilder.and(qArticle.rptrId.eq(rptrId));
        }
        if (!StringUtils.isEmpty(brdcPgmId)){
            booleanBuilder.and(qArticle.brdcPgmId.eq(brdcPgmId));
        }
        if (!StringUtils.isEmpty(artclDivCd)){
            booleanBuilder.and(qArticle.artclDivCd.eq(artclDivCd));
        }
        if (!StringUtils.isEmpty(artclTypCd)){
            booleanBuilder.and(qArticle.artclTypCd.eq(artclTypCd));
        }
        if (!StringUtils.isEmpty(searchWord)){
            if (searchDivCd.equals("01")){
                booleanBuilder.and(qArticle.artclTitl.contains(searchWord));
            }
            if (searchDivCd.equals("02")){
                booleanBuilder.and(qArticle.rptrId.eq(String.valueOf(qUser.userNm.contains(searchWord))));
            }

        }


        return booleanBuilder;
    }

    public void createArticleHist(Article article, ArticleDTO articleDTO){

        ArticleHist articleHist = ArticleHist.builder()
                .article(article)
                .chDivCd(articleDTO.getChDivCd())
                .artclTitl(articleDTO.getArtclTitl())
                .artclTitlEn(articleDTO.getArtclTitlEn())
                .artclCtt(articleDTO.getArtclCtt())
                .ancMentCtt(articleDTO.getAncMentCtt())
                .artclOrd(articleDTO.getArtclOrd())
                .orgArtclId(articleDTO.getOrgArtclId())
                .inputDtm(new Date())
                .ver(0) //수정! 버전이 설정?
                .build();

        articleHistRepository.save(articleHist);
    }
    public void updateArticleHist(Article article, ArticleUpdateDTO articleUpdateDTO){

        ArticleHist articleHist = ArticleHist.builder()
                .article(article)
                .chDivCd(articleUpdateDTO.getChDivCd())
                .artclTitl(articleUpdateDTO.getArtclTitl())
                .artclTitlEn(articleUpdateDTO.getArtclTitlEn())
                .artclCtt(articleUpdateDTO.getArtclCtt())
                .ancMentCtt(articleUpdateDTO.getAncMentCtt())
                .artclOrd(articleUpdateDTO.getArtclOrd())
                .orgArtclId(articleUpdateDTO.getOrgArtclId())
                .inputDtm(new Date())
                .ver(0) //수정! 버전이 설정?
                .build();

        articleHistRepository.save(articleHist);

    }

}
