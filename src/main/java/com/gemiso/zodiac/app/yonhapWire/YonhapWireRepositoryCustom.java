package com.gemiso.zodiac.app.yonhapWire;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface YonhapWireRepositoryCustom {

    Page<YonhapWire> findYonhapWireList(Date sdate, Date edate, String agcyCd, String agcyNm, String source,
                                      String svcTyp, String searchWord, List<String> imprtList, Pageable pageable, String mediaNo);
}
