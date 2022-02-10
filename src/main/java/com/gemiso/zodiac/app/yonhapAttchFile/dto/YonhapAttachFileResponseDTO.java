package com.gemiso.zodiac.app.yonhapAttchFile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapAttachFileResponseDTO {

    private String cap;
    private String expl;
    private int file_ord;
    private String file_titl;
    private String file_typ_cd;
    private String mime_typ;
    private String yh_artcl_id;
    private String yh_url;
    private String file_nm;

    private long file_size;
    private String file_id;
    private String org_file_nm;
    private String file_loc;
    private String file_ext;

    private String file_move_yn;
}
