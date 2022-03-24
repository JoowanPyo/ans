package com.gemiso.zodiac.app.appInterface.prompterCueRefreshDTO;

import com.gemiso.zodiac.app.appInterface.prompterCueDTO.PrompterCueSheetResultDTO;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterCueRefreshXML {

    @XmlElement(name = "result")
    private PrompterCueSheetResultDTO result;

    @XmlElement(name = "data")
    private PrompterCueRefreshDataDTO data;

    /*@XmlElement(name = "record")
    private List<PrompterCueRefreshDTO> cueSheetDTO = new ArrayList<>();*/
}
