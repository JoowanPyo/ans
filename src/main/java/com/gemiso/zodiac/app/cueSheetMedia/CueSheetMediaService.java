package com.gemiso.zodiac.app.cueSheetMedia;

import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaCreateDTO;
import com.gemiso.zodiac.app.cueSheetMedia.dto.CueSheetMediaRequestDTO;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaCreateMapper;
import com.gemiso.zodiac.app.cueSheetMedia.mapper.CueSheetMediaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetMediaService {

    private final CueSheetMediaRepository cueSheetMediaRepository;

    private final CueSheetMediaMapper cueSheetMediaMapper;
    private final CueSheetMediaCreateMapper cueSheetMediaCreateMapper;


    public Long create(CueSheetMediaRequestDTO cueSheetMediaRequestDTO){


        List<CueSheetMediaCreateDTO> cueSheetMediaCreateDTOList = cueSheetMediaRequestDTO.getCueSheetMediaCreateDTO();

        if (!ObjectUtils.isEmpty(cueSheetMediaCreateDTOList)){

            for (CueSheetMediaCreateDTO cueSheetMediaCreateDTO : cueSheetMediaCreateDTOList){

                cueSheetMediaCreateDTO.setCueItemId(cueSheetMediaRequestDTO.getCueItemId());


            }

        }


    return null;
    }

}
