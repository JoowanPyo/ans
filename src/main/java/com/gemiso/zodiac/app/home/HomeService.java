package com.gemiso.zodiac.app.home;

import com.gemiso.zodiac.app.article.Article;
import com.gemiso.zodiac.app.article.ArticleRepository;
import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.article.mapper.ArticleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class HomeService {

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    //private final UserAuthService userAuthService;

    public List<ArticleDTO> findAll(String userId){

        //String userId = userAuthService.authUser.getUserId();

        List<Article> articleList = articleRepository.findMyArticle(userId);

        List<ArticleDTO> articleDTOList = articleMapper.toDtoList(articleList);

        return articleDTOList;

    }
}
