package com.gemiso.zodiac.app.rundown.dto;

import com.gemiso.zodiac.app.articleCap.dto.ArticleCapSimpleDTO;
import com.gemiso.zodiac.app.rundownItem.dto.RundownItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RundownDTO {

    private Long rundownId;
    private Date rundownDt; // 런다운 일시
    private String rundownTime; // 회의시간 ( 오전 /오후 )
    private String rundownData; // 회의자료
    private String headline; // 헤드라인
    private String inputrId; // 작성자
    private String inputrNm;
    private String updtrId;
    private String updtrNm;
    private List<RundownItemDTO> rundownItems = new ArrayList<>();
}
