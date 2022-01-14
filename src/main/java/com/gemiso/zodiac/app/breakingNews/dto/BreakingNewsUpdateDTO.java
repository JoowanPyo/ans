package com.gemiso.zodiac.app.breakingNews.dto;

import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlCreateDTO;
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
public class BreakingNewsUpdateDTO {

    private Long breakingNewsId;
    private Date brdcDtm;
    private String titl;
    private String breakingNewsDiv;
    private String lnTypCd;
    private Date trnsfDtm;
    private String trnsfStCd;
    private String updtrId;
    private List<BreakingNewsDtlCreateDTO> breakingNewsDtls = new ArrayList<>();
}
