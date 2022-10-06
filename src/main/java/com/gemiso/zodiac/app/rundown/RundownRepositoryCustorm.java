package com.gemiso.zodiac.app.rundown;

import java.util.Date;
import java.util.List;

public interface RundownRepositoryCustorm {

    List<Rundown> findRundowns(Date rundownDt, String rundownTime);
}
