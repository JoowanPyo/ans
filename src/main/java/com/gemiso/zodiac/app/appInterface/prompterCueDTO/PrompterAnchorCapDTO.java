package com.gemiso.zodiac.app.appInterface.prompterCueDTO;

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
@XmlRootElement(name = "anchor_cap")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterAnchorCapDTO {

    @XmlElement(name="anchor_cap_id")
    private Long artclCapId;

    @XmlElement(name="symbol_id")
    private String symbolId;

    @XmlElement(name="cap_ctt")
    private String capCtt;

    @XmlElement(name="ln_no")
    private int lnNo;

    @XmlElement(name="ln_ord")
    private int lnOrd;
}
