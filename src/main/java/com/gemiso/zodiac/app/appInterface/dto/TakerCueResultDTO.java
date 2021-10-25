package com.gemiso.zodiac.app.appInterface.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueResultDTO {

    @XmlAttribute
    private String xuccess;

    @XmlAttribute
    private String msg;
}
