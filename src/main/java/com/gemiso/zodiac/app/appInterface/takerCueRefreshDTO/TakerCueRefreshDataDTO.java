package com.gemiso.zodiac.app.appInterface.takerCueRefreshDTO;

import com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO.TakerCueSheetDTO;
import lombok.Data;

import javax.xml.bind.annotation.*;

@Data
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueRefreshDataDTO {

    @XmlAttribute
    private Long totalcount;

    @XmlAttribute
    private int curpage;

    @XmlAttribute
    private int rowcount;

    @XmlElement(name = "record")
    private TakerCueSheetDTO takerCueSheetDTO;

   /* @XmlElement(name = "record")
    private List<ParentProgramDTO> parentCueSheetDTOList;*/


}
