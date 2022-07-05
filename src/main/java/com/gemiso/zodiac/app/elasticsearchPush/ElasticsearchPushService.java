package com.gemiso.zodiac.app.elasticsearchPush;

import com.gemiso.zodiac.app.article.dto.ArticleDTO;
import com.gemiso.zodiac.app.articleMedia.dto.ArticleMediaSimpleDTO;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetSimpleDTO;
import com.gemiso.zodiac.app.elasticsearch.ElasticSearchArticleRepository;
import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasricSearchArticleMedia;
import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasticSearchArticle;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
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
public class ElasticsearchPushService {

    private final ElasticSearchArticleRepository elasticSearchArticleRepository;

    private final DateChangeHelper dateChangeHelper;


    public void pushElastic(List<ArticleDTO> articleDTOList){


        if (CollectionUtils.isEmpty(articleDTOList) == false) {
            for (ArticleDTO articleDTO : articleDTOList) {

                Date getInputDtm = articleDTO.getInputDtm();

                String inputDtm = null;
                if (ObjectUtils.isEmpty(getInputDtm) == false) {

                    //Date형식을 String으로 파싱 "yyyy-MM-dd HH:mm:ss"
                    inputDtm = dateChangeHelper.dateToStringNormal(getInputDtm);
                }

                CueSheetSimpleDTO cueSheet = articleDTO.getCueSheet();

                String brdcPgmNm = null;
                Long cueId = null;
                Long subrmId = null;
                if (ObjectUtils.isEmpty(cueSheet) == false) {

                    brdcPgmNm = Optional.ofNullable(cueSheet.getBrdcPgmNm()).orElse(null);
                    cueId = Optional.ofNullable(cueSheet.getCueId()).orElse(null);
                    subrmId =Optional.ofNullable(cueSheet.getSubrmId()).orElse(null);
                }

                List<ElasricSearchArticleMedia> medias = new ArrayList<>();

                List<ArticleMediaSimpleDTO> articleMediaSimpleDTOList = articleDTO.getArticleMedia();

                for (ArticleMediaSimpleDTO articleMediaSimpleDTO : articleMediaSimpleDTOList){

                    ElasricSearchArticleMedia elasricSearchArticleMedia = new ElasricSearchArticleMedia();
                    elasricSearchArticleMedia.setArtclMediaId(articleMediaSimpleDTO.getArtclMediaId());
                    elasricSearchArticleMedia.setMediaDurtn(articleMediaSimpleDTO.getMediaDurtn());

                    medias.add(elasricSearchArticleMedia);
                }

                ElasticSearchArticle entity = ElasticSearchArticle.builder()
                        .ancMentCtt(articleDTO.getAncMentCtt())
                        .apprvDivCd(articleDTO.getApprvDivCd())
                        .apprvDivCdNm(articleDTO.getApprvDivCdNm())
                        .artclCateCd(articleDTO.getArtclCateCd())
                        .artclCateCdNm(articleDTO.getArtclCateCdNm())
                        .artclDivCd(articleDTO.getArtclDivCd())
                        .artclId(articleDTO.getArtclId())
                        .artclOrd(articleDTO.getArtclOrd())
                        .artclTitl(articleDTO.getArtclTitl())
                        .artclTitlEn(articleDTO.getArtclTitlEn())
                        .artclTypCd(articleDTO.getArtclTypCd())
                        .artclTypCdNm(articleDTO.getArtclTypCdNm())
                        .artclTypDtlCd(articleDTO.getArtclTypDtlCd())
                        .artclTypDtlCdNm(articleDTO.getArtclTypDtlCdNm())
                        .artclCtt(articleDTO.getArtclCtt())
                        .brdcPgmId(articleDTO.getBrdcPgmId())
                        .delYn(articleDTO.getDelYn())
                        .deptCd(articleDTO.getDeptCd())
                        .deptNm(articleDTO.getDeptNm())
                        .embgYn(articleDTO.getEmbgYn())
                        .inputDtm(inputDtm)
                        .inputrId(articleDTO.getInputrId())
                        .inputrNm(articleDTO.getInputrNm())
                        .lckYn(articleDTO.getLckYn())
                        .orgArtclId(articleDTO.getOrgArtclId())
                        .rptrId(articleDTO.getRptrId())
                        .rptrNm(articleDTO.getRptrNm())
                        .brdcPgmNm(brdcPgmNm)
                        .cueId(cueId)
                        .subrmId(subrmId)
                        .ancMentCttTime(articleDTO.getAncMentCttTime())
                        .artclCttTime(articleDTO.getArtclCttTime())
                        .artclExtTime(articleDTO.getArtclExtTime())
                        .tags(null)
                        .articleMedias(medias)
                        .build();

                elasticSearchArticleRepository.save(entity);

            }
        }

    }
}
