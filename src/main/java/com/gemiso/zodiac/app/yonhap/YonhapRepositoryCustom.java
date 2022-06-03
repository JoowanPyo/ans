package com.gemiso.zodiac.app.yonhap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface YonhapRepositoryCustom {

    Page<Yonhap> findByYonhapList(Date sdate, Date edate, String artclCateCd, /*List<String> region_cds,*/
                                  String search_word, String svcTyp, Pageable pageable);
}
