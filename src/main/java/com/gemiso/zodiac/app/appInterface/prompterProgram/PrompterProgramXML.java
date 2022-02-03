package com.gemiso.zodiac.app.appInterface.prompterProgram;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterProgramXML {

    /*@XmlElement(name = "result")
    private PrompterProgramResultDTO result;

    @XmlElement(name = "data")
    private PrompterProgramDataDTO data;*/

    @XmlElement(name = "record")
    private List<PrompterProgramDTO> prompterProgramDTO = new ArrayList<>();
}
