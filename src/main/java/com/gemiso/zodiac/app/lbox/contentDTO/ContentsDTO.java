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
public class ContentsDTO {

    private Long id;
    private Long meta_set_id;
    private String media_type;
    private String title;
    private Integer status;
    private String review_status;
    private String delete_status;
    private String archive_status;
    private Long registerer_id;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;
    private String expire_on;
    private Long view_count;
    private Date locked_at;
    private Boolean in_recycle_bin;
    private Long user_id;
    private String user_name;
    private String user_account_id;
    private Long media_id;
    private String video_time;
    private String video_time_short;
    private RegistererDTO registerer;
    private MediaInfoDTO media_info;
    private ProxyDTO proxy;
    private VideoIdDTO video_id;
}
