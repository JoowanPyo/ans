package com.gemiso.zodiac.app.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface ElasticSearchArticleCustom {

    Page<ElasticSearchArticle> findByElasticSearchArticleList(Date sdate, Date edate, Date rcvDt, String rptrId, String inputrId, String brdcPgmId,
                                                              String artclDivCd, String artclTypCd, String searchDivCd, String searchWord,
                                                              List<String> apprvDivCdList, Integer deptCd, String artclCateCd, String artclTypDtlCd,
                                                              String delYn, Long artclId, String copyYn, Long orgArtclId, Pageable pageable);
}
