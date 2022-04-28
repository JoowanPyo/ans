package com.gemiso.zodiac.app.lbox.mediaTransportDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ClipInfoDTO {

    private String video_id;
    private String filename;
    private Integer filesize;
    private String formatted_filesize;
}
