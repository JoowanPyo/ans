package com.gemiso.zodiac.app.cueSheetItem;

import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSimpleDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolCreateDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolDTO;
import com.gemiso.zodiac.app.cueSheetItem.dto.CueSheetItemSymbolUpdateDTO;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemSymbolCreateMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemSymbolMapper;
import com.gemiso.zodiac.app.cueSheetItem.mapper.CueSheetItemSymbolUpdateMapper;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.exception.ApiRequestException;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${files.url-key}")
    private String fileUrl;

    private final CueSheetItemSymbolRepository cueSheetItemSymbolRepository;

    private final CueSheetItemSymbolCreateMapper cueSheetItemSymbolCreateMapper;
    private final CueSheetItemSymbolMapper cueSheetItemSymbolMapper;
    private final CueSheetItemSymbolUpdateMapper cueSheetItemSymbolUpdateMapper;


    public CueSheetItemSymbolDTO find(Long cueItemId){ //큐시트 아이템 방송아이콘 상세조회

        CueSheetItemSymbol cueSheetItemSymbol = findSymbol(cueItemId);

        CueSheetItemSymbolDTO cueSheetItemSymbolDTO = cueSheetItemSymbolMapper.toDto(cueSheetItemSymbol);

        cueSheetItemSymbolDTO = setSymbolUrl(cueSheetItemSymbolDTO);

        return cueSheetItemSymbolDTO;

    }

    public CueSheetItemSymbolDTO setSymbolUrl(CueSheetItemSymbolDTO cueSheetItemSymbolDTO){

        SymbolDTO symbolDTO = cueSheetItemSymbolDTO.getSymbol();

        if (ObjectUtils.isEmpty(symbolDTO) == false) {
            String fileLoc = symbolDTO.getAttachFile().getFileLoc(); //파일로그 get
            String url = fileUrl + fileLoc; //url + 파일로그

            symbolDTO.setUrl(url);//방송아이콘이 저장된 url set

        }

        cueSheetItemSymbolDTO.setSymbol(symbolDTO);
        return cueSheetItemSymbolDTO;
    }

    public Long create(CueSheetItemSymbolCreateDTO cueSheetItemSymbolCreateDTO, Long cueItemId){ //큐시트 아이템 방송아이콘 등록

        //chkSymbol(cueSheetItemSymbolCreateDTO, cueItemId); //순번과 타입이 같은 방송아이콘이 있는지 확인.[있을경우 삭제]

        findSymbolList(cueSheetItemSymbolCreateDTO, cueItemId);

        //큐시트 아이템 아이디 set
        CueSheetItemSimpleDTO cueSheetItemSimpleDTO = CueSheetItemSimpleDTO.builder().cueItemId(cueItemId).build();
        cueSheetItemSymbolCreateDTO.setCueSheetItem(cueSheetItemSimpleDTO);

        CueSheetItemSymbol cueSheetItemSymbolEntity = cueSheetItemSymbolCreateMapper.toEntity(cueSheetItemSymbolCreateDTO);

        cueSheetItemSymbolRepository.save(cueSheetItemSymbolEntity);


        return cueSheetItemSymbolEntity.getId();

    }
    
    public void findSymbolList(CueSheetItemSymbolCreateDTO cueSheetItemSymbolCreateDTO, Long cueItemId){

        //큐시트아이템 방송아이콘 List 조회
        List<CueSheetItemSymbol> cueSheetItemSymbol = cueSheetItemSymbolRepository.findSymbol(cueItemId);

        int newOrd = cueSheetItemSymbolCreateDTO.getOrd();//새로 등록할 큐시트아이템 방송아이콘 순번 get
        String newTypCd = cueSheetItemSymbolCreateDTO.getSymbol().getTypCd();// "tv", "radio" new구분코드

        if (ObjectUtils.isEmpty(cueSheetItemSymbol) == false){
            for (CueSheetItemSymbol getCueItemSymbol : cueSheetItemSymbol){
                int orgOrd = getCueItemSymbol.getOrd(); //등록되어 있던 큐시트아이템 방송아이콘 순번 get.
                Long orgId = getCueItemSymbol.getId();//기존아이디 get
                String ordTypCd = getCueItemSymbol.getSymbol().getTypCd();//기본 구분코드 get
                if (orgOrd == newOrd && ordTypCd.equals(newTypCd)){ //새로등록할 큐시트아이템 방송아이콘 순번과,그분타입 이미등록되어있던 방손아이콘 순번이 같으면 에러.
                    cueSheetItemSymbolRepository.deleteById(orgId); //새로 들어온 순번에 들어가 있던 방송아이콘 삭제.
                }
            }

        }


    }

    public CueSheetItemSymbol findSymbol(Long cueItemId){

        Optional<CueSheetItemSymbol> cueSheetItemSymbol = cueSheetItemSymbolRepository.findById(cueItemId);

        if (cueSheetItemSymbol.isPresent() == false){
            throw new ResourceNotFoundException("큐시트 아이템에 해당하는 방송아이콘이 없습니다. Cuesheetitem Id : " + cueItemId);
        }

        return cueSheetItemSymbol.get();

    }

    //큐시트아이템 방송아이콘 업데이트
    public Long update(CueSheetItemSymbolUpdateDTO cueSheetItemSymbolUpdateDTO, Long cueItemId){

        //큐시트 아이템에 등록된 방송아이콘 List조회
        List<CueSheetItemSymbol> cueSheetItemSymbol = cueSheetItemSymbolRepository.findSymbol(cueItemId); 
        
        int newOrd = cueSheetItemSymbolUpdateDTO.getOrd(); //수정할 방송아이콘 순번get
        
        for (CueSheetItemSymbol getCueSheetItemSymbol : cueSheetItemSymbol){
            Long orgId = getCueSheetItemSymbol.getId(); //등록되어 있던 방송아이콘 Id
            int orgOrd = getCueSheetItemSymbol.getOrd(); //등록되어 있던 방송아이콘 ord

            if (newOrd == orgOrd){
                cueSheetItemSymbolRepository.deleteById(orgId); //새로 들어온 순번에 들어가 있던 방송아이콘 삭제.
            }
        }
        //새로 등록할 방송아이콘 Entity변환
        CueSheetItemSymbol cueSheetItemSymbolEntity = cueSheetItemSymbolUpdateMapper.toEntity(cueSheetItemSymbolUpdateDTO);

        cueSheetItemSymbolRepository.save(cueSheetItemSymbolEntity);

        return cueSheetItemSymbolEntity.getId();

    }

    public void delete(Long cueItemSymbolId){

        //큐시트 아이템 방송아이콘 아이디로 큐시트아이템 방송아이콘 존재 유무 확인
        Optional<CueSheetItemSymbol> getCueSheetItemSymbol = cueSheetItemSymbolRepository.findById(cueItemSymbolId);
        if (getCueSheetItemSymbol.isPresent() == false){
            throw new ResourceNotFoundException("큐시트 아이템 방송아이콘이 없습니다. 큐시트 아이템 방송아이콘 아이디 : " + cueItemSymbolId);
        }
        //큐시트 아이템 방송아이콘 삭제
        cueSheetItemSymbolRepository.deleteById(cueItemSymbolId);


    }

    /*    public void chkSymbol(CueSheetItemSymbolCreateDTO cueSheetItemSymbolCreateDTO, Long cueItemId){

        List<CueSheetItemSymbol> cueSheetItemSymbolList = cueSheetItemSymbolRepository.findSymbol(cueItemId);

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
    }*/
}
