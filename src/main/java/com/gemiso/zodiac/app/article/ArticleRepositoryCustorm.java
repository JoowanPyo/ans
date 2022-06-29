package com.gemiso.zodiac.app.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface ArticleRepositoryCustorm {

    Page<Article> findByArticleList(Date sdate, Date edate, Date rcvDt, String rptrId, String inputrId, String brdcPgmId,
                        String artclDivCd, String artclTypCd, String searchDivCd, String searchWord,
                        List<String> apprvDivCdList, Long deptCd, String artclCateCd, String artclTypDtlCd,
                        String delYn, Long artclId, String copyYn, Long orgArtclId, Pageable pageable);

    Page<Article> findByArticleIssue(Date sdate, Date edate, String issuKwd, String artclDivCd, String artclTypCd,
                                     String artclTypDtlCd, String artclCateCd, Long deptCd, String inputrId,
                                     String brdcPgmId, Long orgArtclId, String delYn, String searchDivCd,
                                     String searchWord, List<String> apprvDivCdList, Pageable pageable);

    Page<Article> findByArticleCue(Date sdate, Date edate, String searchWord, Long cueId, String brdcPgmId, String artclTypCd,
                                   String artclTypDtlCd, String copyYn, Long deptCd, Long orgArtclId, Pageable pageable);


    Page<Article> findByArticleListElastic(Date sdate, Date edate,Pageable pageable);
}
