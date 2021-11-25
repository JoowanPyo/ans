package com.gemiso.zodiac.app.appInterface.codeDTO;

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
@XmlRootElement(name = "record")
@XmlAccessorType(XmlAccessType.FIELD)
public class TakerCodeDTO {

    @XmlElement(name="hrnk_cd")
    private String hrnkCd;
    @XmlElement(name="hrnk_cd_nm")
    private String hrnkCdNm;
    @XmlElement(name="code")
    List<TakerCodeHrnkDTO> code;
}
