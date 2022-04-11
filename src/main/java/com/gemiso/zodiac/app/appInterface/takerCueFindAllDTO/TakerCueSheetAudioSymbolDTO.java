package com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "audio_symbols")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueSheetAudioSymbolDTO {

    @XmlElement(name = "symbol")
    private List<TakerCueSheetSymbolListDTO> takerCueSheetSymbolListDTOList;
}
