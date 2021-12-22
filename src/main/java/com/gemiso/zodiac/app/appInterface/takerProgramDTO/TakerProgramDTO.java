package com.gemiso.zodiac.app.appInterface.takerProgramDTO;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerProgramDTO {

    @XmlElement(name = "result")
    private TakerProgramResultDTO result;

    @XmlElement(name = "data")
    TakerProgramDataDTO data;
}
