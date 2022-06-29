package com.gemiso.zodiac.app.breakingNews.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.breakingNewsDetail.dto.BreakingNewsDtlCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BreakingNewsCreateDTO {

    //private Long breakingNewsId;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date brdcDtm;
    @NotNull
    private String titl;
    @NotNull
    private String breakingNewsDiv;
    @NotNull
    private String lnTypCd;
    private Date trnsfDtm;
    @NotNull
    private String trnsfStCd;
    private String inputrId;
    private List<BreakingNewsDtlCreateDTO> breakingNewsDtls = new ArrayList<>();
}
