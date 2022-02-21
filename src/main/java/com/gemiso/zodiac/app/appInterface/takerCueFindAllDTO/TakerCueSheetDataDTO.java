package com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO;

import lombok.Data;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Data
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueSheetDataDTO {

    @XmlAttribute
    private Long totalcount;

    @XmlAttribute
    private int curpage;

    @XmlAttribute
    private int rowcount;

    @XmlElement(name = "record")
    private List<TakerCueSheetDTO> takerCueSheetDTO = new ArrayList<>();

    @XmlElement(name = "spareRecord")
    private List<TakerCueSheetSpareDTO> takerCueSheetSpareDTO = new ArrayList<>();

   /* @XmlElement(name = "record")
    private List<ParentProgramDTO> parentCueSheetDTOList;*/


}
