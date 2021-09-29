package com.gemiso.zodiac.app.articleMedia;

import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaCreateDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaUpdateDTO;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaCreateMapper;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaMapper;
import com.gemiso.zodiac.app.articleMedia.mapper.ArticleMediaUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ArticleMediaService {

    private final ArticleMediaRepository articleMediaRepository;

    private final ArticleMediaMapper articleMediaMapper;
    private final ArticleMediaCreateMapper articleMediaCreateMapper;
    private final ArticleMediaUpdateMapper articleMediaUpdateMapper;

    private final UserAuthService userAuthService;


    public List<ArticleMediaDTO> findAll(Date sdate, Date edate, String trnsfFileNm) {

        BooleanBuilder booleanBuilder = getSearch(sdate, edate, trnsfFileNm);

        List<ArticleMedia> articleMediaList = (List<ArticleMedia>) articleMediaRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "mediaOrd"));

        List<ArticleMediaDTO> articleMediaDTOList = articleMediaMapper.toDtoList(articleMediaList);

        return articleMediaDTOList;
    }

    public ArticleMediaDTO find(Long artclMediaId) {

        ArticleMedia articleMedia = articleMediaFindOrFail(artclMediaId);

        ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);

        return articleMediaDTO;

    }

    public Long create(ArticleMediaCreateDTO articleMediaCreateDTO) {

        String userId = userAuthService.authUser.getUserId();
        articleMediaCreateDTO.setInputrId(userId);

        ArticleMedia articleMedia = articleMediaCreateMapper.toEntity(articleMediaCreateDTO);

        articleMediaRepository.save(articleMedia);

        return articleMedia.getArtclMediaId();

    }

    public void update(ArticleMediaUpdateDTO articleMediaUpdateDTO, Long artclMediaId) {

        ArticleMedia articleMedia = articleMediaFindOrFail(artclMediaId);

        String userId = userAuthService.authUser.getUserId();
        articleMediaUpdateDTO.setUpdtrId(userId);

        articleMediaUpdateMapper.updateFromDto(articleMediaUpdateDTO, articleMedia);

        articleMediaRepository.save(articleMedia);

    }

    public void delete(Long artclMediaId) {

        ArticleMedia articleMedia = articleMediaFindOrFail(artclMediaId);

        ArticleMediaDTO articleMediaDTO = articleMediaMapper.toDto(articleMedia);

        String userId = userAuthService.authUser.getUserId();
        articleMediaDTO.setDelrId(userId);
        articleMediaDTO.setDelDtm(new Date());
        articleMediaDTO.setDelYn("Y");

        articleMediaMapper.updateFromDto(articleMediaDTO, articleMedia);

        articleMediaRepository.save(articleMedia);

    }

    public ArticleMedia articleMediaFindOrFail(Long artclMediaId) {

        Optional<ArticleMedia> articleMedia = articleMediaRepository.findByArticleMedia(artclMediaId);

        if (!articleMedia.isPresent()) {
            throw new ResourceNotFoundException("ArticleMediaId not found. ArticleMediaId : " + artclMediaId);
        }

        return articleMedia.get();
    }

    public BooleanBuilder getSearch(Date sdate, Date edate, String trnsfFileNm) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QArticleMedia qArticleMedia = QArticleMedia.articleMedia;

        booleanBuilder.and(qArticleMedia.delYn.eq("N"));

        if (!StringUtils.isEmpty(sdate) && !StringUtils.isEmpty(edate)) {
            booleanBuilder.and(qArticleMedia.inputDtm.between(sdate, edate));
        }
        if (!StringUtils.isEmpty(trnsfFileNm)) {
            booleanBuilder.and(qArticleMedia.trnsfFileNm.contains(trnsfFileNm));
        }

        return booleanBuilder;
    }


}
