package com.gemiso.zodiac.app.cueSheetTemplateSymbol;

import com.gemiso.zodiac.app.cueSheetTemplate.CueSheetTemplate;
import com.gemiso.zodiac.app.cueSheetTemplate.dto.CueSheetTemplateSimpleDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.dto.CueTmplSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.mapper.CueTmplSymbolCreateMapper;
import com.gemiso.zodiac.app.cueSheetTemplateSymbol.mapper.CueTmplSymbolMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueTmplSymbolService {

    private final CueTmplSymbolRepository cueTmplSymbolRepository;

    private final CueTmplSymbolMapper cueTmplSymbolMapper;
    private final CueTmplSymbolCreateMapper cueTmplSymbolCreateMapper;


    public void create(Long cueTmpltId, List<CueTmplSymbolCreateDTO> cueTmplSymbolCreateDTO){

        //해당 큐시트 아이템으로 등록된 큐시트 방송아이콘 리스트를 불러온다.
        List<CueTmplSymbol> cueTmplSymbolList = cueTmplSymbolRepository.findCueTmplSymbol(cueTmpltId);

        //큐시트 아이템으로 등록되어 있는 방송아이콘 삭제.
        for (CueTmplSymbol cueTmplSymbol : cueTmplSymbolList){
            Long id = cueTmplSymbol.getId();
            cueTmplSymbolRepository.deleteById(id);
        }

        //큐시트 아이템 방송아이콘에 넣어줄 큐시트아이템 아이디 빌드
        CueSheetTemplateSimpleDTO cueSheetTemplate = CueSheetTemplateSimpleDTO.builder().cueTmpltId(cueTmpltId).build();

        for (CueTmplSymbolCreateDTO cueTmplSymbolDTO : cueTmplSymbolCreateDTO){//새로등록할 방송아이콘 등록
            cueTmplSymbolDTO.setCueSheetTemplate(cueSheetTemplate);//큐시트아이템 아이디 set
            
            CueTmplSymbol cueTmplSymbol = cueTmplSymbolCreateMapper.toEntity(cueTmplSymbolDTO); //엔티티 변환
            cueTmplSymbolRepository.save(cueTmplSymbol);//등록
        }
    }

}
