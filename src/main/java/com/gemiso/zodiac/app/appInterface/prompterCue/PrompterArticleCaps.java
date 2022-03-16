package com.gemiso.zodiac.app.appInterface.prompterCue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "articleCaps")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterArticleCaps {

    @XmlElement(name = "articleCap")
    private List<PrompterArticleCapDTO> articleCapList = new ArrayList<>();
}
