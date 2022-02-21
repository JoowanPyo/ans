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
@XmlRootElement(name = "video")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueVideoDTO {

    @XmlAttribute
    private String title;

    @XmlAttribute
    private String playout_id;

    @XmlAttribute
    private String seq;

    @XmlAttribute
    private String duration;

}
