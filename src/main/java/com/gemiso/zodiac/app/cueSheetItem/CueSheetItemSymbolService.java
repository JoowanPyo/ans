package com.gemiso.zodiac.app.cueSheetItem;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolDTO;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemSymbolCreateMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemSymbolMapper;
import com.gemiso.zodiac.app.symbol.Symbol;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CueSheetItemSymbolService {

    private final CueSheetItemSymbolRepository cueSheetItemSymbolRepository;

    private final CueSheetItemSymbolCreateMapper cueSheetItemSymbolCreateMapper;
    private final CueSheetItemSymbolMapper cueSheetItemSymbolMapper;


    public CueSheetItemSymbolDTO find(Long cueItemId){

        CueSheetItemSymbol cueSheetItemSymbol = findSymbol(cueItemId);

        CueSheetItemSymbolDTO cueSheetItemSymbolDTO = cueSheetItemSymbolMapper.toDto(cueSheetItemSymbol);

        return cueSheetItemSymbolDTO;

    }

    public void create(CueSheetItemSymbolCreateDTO cueSheetItemSymbolCreateDTO, Long cueItemId){

        chkSymbol(cueSheetItemSymbolCreateDTO, cueItemId); //순번과 타입이 같은 방송아이콘이 있는지 확인.[있을경우 삭제]

        CueSheetItemSymbol cueSheetItemSymbolEntity = cueSheetItemSymbolCreateMapper.toEntity(cueSheetItemSymbolCreateDTO);

        cueSheetItemSymbolRepository.save(cueSheetItemSymbolEntity);

    }

    public void chkSymbol(CueSheetItemSymbolCreateDTO cueSheetItemSymbolCreateDTO, Long cueItemId){

        List<CueSheetItemSymbol> cueSheetItemSymbolList = findSymbolList(cueItemId);

        //update 방송아이콘
        int newOrd = cueSheetItemSymbolCreateDTO.getOrd();//신규 방송아이콘 순번
        String newTypCd = cueSheetItemSymbolCreateDTO.getSymbol().getTypCd();//신규 방송아이콘 타입 [기사, 큐시트오디오, 큐시트비디오]

        for (CueSheetItemSymbol cueSheetItemSymbol : cueSheetItemSymbolList){

            Long id = cueSheetItemSymbol.getId();//조회한 방송아이콘 아이디
            int ord = cueSheetItemSymbol.getOrd();//조회한 방송아이콘 순번
            Symbol symbol = cueSheetItemSymbol.getSymbol();
            String typCd = symbol.getTypCd();//조회한 방송아이콘 타입 [기사, 큐시트오디오, 큐시트비디오]

            if (ord == newOrd && typCd.equals(newTypCd)){ //방송아이콘 순번과, 타입이 동일한 경우 삭제후 재등록.
                cueSheetItemSymbolRepository.deleteById(id);
            }
        }
    }

    public List<CueSheetItemSymbol> findSymbolList(Long cueItemId){

        List<CueSheetItemSymbol> cueSheetItemSymbol = cueSheetItemSymbolRepository.findSymbol(cueItemId);

        if (ObjectUtils.isEmpty(cueSheetItemSymbol)){
            throw new ResourceNotFoundException("방송아이콘을 찾을수 없습니다. Cuesheetitem Id : " + cueItemId);
        }

        return cueSheetItemSymbol;

    }

    public CueSheetItemSymbol findSymbol(Long cueItemId){

        Optional<CueSheetItemSymbol> cueSheetItemSymbol = cueSheetItemSymbolRepository.findById(cueItemId);

        if (cueSheetItemSymbol.isPresent() == false){
            throw new ResourceNotFoundException("큐시트 아이템에 해당하는 방송아이콘이 없습니다. Cuesheetitem Id : " + cueItemId);
        }

        return cueSheetItemSymbol.get();

    }
}
