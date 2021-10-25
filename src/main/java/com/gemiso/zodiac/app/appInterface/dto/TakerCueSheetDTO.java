package com.gemiso.zodiac.app.appInterface.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueSheetDTO {

    @XmlElement(name = "result")
    private TakerCueResultDTO result;

    @XmlElement(name = "data")
    private TakerCueDataDTO data;

   /* @XmlElement(name = "record")
    private List<ParentCueSheetDTO> parentCueSheetDTOList;*/


}
