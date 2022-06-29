package com.gemiso.zodiac.app.appInterface.codeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@XmlRootElement(name = "code")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCodeHrnkDTO {

    @XmlElement(name="cd")
    private String cd;
    @XmlElement(name="cd_nm")
    private String cdNm;
}
