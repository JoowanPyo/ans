package com.gemiso.zodiac.app.appInterface.codeDTO;

import com.gemiso.zodiac.app.appInterface.cueFindAllDTO.TakerCueSheetDataDTO;
import com.gemiso.zodiac.app.appInterface.cueFindAllDTO.TakerCueSheetResultDTO;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCodeXML {

    @XmlElement(name = "result")
    private TakerCodeResultDTO result;

    @XmlElement(name = "data")
    TakerCodeDataDTO data;
}
