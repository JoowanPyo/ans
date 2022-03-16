package com.gemiso.zodiac.app.appInterface.takerCueRefresh;

import com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO.TakerCueSheetDataDTO;
import com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO.TakerCueSheetResultDTO;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueRefreshXML {

    @XmlElement(name = "result")
    private TakerCueSheetResultDTO result;

    @XmlElement(name = "data")
    private TakerCueRefreshDataDTO data;
}
