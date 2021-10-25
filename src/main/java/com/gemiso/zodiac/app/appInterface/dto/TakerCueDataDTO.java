package com.gemiso.zodiac.app.appInterface.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueDataDTO {

    @XmlAttribute
    private Long totalcount;

    @XmlAttribute
    private int curpage;

    @XmlAttribute
    private int rowcount;

    @XmlElement(name = "record")
    private List<ParentCueSheetDTO> parentCueSheetDTOList;
}
