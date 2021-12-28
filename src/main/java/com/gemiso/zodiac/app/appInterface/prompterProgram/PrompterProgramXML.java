package com.gemiso.zodiac.app.appInterface.prompterProgram;

import com.gemiso.zodiac.app.appInterface.prompterCue.PrompterCueSheetDataDTO;
import com.gemiso.zodiac.app.appInterface.prompterCue.PrompterCueSheetResultDTO;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterProgramXML {

    @XmlElement(name = "result")
    private PrompterProgramResultDTO result;

    @XmlElement(name = "data")
    private PrompterProgramDataDTO data;
}
