package com.gemiso.zodiac.app.appInterface.codeDTO;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

@Data
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCodeDataDTO {

    @XmlElement(name = "record")
    private TakerCodeDTO takerCodeDTO;
}
