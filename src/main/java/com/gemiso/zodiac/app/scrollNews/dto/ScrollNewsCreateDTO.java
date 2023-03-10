package com.gemiso.zodiac.app.scrollNews.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gemiso.zodiac.app.scrollNewsDetail.dto.ScrollNewsDetailCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScrollNewsCreateDTO {

    //private Long scrlNewsId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private Date brdcDtm;
    private String scrlDivCd;
    //private String scrlDivCdNm;
    private String scrlFrmCd;
    //private String scrlFrmCdNm;
    private String titl;
    private String fileNm;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date trnsfDtm;
    private String trnsfStCd;
    //private Date delDtm;
    private String delYn;
    private String inputrId;
    //private String inputrNm;
    //private String updtrId;
    //private String updtrNm;
    //private String delrId;
    //private String delrNm;
    private String cgText;
    private List<ScrollNewsDetailCreateDTO> scrollNewsDetails = new ArrayList<>();
}
