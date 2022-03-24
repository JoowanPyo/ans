package com.gemiso.zodiac.app.appInterface.prompterCueDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "anchorCaps")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterAnchorCaps {

    @XmlElement(name = "anchorCap")
    private List<PrompterAnchorCapDTO> anchorCapList = new ArrayList<>();
}
