package com.gemiso.zodiac.app.yonhapAttchFile.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapAttachFileCreateDTO {
/*    private Long file_id;
    private Long yh_artcl_id;
    private int file_ord;
    private String file_titl;
    private String mime_type;
    private String cap;
    private String yh_url;*/


    // "등록된 파일 아이디"
    private Long update_file_id;
    // "파일아이디"
    private Long file_id;
    // "연합 기사 아이디"
    private Long yh_artcl_id;
    // "파일 순번"
    private int file_ord;
    // "파일 유형 코드"
    private String file_typ_cd;
    // "마임 유형"
    private String mime_typ;
    // "연합 url"
    private String yh_url;
    // "파일 제목"
    private String file_titl;
    // "설명"
    private String expl;
    // "자막"
    private String cap;
    // "파일위치"
    private String file_loc;
    // "파일명"
    private String file_nm;
    // "파일크기"
    private int file_size;

}
