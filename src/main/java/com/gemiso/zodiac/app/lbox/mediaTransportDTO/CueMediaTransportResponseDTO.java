package com.gemiso.zodiac.app.lbox.mediaTransportDTO;

import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CueMediaTransportResponseDTO {

    private CueSheetMediaDTO cueSheetMediaDTO;
    private List<TransportFaildDTO> transportFaild;
}
