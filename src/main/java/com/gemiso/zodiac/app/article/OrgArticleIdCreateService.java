package com.gemiso.zodiac.app.article;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrgArticleIdCreateService {

    private final ArticleRepository articleRepository;

    //기사 오리지널 아이디 가져오기
    public Long orgArticleIdCreate(){

        Long orgArticleId = articleRepository.findOrgArticleId();

        return orgArticleId;
    }
}
