package com.gemiso.zodiac.app.rundownItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface RundownItemRepositoryCustorm {

    List<RundownItem> findAllItems(Long rundownId);
    Page<RundownItem> findMyRundown(Date sdate, Date edate, String rptrId, Pageable pageable);

}
