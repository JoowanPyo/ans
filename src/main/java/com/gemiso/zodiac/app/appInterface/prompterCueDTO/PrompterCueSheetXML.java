package com.gemiso.zodiac.app.appInterface.prompterCueDTO;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterCueSheetXML {

    @XmlElement(name = "result")
    private PrompterCueSheetResultDTO result;

    @XmlElement(name = "data")
    private PrompterCueSheetDataDTO data;

    /*@XmlElement(name = "record")
    private List<PrompterCueRefreshDTO> cueSheetDTO = new ArrayList<>();*/
}
