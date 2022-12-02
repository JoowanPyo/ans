package com.gemiso.zodiac.app.breakingNews.dto;

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
@XmlRootElement(name = "cg")
@Builder
@XmlAccessorType(XmlAccessType.FIELD)
public class BreakingNewsSendDTO {

    @XmlElement(name="PAGE")
    private Integer page;

    @XmlElement(name="CONTENT")
    private String content;

    @XmlElement(name="TEMPLATE")
    private String template;


}
