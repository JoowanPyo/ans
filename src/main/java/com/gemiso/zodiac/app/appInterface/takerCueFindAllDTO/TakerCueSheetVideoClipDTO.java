package com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "clip")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueSheetVideoClipDTO {

    @XmlAttribute
    private String title;

    @XmlAttribute(name = "playout_id")
    private String playout_id;

    @XmlAttribute
    private String seq;

    @XmlAttribute
    private String duration;
}