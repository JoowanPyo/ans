package com.gemiso.zodiac.app.breakingNews.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.breakingNewsDetail.BreakingNewsDtl;
import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Schema(description = "속보뉴스 articleDTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreakingNewsDTO {

    private Long breakingNewsId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date brdcDtm;
    private String titl;
    private String breakingNewsDiv;
    private String breakingNewsDivNm;
    private String lnTypCd;
    private String lnTypCdNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date trnsfDtm;
    private String trnsfStCd;
    private String trnsfStCdNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updtDtm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date delDtm;
    private String delYn;
    private String inputrId;
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private String delrId;
    private String delrNm;
    private List<BreakingNewsDtlDTO> breakingNewsDtls = new ArrayList<>();
}
