package com.gemiso.zodiac.app.lbox.mediaTransportDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponsMediaTransportDTO {

    private Integer status;
    private String success;
    private MediaTransportDataDTO data;
}
