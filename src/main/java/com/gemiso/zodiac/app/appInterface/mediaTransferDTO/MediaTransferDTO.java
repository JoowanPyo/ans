package com.gemiso.zodiac.app.appInterface.mediaTransferDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MediaTransferDTO {

    private Integer contentId;
    private Integer trnasfVal;
    private String trnsfStCd;
}
