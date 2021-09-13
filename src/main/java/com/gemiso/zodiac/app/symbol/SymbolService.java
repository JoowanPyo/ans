package com.gemiso.zodiac.app.symbol;

import com.gemiso.zodiac.app.file.AttachFile;
import com.gemiso.zodiac.app.file.AttachFileRepository;
import com.gemiso.zodiac.app.file.dto.AttachFileDTO;
import com.gemiso.zodiac.app.file.mapper.AttachFileMapper;
import com.gemiso.zodiac.app.symbol.dto.SymbolCreateDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolDTO;
import com.gemiso.zodiac.app.symbol.dto.SymbolUpdateDTO;
import com.gemiso.zodiac.app.symbol.mapper.SymbolCreateMapper;
import com.gemiso.zodiac.app.symbol.mapper.SymbolMapper;
import com.gemiso.zodiac.app.symbol.mapper.SymbolUpdateMapper;
import com.gemiso.zodiac.app.user.QUser;
import com.gemiso.zodiac.core.service.AuthAddService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class SymbolService {

    private final SymbolRepository symbolRepository;
    private final AttachFileRepository attachFileRepository;

    private final SymbolMapper symbolMapper;
    private final SymbolCreateMapper symbolCreateMapper;
    private final SymbolUpdateMapper symbolUpdateMapper;
    private final AttachFileMapper attachFileMapper;


    private final AuthAddService authAddService;

    public List<SymbolDTO> findAll(String useYn, String userNm, String delYn){

        BooleanBuilder booleanBuilder = getSearch(useYn, userNm, delYn);

        List<Symbol> symbolList = (List<Symbol>) symbolRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "id"));

        List<SymbolDTO> symbolDTOS = symbolMapper.toDtoList(symbolList);

        return symbolDTOS;
    }


    public SymbolDTO find(Long symbolId){ //방송 아이콘 상세(단건) 조회

        //수정! 1:1관계에서 조인이 잘 되지 않음.
        Symbol symbol = userFindOrFail(symbolId);

        SymbolDTO symbolDTO = symbolMapper.toDto(symbol);

/*        Optional<AttachFile> attachFile = attachFileRepository.findById(symbol.getAttachFile().getFileId());

        if (attachFile.isPresent()){

            AttachFileDTO attachFileDTO = attachFileMapper.toDto(attachFile.get());
            symbolDTO.setAttachFile(attachFileDTO);
        }*/

        return symbolDTO;
    }

    public Long create(SymbolCreateDTO symbolCreateDTO){ //방송 아이콘 등록 서비스

        String userId = authAddService.authUser.getUserId(); //토큰에필터에서 토큰 파싱하여 등록된 UserId

        symbolCreateDTO.setInputrId(userId); //등록자 추가.

        Symbol symbol = symbolCreateMapper.toEntity(symbolCreateDTO); //DTO -> Entity로 변환

        symbolRepository.save(symbol); //등록

        return symbol.getSymbolId(); //DTO -> Entity변환시 자동생성된 ID return

    }

    public void update(SymbolUpdateDTO symbolUpdateDTO, Long symbolId){

        Symbol symbol = userFindOrFail(symbolId);


        String userId = authAddService.authUser.getUserId();
        symbolUpdateDTO.setUpdtrId(userId); // 수정자 추가.

        symbolUpdateMapper.updateFromDto(symbolUpdateDTO, symbol);
        symbolRepository.save(symbol);

    }

    public void delete(Long symbolId){

        Symbol symbol = userFindOrFail(symbolId);

        SymbolDTO symbolDTO = symbolMapper.toDto(symbol);

        symbolDTO.setDelDtm(new Date());
        String userId = authAddService.authUser.getUserId();
        symbolDTO.setDelrId(userId);
        symbolDTO.setDelYn("Y");

        symbolMapper.updateFromDto(symbolDTO, symbol);

        symbolRepository.save(symbol);


    }


    public Symbol userFindOrFail(Long symbolId){ //방송 아이콘 Id로 등록된 방송아이콘 유무 검증.

        Optional<Symbol> symbol = symbolRepository.findBySymbolId(symbolId);

        if (!symbol.isPresent()){
            throw new ResourceNotFoundException("방송아이콘에 등록된 방송아이콘 아이디가 없습니다. : " + symbolId);
        }

        return symbol.get();

    }

    private BooleanBuilder getSearch(String useYn, String userNm, String delYn) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QSymbol qSymbol = QSymbol.symbol;

        if(!StringUtils.isEmpty(useYn)){
            booleanBuilder.and(qSymbol.useYn.eq(useYn));
        }
        if(!StringUtils.isEmpty(userNm)){
            booleanBuilder.and(qSymbol.symbolNm.contains(userNm));
        }
        if(!StringUtils.isEmpty(delYn)){
            booleanBuilder.and(qSymbol.delYn.eq(delYn));
        }else{
            booleanBuilder.and(qSymbol.delYn.eq("N"));
        }
        return booleanBuilder;
    }
}
