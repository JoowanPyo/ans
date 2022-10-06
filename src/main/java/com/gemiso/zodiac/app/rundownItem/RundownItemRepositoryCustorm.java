package com.gemiso.zodiac.app.rundownItem;

import java.util.List;

public interface RundownItemRepositoryCustorm {

    List<RundownItem> findAllItems(Long rundownId);
}
