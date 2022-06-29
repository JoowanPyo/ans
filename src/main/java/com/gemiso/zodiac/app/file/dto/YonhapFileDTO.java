package com.gemiso.zodiac.app.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YonhapFileDTO {

/*    private Long fileId;
    private String fileDivCd;
    private String fileNm;
    private String fileExpl;
    private String fileLoc;
    private int fileSize;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date fileUpldDtm;
    private String delYn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date inputDtm;
    private String orgFileNm;
    private String inputrId;
    private String inputrNm;*/

    // "파일사이즈"
    private int file_size;
    //"파일아이디"
    private String file_id;
    //"파일구분코드"
    private String file_div_cd;
    //"파일명"
    private String file_nm;
    //"파일설명"
    private String file_expl;
    //"파일위치"
    private String file_loc;
    //"파일등록일시"
    private String file_upld_dtm;
    //"파일등록자아이디"
    private String file_upldr_id;
    //"원본파일명"
    private String org_file_nm;
    //"파일확장자"
    private String file_ext;
}
