package com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "audio_symbols")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueSheetAudioSymbolDTO {

    @XmlElement(name = "symbol")
    private List<TakerCueSheetSymbolListDTO> takerCueSheetSymbolListDTOList;
}
