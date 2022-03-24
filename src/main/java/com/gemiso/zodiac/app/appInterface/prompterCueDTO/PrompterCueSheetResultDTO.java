package com.gemiso.zodiac.app.appInterface.prompterCueDTO;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterCueSheetResultDTO {

    @XmlAttribute
    private String success;

    @XmlAttribute
    private String msg;
}
