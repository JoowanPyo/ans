package com.gemiso.zodiac.app.symbol;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolCreateDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolOrdUpdateDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolUpdateDTO;
import com.gemiso.zodiac.app.symbol.mapper.SymbolCreateMapper;
import com.gemiso.zodiac.app.symbol.mapper.SymbolMapper;
import com.gemiso.zodiac.app.symbol.mapper.SymbolUpdateMapper;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SymbolService {

    private final SymbolRepository symbolRepository;
    //private final AttachFileRepository attachFileRepository;

    private final SymbolMapper symbolMapper;
    private final SymbolCreateMapper symbolCreateMapper;
    private final SymbolUpdateMapper symbolUpdateMapper;
    //private final AttachFileMapper attachFileMapper;

    private final UserAuthService userAuthService;

    @Value("${files.url-key}")
    private String fileUrl;

    public List<SymbolDTO> findAll(String useYn, String symbolNm){

        BooleanBuilder booleanBuilder = getSearch(useYn, symbolNm);

        List<Symbol> symbolList = (List<Symbol>) symbolRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "symbolOrd"));

        List<SymbolDTO> symbolDTOS = symbolMapper.toDtoList(symbolList);

        //방송아이콘 이미지가 있는 서버Url Set
        List<SymbolDTO> returnSymbolDTOList = new ArrayList<>();

            for (SymbolDTO symbolDTO : symbolDTOS) {

                if (ObjectUtils.isEmpty(symbolDTO.getAttachFile()) == false) {
                    String fileLoc = symbolDTO.getAttachFile().getFileLoc();
                    String url = fileUrl + fileLoc;
                    symbolDTO.setUrl(url);
                }
                returnSymbolDTOList.add(symbolDTO);
            }


        return returnSymbolDTOList;
    }


    public SymbolDTO find(String symbolId){ //방송 아이콘 상세(단건) 조회

        //수정! 1:1관계에서 조인이 잘 되지 않음.
        Symbol symbol = symbolFindOrFail(symbolId);

        SymbolDTO symbolDTO = symbolMapper.toDto(symbol);

        //방송아이콘 이미지가 있는 서버Url Set
        if (ObjectUtils.isEmpty(symbolDTO.getAttachFile()) == false) {
            String fileLoc = symbolDTO.getAttachFile().getFileLoc();
            String url = fileUrl + fileLoc;
            symbolDTO.setUrl(url);
        }

        return symbolDTO;
    }

    public String create(SymbolCreateDTO symbolCreateDTO){ //방송 아이콘 등록 서비스

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        symbolCreateDTO.setInputrId(userId); //등록자 추가.

        Symbol symbol = symbolCreateMapper.toEntity(symbolCreateDTO); //articleDTO -> Entity로 변환

        symbolRepository.save(symbol); //등록

        return symbol.getSymbolId(); //articleDTO -> Entity변환시 자동생성된 ID return

    }

    public void update(SymbolUpdateDTO symbolUpdateDTO, String symbolId){

        Symbol symbol = symbolFindOrFail(symbolId);

        String userId = userAuthService.authUser.getUserId();
        symbolUpdateDTO.setUpdtrId(userId); // 수정자 추가.

        //수정.
        //파일이 바뀐경우 엔티티에서 파일을 지우고 새로운 파일로 업데이트,이유=update가 안댐[file_id가 필수값.]
        AttachFileDTO attachFileDTO = symbolUpdateDTO.getAttachFile();
        Long newFileId = Optional.ofNullable(attachFileDTO.getFileId()).orElse(0L);
        Long orgFileId = Optional.ofNullable(symbol.getAttachFile().getFileId()).orElse(0L);
        if (newFileId.equals(orgFileId) == false){
            AttachFile attachFile = AttachFile.builder().build();
            symbol.setAttachFile(attachFile);
        }

        symbolUpdateMapper.updateFromDto(symbolUpdateDTO, symbol);
        symbolRepository.save(symbol);

    }

    public void delete(String symbolId){

        Symbol symbol = symbolFindOrFail(symbolId);

        SymbolDTO symbolDTO = symbolMapper.toDto(symbol);


        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        symbolDTO.setDelrId(userId);
        symbolDTO.setDelYn("Y");
        symbolDTO.setDelDtm(new Date());

        symbolMapper.updateFromDto(symbolDTO, symbol);

        symbolRepository.save(symbol);


    }

    public void ordupdate(SymbolOrdUpdateDTO symbolOrdUpdateDTO, String symbolId){

        Symbol symbol = symbolFindOrFail(symbolId);

        Integer symbolOrd = symbolOrdUpdateDTO.getSymbolOrd();

        SymbolDTO symbolDTO = symbolMapper.toDto(symbol);
        symbolDTO.setSymbolOrd(symbolOrd);

        symbolMapper.updateFromDto(symbolDTO, symbol);
        symbolRepository.save(symbol);

        String typCd = symbolOrdUpdateDTO.getTypCd();

        List<Symbol> symbolList = symbolRepository.findSymbolList(typCd);

        for (int i = symbolList.size()-1; i >= 0; i-- ){

            String id = symbolList.get(i).getSymbolId();

            if (id.equals(symbolId)){
                symbolList.remove(i);
            }
        }

        symbolList.add(symbolOrd, symbol);

        //조회된 방송아이콘 리스트 Ord 업데이트
        int index = 0;
        for (Symbol symbolEntity : symbolList){

            SymbolDTO updateSymbolDTO = symbolMapper.toDto(symbolEntity);
            updateSymbolDTO.setSymbolOrd(index);

            Symbol symbolSave = symbolMapper.toEntity(updateSymbolDTO);

            symbolRepository.save(symbolSave);

            index++;
        }

    }

    public Symbol symbolFindOrFail(String symbolId){ //방송 아이콘 Id로 등록된 방송아이콘 유무 검증.

        Optional<Symbol> symbol = symbolRepository.findBySymbolId(symbolId);

        if (symbol.isPresent() == false){
            throw new ResourceNotFoundException("방송아이콘을 찾을 수 없습니다. 방송아이콘 아이디 : " + symbolId);
        }

        return symbol.get();

    }

    private BooleanBuilder getSearch(String useYn, String symbolNm) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QSymbol qSymbol = QSymbol.symbol;

        booleanBuilder.and(qSymbol.delYn.eq("N"));

        if(useYn != null && useYn.trim().isEmpty() == false){
            booleanBuilder.and(qSymbol.useYn.eq(useYn));
        }
        if(symbolNm != null && symbolNm.trim().isEmpty() == false){
            booleanBuilder.and(qSymbol.symbolNm.contains(symbolNm));
        }

        return booleanBuilder;
    }
}
