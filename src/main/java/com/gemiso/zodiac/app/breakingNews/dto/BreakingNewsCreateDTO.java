package com.gemiso.zodiac.app.breakingNews.dto;

import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlCreateDTO;
import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreakingNewsCreateDTO {

    //private Long breakingNewsId;
    private Date brdcDtm;
    private String titl;
    private String breakingNewsDiv;
    //private String breakingNewsDivNm;
    private String lnTypCd;
    //private String lnTypCdNm;
    private Date trnsfDtm;
    private String trnsfStCd;
    //private String trnsfStCdNm;
    //private Date inputDtm;
    //private Date updtDtm;
    //private Date delDtm;
    //private String delYn;
    private String inputrId;
    //private String inputrNm;
    //private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    private List<BreakingNewsDtlCreateDTO> breakingNewsDtls = new ArrayList<>();
}
