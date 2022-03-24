package com.gemiso.zodiac.app.appInterface.prompterCueRefreshDTO;

import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterCueRefreshDataDTO {

    @XmlAttribute
    private Long totalcount;

    @XmlAttribute
    private int curpage;

    @XmlAttribute
    private int rowcount;

    @XmlElement(name = "record")
    private PrompterCueRefreshDTO cueSheetDTO;
}
