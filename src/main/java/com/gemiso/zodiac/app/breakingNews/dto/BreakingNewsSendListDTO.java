package com.gemiso.zodiac.app.breakingNews.dto;


import lombok.Data;
import org.springframework.stereotype.Component;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "cg_list")
@XmlAccessorType(XmlAccessType.FIELD)
@Component
public class BreakingNewsSendListDTO {

    @XmlElement(name = "cg")
    private List<BreakingNewsSendDTO> cg;
}
