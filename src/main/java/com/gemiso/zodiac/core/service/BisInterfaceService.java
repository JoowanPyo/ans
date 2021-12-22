package com.gemiso.zodiac.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemiso.zodiac.app.baseProgram.BaseProgram;
import com.gemiso.zodiac.app.baseProgram.BaseProgramRepository;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramDTO;
import com.gemiso.zodiac.app.baseProgram.mapper.BaseProgramMapper;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.ProgramRepository;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import com.gemiso.zodiac.core.scheduling.dto.BisBasicScheduleDTO;
import com.gemiso.zodiac.core.scheduling.dto.BisProgramDTO;
import com.gemiso.zodiac.core.scheduling.dto.DsBasicScheduleListDTO;
import com.gemiso.zodiac.core.scheduling.dto.DsProgramDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BisInterfaceService {

    private final ProgramRepository programRepository;
    private final BaseProgramRepository baseProgramRepository;

    private final BaseProgramMapper baseProgramMapper;

    private final UserAuthService userAuthService;

    //Bis에서 프로그램 정보를 가져온다.
    public BisProgramDTO bisProgramfindAll() throws Exception{

        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        httpHeaders.add("Session_user_id", "ans");

        //Object Mapper를 통한 Json바인딩할 dmParam생성
        Map<String, Object> dmParam = new HashMap<>();
        dmParam.put("companyCd", "C1");
        dmParam.put("chanId", "CH_K");
      /*  dmParam.put("productClf", "004");
        dmParam.put("productCorpNm", "");
        dmParam.put("pgmType", "0");
        dmParam.put("delibGrade", "15");
        dmParam.put("productCountry1", "KR");
        dmParam.put("useYn", "Y");
        dmParam.put("scheduleClf", "0");
        dmParam.put("foreignClf", "0");
        dmParam.put("pgmCd", "");
        dmParam.put("pgmNm", "더쇼");*/

        //Object Mapper를 통한 Json바인딩할 data생성
        Map<String, Object> data = new HashMap<>();
        data.put("dmParam", dmParam);

        //Object Mapper를 통한 Json바인딩
        Map<String, Object> map = new HashMap<>();
        map.put("data", data);
        System.out.println("map : "+map);
        ObjectMapper objectMapper = new ObjectMapper();
        String params = objectMapper.writeValueAsString(map);

        System.out.println("params : "+params);

        //httpEntity에 헤더 및 params 설정
        HttpEntity entity = new HttpEntity(params, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("http://121.134.111.62:8060/api/v1/bis/listProgramV3.do", HttpMethod.POST,
                        entity, String.class);



        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());

        String results = responseEntity.getBody();

        BisProgramDTO bisProgramDTO = new BisProgramDTO();
        bisProgramDTO = objectMapper.readValue(results, BisProgramDTO.class);

        System.out.println(bisProgramDTO);

        return bisProgramDTO;
    }

    //Bis에서 조회한 프로그램 정보와 ANS에 이미 등록되어 있는 프로그램 정보와 비교하여 새로들어온 정보를 업데이트.
    public void matchProgramCreate(BisProgramDTO bisProgramDTO){

        //Bis에서 조회된 프로그램 정보 리스트를 받는다.
        List<DsProgramDTO> dsProgramDTOList = bisProgramDTO.getDsProgram();

        List<Program> programList = programRepository.findAll();//등록되어 있는 ANS 프로그램 리스트를 불러온다.


        for (Program program : programList){ //ANS프로그램 리스트 정보를 확인.

            String brdcPgmId = program.getBrdcPgmId(); //ANS프로그램 아이디를 가져온다.

            for (DsProgramDTO dsProgramDTO : dsProgramDTOList){ //BIS에서 보낸 프로그램 리스트 정보

                String pgmCd = dsProgramDTO.getPgmCd(); // BIS 프로그램 아이디를 가져온다.

                if (brdcPgmId.equals(pgmCd)){// ANS프로그램 아이디와 BIS프로그램 아이디가 같으면 BIS프로그램 리스트에서 삭제.
                    dsProgramDTOList.remove(dsProgramDTO);
                }
            }
        }

        /*String userId = userAuthService.authUser.getUserId();*/

        if (CollectionUtils.isEmpty(dsProgramDTOList) == false) { //BIS에서 새로들오온 프로그램의 정보를 엔티티 빌드후 등록한다.
            for (DsProgramDTO dsProgramDTO : dsProgramDTOList) {
                Program program = Program.builder()
                        .brdcPgmId(dsProgramDTO.getPgmCd())
                        .brdcPgmNm(dsProgramDTO.getPgmNm())
                        .chDivCd(dsProgramDTO.getChanTp())
                        .gneDivCd(dsProgramDTO.getJenreClf1())
                        .prdDivCd(dsProgramDTO.getProductClf())
                        /*.inputrId(userId)*/
                        .build();

                programRepository.save(program);
            }
        }
    }

    //Bis에서 기본편성 정보를 가져온다.
    public BisBasicScheduleDTO bisBasicSchedulefindAll() throws Exception {

        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        httpHeaders.add("Session_user_id", "ans");

        //Object Mapper를 통한 Json바인딩할 dmParam생성
        Map<String, Object> dmParam = new HashMap<>();
        dmParam.put("chanId", "CH_K");
        dmParam.put("broadYmd", "20211101");

        //Object Mapper를 통한 Json바인딩할 data생성
        Map<String, Object> data = new HashMap<>();
        data.put("dmParam", dmParam);

        //Object Mapper를 통한 Json바인딩
        Map<String, Object> map = new HashMap<>();
        map.put("data", data);
        System.out.println("map : "+map);
        ObjectMapper objectMapper = new ObjectMapper();
        String params = objectMapper.writeValueAsString(map);

        //httpEntity에 헤더 및 params 설정
        HttpEntity entity = new HttpEntity(params, httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity =
                restTemplate.exchange("http://121.134.111.62:8060/api/v1/bis/listBasicScheduleV3.do", HttpMethod.POST,
                        entity, String.class);

        System.out.println(responseEntity.getStatusCode());
        System.out.println(responseEntity.getBody());

        String results = responseEntity.getBody();

        BisBasicScheduleDTO bisBasicScheduleDTO = new BisBasicScheduleDTO();
        bisBasicScheduleDTO = objectMapper.readValue(results, BisBasicScheduleDTO.class);

        System.out.println(bisBasicScheduleDTO);

        return bisBasicScheduleDTO;
    }

    public void matchBasicScheduleCreate(BisBasicScheduleDTO bisBasicScheduleDTO){

        //bis에서 조회한 기본편성 정보를 불러온다.
        List<DsBasicScheduleListDTO> dsBasicScheduleListDTOList = bisBasicScheduleDTO.getDsBasicScheduleList();
        List<DsBasicScheduleListDTO> addDsBasicScheduleListDTOList = new ArrayList<>();

        //ANS에 등록되어 있는 기본편성을 가져온다.
        List<BaseProgram> baseProgramList = baseProgramRepository.findAll();

        for (BaseProgram baseProgram : baseProgramList){

            String basePgmschId = baseProgram.getBasePgmschId(); //기본편성 아이디를 가져온다

            for (DsBasicScheduleListDTO dsBasicScheduleDTO : dsBasicScheduleListDTOList){

                String basicScheduleId = dsBasicScheduleDTO.getBasicScheduleId();//Bis에서 조회한 기본편성 아이디를 가져온다.

                // 기본편성 아이디가 같다면, 이미 등록되어 있는 정보이므로 bis에서 조회한 기본편성 리스트 에서 삭제.
                if (basePgmschId.equals(basicScheduleId) == false){
                    addDsBasicScheduleListDTOList.add(dsBasicScheduleDTO);
                }
            }
        }

        //새로들어온 기본편성이 있으면 등록
        if (CollectionUtils.isEmpty(addDsBasicScheduleListDTOList) == false){
            //새로들어온 기본편성 등록
            for (DsBasicScheduleListDTO dsBasicScheduleDTO : addDsBasicScheduleListDTOList){

                String brdcPgmId = dsBasicScheduleDTO.getPgmCd();
                ProgramSimpleDTO programSimpleDTO = ProgramSimpleDTO.builder().brdcPgmId(brdcPgmId).build();

                BaseProgramDTO baseProgramDTO = BaseProgramDTO.builder()
                        .basePgmschId(dsBasicScheduleDTO.getBasicScheduleId())
                        .brdcStartDt(dsBasicScheduleDTO.getBroadBeginYmd())
                        .brdcEndDt(dsBasicScheduleDTO.getBroadEndYmd())
                        .brdcStartClk(dsBasicScheduleDTO.getBroadHm())
                        .program(programSimpleDTO)
                        .build();

                BaseProgram baseProgram = baseProgramMapper.toEntity(baseProgramDTO);

                baseProgramRepository.save(baseProgram);
            }

        }


    }
}
