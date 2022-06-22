package com.gemiso.zodiac.app.elasticsearch;

import com.gemiso.zodiac.app.elasticsearch.articleEntity.ElasticSearchArticle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface ElasticSearchArticleCustom {

    Page<ElasticSearchArticle> findByElasticSearchArticleList(Date sdate, Date edate, Date rcvDt, String rptrId, String inputrId, String brdcPgmId,
                                                              String artclDivCd, String artclTypCd, String searchDivCd, String searchWord,
                                                              List<String> apprvDivCdList, Long deptCd, String artclCateCd, String artclTypDtlCd,
                                                              String delYn, Long artclId, String copyYn, Long orgArtclId, Long cueId, Pageable pageable) throws Exception;

    Page<ElasticSearchArticle> findByElasticSearchArticleListCue(Date sdate, Date edate, String searchWord, Long cueId,
                                                                 String brdcPgmId, String artclTypCd,String artclTypDtlCd,
                                                                 String copyYn, Long deptCd, Long orgArtclId, Pageable pageable) throws Exception;
}
