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
public class MediaInfoDTO {

    private Long id;
    private Long content_id;
    private Long media_id;
    private String org_filename;
    private String video_is_drop;
    private String video_par;
    private String video_dar;
    private String video_codec;
    private Integer video_bitrate;
    private String video_framerate;
    private Integer video_frame_count;
    private Integer audio_bitrate;
    private Integer audio_sample_rate;
    private String audio_codec;
    private Integer audio_channel_count;
    private String duration;
    private String timecode;
    private Integer width;
    private Integer height;
    private String dimensions;
    private String format;
    private String raw_value;
    private Date created_at;
    private Date updated_at;


}
