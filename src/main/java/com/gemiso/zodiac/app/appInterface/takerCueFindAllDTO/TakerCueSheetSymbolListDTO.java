package com.gemiso.zodiac.app.appInterface.takerCueFindAllDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "symbol")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCueSheetSymbolListDTO {

    //@XmlElement(name="symbol_id")
    @XmlAttribute(name="symbol_id")
    private String symbolId;
}
