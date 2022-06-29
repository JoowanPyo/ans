package com.gemiso.zodiac.app.appInterface.mediaTransferDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MediaTransferDTO {

    private Integer contentId;
    private Integer trnasfVal;
    private String trnsfStCd;
}
