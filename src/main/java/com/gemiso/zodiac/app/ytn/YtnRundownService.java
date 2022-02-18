package com.gemiso.zodiac.app.ytn;

import com.gemiso.zodiac.app.ytn.dto.YtnRowDTO;
import com.gemiso.zodiac.app.ytn.dto.YtnRundownCreateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class YtnRundownService {

    private final YtnRundownRepository ytnRundownRepository;

    public Long create(YtnRundownCreateDTO ytnRundownCreateDTO){

        Long ytnId = null;

        String contId = ytnRundownCreateDTO.getContId();

        List<YtnRundown> ytnRundownList = ytnRundownRepository.findByYtn(contId);

        if (CollectionUtils.isEmpty(ytnRundownList) == false){

            for (YtnRundown ytnRundown : ytnRundownList){
                Long id = ytnRundown.getId();
                ytnRundownRepository.deleteById(id);
            }
            List<YtnRowDTO> rowDTOList = ytnRundownCreateDTO.getRowList();

            for (YtnRowDTO ytnRowDTO: rowDTOList) {

                YtnRundown ytnRundown = YtnRundown.builder()
                        .contId(ytnRundownCreateDTO.getContId())
                        .brdcDtm(ytnRundownCreateDTO.getBrdcDate())
                        .brdcStartDtm(ytnRundownCreateDTO.getStartTime())
                        .brdcEndDtm(ytnRundownCreateDTO.getEndTime())
                        .ord(stringToInt(ytnRowDTO.getNO()))
                        .frm(ytnRowDTO.getFORM())
                        .mc(ytnRowDTO.getMC())
                        .rprt(ytnRowDTO.getREPORTER())
                        .video(ytnRowDTO.getVIDEO_CNT())
                        .time(ytnRowDTO.getTIME())
                        .article(ytnRowDTO.getARTICLE())
                        .build();

                ytnRundownRepository.save(ytnRundown);
            }
        }else {


        }
        return null;
    }

    public int stringToInt(String str){

        int returnValue = 0;

        if (str != null && str.trim().isEmpty() == false){
            returnValue = Integer.parseInt(str);
        }

        return returnValue;
    }
}
