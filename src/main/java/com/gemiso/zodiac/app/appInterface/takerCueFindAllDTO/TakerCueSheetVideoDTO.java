package com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "video")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueSheetVideoDTO {

    @XmlElement(name = "clip")
    private List<TakerCueSheetVideoClipDTO> takerCueSheetVideoClipDTO;

    /*@XmlAttribute
    private String title;

    @XmlAttribute
    private String playout_id;

    @XmlAttribute
    private String seq;

    @XmlAttribute
    private String duration;*/

}
