package com.gemiso.zodiac.app.symbol.dto;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SymbolCreateDTO {

    //private Long symbolId;
    private String symbolNm;
    private String expl;
    //private String delYn;
    private String useYn;
    private String cap_tmplt_yn;
    private Date inputDtm;
    //private Date updtDtm;
    //private Date delDtm;
    private String inputrId;
    //private String inputrNm;
    //private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    private String typCd;
    //private String typCdNm;
    private AttachFileDTO attachFile;
}
