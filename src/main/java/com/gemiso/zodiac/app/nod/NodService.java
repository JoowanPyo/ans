package com.gemiso.zodiac.app.nod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemiso.zodiac.app.cueSheet.CueSheet;
import com.gemiso.zodiac.app.cueSheet.CueSheetRepository;
import com.gemiso.zodiac.app.cueSheet.CueSheetService;
import com.gemiso.zodiac.app.cueSheet.dto.CueSheetDTO;
import com.gemiso.zodiac.app.cueSheet.mapper.CueSheetMapper;
import com.gemiso.zodiac.app.nod.dto.NodCreateDTO;
import com.gemiso.zodiac.app.nod.dto.NodDTO;
import com.gemiso.zodiac.app.nod.dto.NodScriptSendDTO;
import com.gemiso.zodiac.app.nod.mapper.NodCreateMapper;
import com.gemiso.zodiac.app.nod.mapper.NodMapper;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.ProgramService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class NodService {

    private final NodRepository nodRepository;
    private final CueSheetRepository cueSheetRepository;

    private final NodMapper nodMapper;
    private final CueSheetMapper cueSheetMapper;
    private final NodCreateMapper nodCreateMapper;

    private final ProgramService programService;
    private final CueSheetService cueSheetService;


    //Nod에서 ANS에 등록된 큐시트 정보 목록조회
    //[검색조건이 현재 날짜와 현재날짜 기준으로 전시간, 후시간 으로 들어온다.
    // 현재 날짜에서 before시간을 뺀 시간과 현재 날짜에서 after시간을 더한 값을 between 조회한다/.]
    public List<CueSheetDTO> findCue(Date nowDate, Integer before, Integer after, String cueDivCd){


        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);
        cal.add(Calendar.HOUR, -before);

        //검색시작날짜
        Date sdate = cal.getTime();

        cal.setTime(nowDate);
        cal.add(Calendar.HOUR, after);

        Date edate = cal.getTime();

        List<CueSheet>  cueSheetList = cueSheetRepository.findNodCue(sdate, edate, cueDivCd);

        List<CueSheetDTO> cueSheetDTOList = cueSheetMapper.toDtoList(cueSheetList);


        return cueSheetDTOList;

    }

    //등록된 Nod정보 조회
    public NodDTO find(Long nodId){

        Nod nod = findNod(nodId);

        NodDTO nodDTO = nodMapper.toDto(nod);

        return nodDTO;
    }

    //Nod등록 후 NodId 홈페이지 전송
    public Long create(NodCreateDTO nodCreateDTO) throws JsonProcessingException {

        //큐시트 검증
        Long cueId = nodCreateDTO.getCueId();
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);

        //프로그램검증
        String brdcPgmId = nodCreateDTO.getBrdcPgmId();
        Program program = programService.programFindOrFail(brdcPgmId);

        Nod nod = nodCreateMapper.toEntity(nodCreateDTO);

        nodRepository.save(nod);


        sendHomePage(nod.getNodId());


        return nod.getNodId();

    }

    //큐시트 화면에서 스크립트 전송 버튼 눌럿을때 스크립트 홈페이지 전송
    public void sendScriptToHomePage(NodScriptSendDTO nodScriptSendDTO) throws JsonProcessingException {

        //큐시트 검증
        Long cueId = nodScriptSendDTO.getCueId();
        CueSheet cueSheet = cueSheetService.cueSheetFindOrFail(cueId);

        //프로그램검증
        Program getProgram = cueSheet.getProgram();
        String brdcPgmId = getProgram.getBrdcPgmId();
        Program program = programService.programFindOrFail(brdcPgmId);

        String brdcPgmDivCd = program.getBrdcPgmDivCd();

        if ("Bulletin".equals(brdcPgmDivCd)){

            sendCueId(cueId);

        }else {
            throw new ResourceNotFoundException("블랜틴 큐시트가 아닙니다. 블랜틴 큐시트 인지 확인해 주세요.");
        }

    }

    //스크립트 전송 [ 홈페이지 ]
    public void sendCueId(Long cueId) throws JsonProcessingException {

        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        //Object Mapper를 통한 Json바인딩할 dmParam생성
        Map<String, Object> dmParam = new HashMap<>();
        dmParam.put("cue_id", cueId);

        ObjectMapper objectMapper = new ObjectMapper();
        String params = objectMapper.writeValueAsString(dmParam);

        //httpEntity에 헤더 및 params 설정
        HttpEntity entity = new HttpEntity(params, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("http://121.134.112.114:8090/api/send-bulletin", HttpMethod.POST,
                        entity, String.class);

        String results = responseEntity.getBody();

    }

    //Nod등록시 홈페이지 전송
    public void sendHomePage(Long nodId) throws JsonProcessingException {

        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        //Object Mapper를 통한 Json바인딩할 dmParam생성
        Map<String, Object> dmParam = new HashMap<>();
        dmParam.put("nod_id", nodId);

        ObjectMapper objectMapper = new ObjectMapper();
        String params = objectMapper.writeValueAsString(dmParam);

        //httpEntity에 헤더 및 params 설정
        HttpEntity entity = new HttpEntity(params, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("http://121.134.112.114:8090/api/send-vod", HttpMethod.POST,
                        entity, String.class);

        String results = responseEntity.getBody();
    }

    public Nod findNod(Long nodId){

        Optional<Nod> nodEntity = nodRepository.findNod(nodId);

        if (nodEntity.isPresent() == false){
            throw new ResourceNotFoundException("NOD를 찾을 수 없습니다. NOD 아이디 "+nodId);
        }

        return nodEntity.get();
    }
}
