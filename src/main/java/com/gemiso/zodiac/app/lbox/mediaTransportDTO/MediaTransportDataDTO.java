package com.gemiso.zodiac.app.lbox.mediaTransportDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaTransportDataDTO {

    private ClipInfoDTO clip_info;
    private List<TasksDTO> tasks;
}
