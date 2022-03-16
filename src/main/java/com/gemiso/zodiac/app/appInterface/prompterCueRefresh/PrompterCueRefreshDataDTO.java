package com.gemiso.zodiac.app.appInterface.prompterCueRefresh;

import com.gemiso.zodiac.app.appInterface.prompterCue.PrompterCueSheetDTO;
import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

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
