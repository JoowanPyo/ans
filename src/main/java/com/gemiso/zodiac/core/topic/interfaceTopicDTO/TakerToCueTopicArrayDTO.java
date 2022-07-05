package com.gemiso.zodiac.core.topic.interfaceTopicDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TakerToCueTopicArrayDTO extends TakerToCueTopicDTO{

    //private TakerToCueTopicDTO header;
    private List<TakerToCueArrayBodyDTO> body;
}
