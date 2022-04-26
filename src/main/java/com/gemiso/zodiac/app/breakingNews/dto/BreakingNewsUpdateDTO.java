package com.gemiso.zodiac.app.breakingNews.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date brdcDtm;
    private String titl;
    private String breakingNewsDiv;
    private String lnTypCd;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date trnsfDtm;
    private String trnsfStCd;
    private String updtrId;
    private List<BreakingNewsDtlCreateDTO> breakingNewsDtls = new ArrayList<>();
}
