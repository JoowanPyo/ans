package com.gemiso.zodiac.app.appInterface.prompterCueDTO;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterCueSheetDataDTO {

    @XmlAttribute
    private Long totalcount;

    @XmlAttribute
    private int curpage;

    @XmlAttribute
    private int rowcount;

    @XmlElement(name = "record")
    private List<PrompterCueSheetDTO> cueSheetDTO = new ArrayList<>();

    @XmlElement(name = "spare_record")
    private List<PrompterSpareCueSheetDTO> prompterSpareCueSheetDTOS = new ArrayList<>();

}
