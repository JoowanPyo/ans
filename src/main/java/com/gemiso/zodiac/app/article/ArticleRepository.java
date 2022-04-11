package com.gemiso.zodiac.app.article;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, QuerydslPredicateExecutor<Article> {

    @Query("select a from Article a " +
            "left outer join Issue f on f.issuId = a.issue.issuId " +
            "left outer join ArticleCap  c on c.article.artclId = a.artclId " +
            "left outer join AnchorCap h on h.article.artclId = a.artclId " +
            "left outer join ArticleMedia d on d.article.artclId = a.artclId " +
            "left outer join ArticleOrder e on e.article.artclId = a.artclId " +
            "left outer join ArticleTag b on b.article.artclId = a.artclId " +
            "where a.artclId =:articleId and a.delYn = 'N'")
    Optional<Article> findArticle(@Param("articleId")Long articleId);


    @Query("select a from Article a where a.inputrId = :userId and a.delYn = 'N'")
    List<Article> findMyArticle(@Param("userId")String userId);


/*
    @Query("insert \n" +
            "    into\n" +
            "        tb_artcl\n" +
            "        (input_dtm, updt_dtm, anc_ment_ctt, anc_ment_ctt_time, apprv_div_cd, apprv_dtm, apprvr_id, \n" +
            "\t\tartcl_cate_cd, artcl_ctt, artcl_ctt_time, artcl_div_cd, artcl_ext_time, artcl_fld_cd, artcl_frm_cd, \n" +
            "\t\tartcl_kind_cd, artcl_ord, artcl_reqd_sec, artcl_reqd_sec_div_yn, artcl_titl, artcl_titl_en, artcl_typ_cd, \n" +
            "\t\tartcl_typ_dtl_cd, brdc_cnt, brdc_pgm_id, brdc_schd_dtm, ch_div_cd, del_dtm, del_yn, delr_id, dept_cd, \n" +
            "\t\tdevice_cd, embg_dtm, embg_yn, frnoti_yn, inputr_id, issu_id, lck_dtm, lck_yn, lckr_id, noti_yn, \n" +
            "\t\torg_artcl_id, parent_artlc_id, prd_div_cd, reg_app_typ, rptr_id, updtr_id, urg_yn, user_grp_id, video_time) \n" +
            "    values\n" +
            "        (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, \n" +
            "\t\t?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ")
    void saveArticle(@Param("article")Article article);*/
}
