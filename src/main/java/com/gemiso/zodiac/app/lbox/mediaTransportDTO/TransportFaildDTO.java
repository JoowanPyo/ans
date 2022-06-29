package com.gemiso.zodiac.app.lbox.mediaTransportDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportFaildDTO {

    private String message;
    private String subrmNm;
    private String destination;
}
