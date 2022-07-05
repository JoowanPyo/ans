package com.gemiso.zodiac.app.lbox.mediaTransportDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ClipInfoDTO {

    private String video_id;
    private String filename;
    private Long filesize;
    private String formatted_filesize;
}
