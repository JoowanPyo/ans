package com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "video_symbols")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueSheetVideoSymbolDTO {

    @XmlElement(name = "symbol")
    private List<TakerCueSheetSymbolListDTO> takerCueSheetSymbolListDTOList;
}
