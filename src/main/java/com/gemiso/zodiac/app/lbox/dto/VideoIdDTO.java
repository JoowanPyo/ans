package com.gemiso.zodiac.app.lbox.dto;

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
public class VideoIdDTO {

    private Long id;
    private Long content_id;
    private String video_id;
    private Date created_at;
    private Date updated_at;
}
