package com.gemiso.zodiac.app.appInterface.prompterCueDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "article_caps")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrompterArticleCaps {

    @XmlElement(name = "article_cap")
    private List<PrompterArticleCapDTO> articleCapList = new ArrayList<>();
}
