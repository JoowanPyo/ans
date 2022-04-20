package com.gemiso.zodiac.app.lbox.contentDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxyDTO {

    private Long id;
    private Long storage_id;
    private Long content_id;
    private String media_profile;
    private String path;
    private Long filesize;
    private Boolean is_base;
    private Integer status;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;
    private Date expire_on;

}
