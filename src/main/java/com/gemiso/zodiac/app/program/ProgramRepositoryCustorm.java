package com.gemiso.zodiac.app.program;

import java.util.List;

public interface ProgramRepositoryCustorm {

    List<Program> findByProgram(String brdcPgmNm, String useYn);
}
