package com.gemiso.zodiac.app.appInterface.programDTO;

import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerProgramDTO {

    @XmlElement(name = "result")
    private TakerProgramResultDTO result;

    @XmlElement(name = "data")
    TakerProgramDataDTO data;
}
