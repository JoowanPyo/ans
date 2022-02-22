package com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "video")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueSheetVideoDTO {

    @XmlElement(name = "clip")
    private TakerCueSheetVideoClipDTO takerCueSheetVideoClipDTO;

    /*@XmlAttribute
    private String title;

    @XmlAttribute
    private String playout_id;

    @XmlAttribute
    private String seq;

    @XmlAttribute
    private String duration;*/

}
