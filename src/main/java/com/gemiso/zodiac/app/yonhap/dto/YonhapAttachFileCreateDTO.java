package com.gemiso.zodiac.app.yonhap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class YonhapAttachFileCreateDTO {
    private Long file_id;
    private Long yh_artcl_id;
    private int file_ord;
    private String file_titl;
    private String mime_type;
    private String cap;
    private String yh_url;
}
