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
import com.gemiso.zodiac.app.user.User;
import com.gemiso.zodiac.app.user.dto.UserSimpleDTO;
import com.gemiso.zodiac.core.service.UserAuthService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
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

        List<Symbol> symbolList = (List<Symbol>) symbolRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "symbolId"));

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


    public SymbolDTO find(Long symbolId){ //방송 아이콘 상세(단건) 조회

        //수정! 1:1관계에서 조인이 잘 되지 않음.
        Symbol symbol = userFindOrFail(symbolId);

        SymbolDTO symbolDTO = symbolMapper.toDto(symbol);

        //방송아이콘 이미지가 있는 서버Url Set
        if (ObjectUtils.isEmpty(symbolDTO.getAttachFile()) == false) {
            String fileLoc = symbolDTO.getAttachFile().getFileLoc();
            String url = fileUrl + fileLoc;
            symbolDTO.setUrl(url);
        }

        return symbolDTO;
    }

    public Long create(SymbolCreateDTO symbolCreateDTO){ //방송 아이콘 등록 서비스

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        UserSimpleDTO userSimpleDTO = UserSimpleDTO.builder().userId(userId).build();
        symbolCreateDTO.setInputr(userSimpleDTO); //등록자 추가.

        Symbol symbol = symbolCreateMapper.toEntity(symbolCreateDTO); //DTO -> Entity로 변환

        symbolRepository.save(symbol); //등록

        return symbol.getSymbolId(); //DTO -> Entity변환시 자동생성된 ID return

    }

    public void update(SymbolUpdateDTO symbolUpdateDTO, Long symbolId){

        Symbol symbol = userFindOrFail(symbolId);

        // 토큰 인증된 사용자 아이디를 입력자로 등록
        User updtr = symbol.getUpdtr();
        if (ObjectUtils.isEmpty(updtr) == false){
            symbol.setUpdtr(null);
        }
        String userId = userAuthService.authUser.getUserId();
        UserSimpleDTO userSimpleDTO = UserSimpleDTO.builder().userId(userId).build();
        symbolUpdateDTO.setUpdtr(userSimpleDTO); // 수정자 추가.

        //수정.
        //파일이 바뀐경우 엔티티에서 파일을 지우고 새로운 파일로 업데이트,이유=update가 안댐[jpa]
        AttachFileDTO attachFileDTO = symbolUpdateDTO.getAttachFile();
        if (ObjectUtils.isEmpty(attachFileDTO)){
            symbol.setAttachFile(null);
        }

        symbolUpdateMapper.updateFromDto(symbolUpdateDTO, symbol);
        symbolRepository.save(symbol);

    }

    public void delete(Long symbolId){

        Symbol symbol = userFindOrFail(symbolId);

        SymbolDTO symbolDTO = symbolMapper.toDto(symbol);


        // 토큰 인증된 사용자 아이디를 입력자로 등록
        String userId = userAuthService.authUser.getUserId();
        UserSimpleDTO userSimpleDTO = UserSimpleDTO.builder().userId(userId).build();
        symbolDTO.setDelr(userSimpleDTO);
        symbolDTO.setDelYn("Y");
        symbolDTO.setDelDtm(new Date());

        symbolMapper.updateFromDto(symbolDTO, symbol);

        symbolRepository.save(symbol);


    }


    public Symbol userFindOrFail(Long symbolId){ //방송 아이콘 Id로 등록된 방송아이콘 유무 검증.

        Optional<Symbol> symbol = symbolRepository.findBySymbolId(symbolId);

        if (symbol.isPresent() == false){
            throw new ResourceNotFoundException("방송아이콘에 등록된 방송아이콘 아이디가 없습니다. : " + symbolId);
        }

        return symbol.get();

    }

    private BooleanBuilder getSearch(String useYn, String symbolNm) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QSymbol qSymbol = QSymbol.symbol;

        booleanBuilder.and(qSymbol.delYn.eq("N"));

        if(!StringUtils.isEmpty(useYn)){
            booleanBuilder.and(qSymbol.useYn.eq(useYn));
        }
        if(!StringUtils.isEmpty(symbolNm)){
            booleanBuilder.and(qSymbol.symbolNm.contains(symbolNm));
        }

        return booleanBuilder;
    }
}
