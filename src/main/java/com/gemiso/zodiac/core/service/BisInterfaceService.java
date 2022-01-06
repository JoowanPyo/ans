package com.gemiso.zodiac.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemiso.zodiac.app.baseProgram.BaseProgram;
import com.gemiso.zodiac.app.baseProgram.BaseProgramRepository;
import com.gemiso.zodiac.app.baseProgram.dto.BaseProgramDTO;
import com.gemiso.zodiac.app.baseProgram.mapper.BaseProgramMapper;
import com.gemiso.zodiac.app.dailyProgram.DailyProgram;
import com.gemiso.zodiac.app.dailyProgram.DailyProgramRepository;
import com.gemiso.zodiac.app.program.Program;
import com.gemiso.zodiac.app.program.ProgramRepository;
import com.gemiso.zodiac.app.program.dto.ProgramSimpleDTO;
import com.gemiso.zodiac.core.scheduling.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BisInterfaceService {

    private final ProgramRepository programRepository;
    private final BaseProgramRepository baseProgramRepository;
    private final DailyProgramRepository dailyProgramRepository;

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

                    /*if (updDt != null && updDt.trim().isEmpty() == false){
                        if (updDt.equals(updtDtm) == false){

                        }
                    }*/

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

    //Bis에서 주간편성 정보를 가져온다.
    public BisDailyScheduleDTO bisDailyScheduleFindAll() throws Exception {

        //헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        httpHeaders.add("Session_user_id", "ans");

        //Object Mapper를 통한 Json바인딩할 dmParam생성
        Map<String, Object> dmParam = new HashMap<>();
        dmParam.put("chanId", "CH_K");
        dmParam.put("broadYmd", "20211207");
        dmParam.put("planNo", "1");

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
                restTemplate.exchange("http://121.134.111.62:8060/api/v1/bis/listScheduleV3.do", HttpMethod.POST,
                        entity, String.class);


        String results = responseEntity.getBody();

        BisDailyScheduleDTO bisDailyScheduleDTO = new BisDailyScheduleDTO();
        bisDailyScheduleDTO = objectMapper.readValue(results, BisDailyScheduleDTO.class);


        return bisDailyScheduleDTO;

    }

    //Bis에서 조회한 주간편성 정보와 ANS에 이미 등록되어 있는 일일편성 정보와 비교하여 새로들어온 data가 있으면 재등록
    public void bisDailyScheduleCreate(BisDailyScheduleDTO bisDailyScheduleDTO) throws ParseException {

        //Bis에서 조회하여 담아온 DTO에서 데이터 주간편성 데이터 리스트를 가져온다.
        List<DschWeekDTO> dschWeekDTOList = bisDailyScheduleDTO.getDsSchWeek();

        //기존에 등록되어 있던 일일편성 데이터를 모두 가져온다.
        List<DailyProgram> dailyProgramList = dailyProgramRepository.findAll();

        int dschWeekDTOListSize = dschWeekDTOList.size();//BIS 에서 조회된 리스트 사이즈를 가져온다.
        int dailyProgramListSize = dailyProgramList.size();//ANS에 등록되어있는 리스트 사이즈를 가져온다.

        //BIS와 ANS리스트 사이즈가 다르면 기존데이터 삭재후 BIS에서 조회한 데이터 재등록.
        if (dschWeekDTOListSize != dailyProgramListSize){

            regenerateDailyProgram(dailyProgramList, dschWeekDTOList); //ANS일일편성 재등록.
            return;
        }

        //ANS 일일편성 리스트와 BIS에서 조회한 주간편성 리스트 업데이트 여부 검증.
        for (DailyProgram dailyProgram : dailyProgramList){

            String brdcPgmId = dailyProgram.getProgram().getBrdcPgmId();//방송프로그램 아이디를 가져온다.
            String brdcStartTime = dailyProgram.getBrdcStartTime();//방송 시작시간을 가져온다.
            String updtDtm = dailyProgram.getUpdtDtm(); //수정날짜.

            //ANS데이터와 BIS조회 데이터 비교검증
            for (DschWeekDTO dschWeekDTO : dschWeekDTOList){

                String pgmCd = dschWeekDTO.getPgmCd(); //프로그램 아이디를 가져온다.
                String broadHm = dschWeekDTO.getBroadHm(); //방송시각
                String updDt = dschWeekDTO.getUpdDt();//수정날짜

                if (brdcPgmId.equals(pgmCd) && brdcStartTime.equals(broadHm)){

                    if (updDt != null && updDt.trim().isEmpty() == false){
                        if (updDt.equals(updtDtm) == false){
                            regenerateDailyProgram(dailyProgramList, dschWeekDTOList);
                            return;
                        }
                    }
                }
            }
        }
    }

    //Ans 일일편성 재등록.
    public void regenerateDailyProgram(List<DailyProgram> dailyProgramList, List<DschWeekDTO> dschWeekDTOList) throws ParseException {

        //ANS일일편성 삭제
        for (DailyProgram dailyProgram : dailyProgramList){
            Long dailyPgmId = dailyProgram.getDailyPgmId();
            dailyProgramRepository.deleteById(dailyPgmId);
        }

        for (DschWeekDTO dschWeekDTO : dschWeekDTOList){

            String brdcPgmId = dschWeekDTO.getPgmCd(); //프로그램 아이디
            String getBroadRun = dschWeekDTO.getBroadRun(); //방송길이
            String getBroadHm = dschWeekDTO.getBroadHm(); //방송시각

            String broadRun = getEndTime(getBroadRun, getBroadHm);

            Program program = Program.builder().brdcPgmId(brdcPgmId).build();

            DailyProgram dailyProgram = DailyProgram.builder()
                    .brdcDt(dschWeekDTO.getBroadYmd()) //방송일자
                    .brdcStartTime(dschWeekDTO.getBroadHm()) // 방송시각
                    .brdcEndClk(broadRun) //방송종료 시각
                    .inputDtm(dschWeekDTO.getRegDt()) //입력일시
                    .updtDtm(dschWeekDTO.getUpdDt()) //수정일시
                    .program(program) //프로그램 아이디
                    .build();

            dailyProgramRepository.save(dailyProgram);
        }
    }

    //시작시간,방송길이 데이터로 방송종료시간 구하기.
    public String getEndTime(String getBroadRun, String getBroadHm) throws ParseException {

        String broadRun = ""; // 리턴값.
        //방송시작시간에 "00000000"[년월일]+방송시작시간[시분]+"00"[초]
         String formatBroadHm = "0000-00-00"+" "+getBroadHm.substring(0,2)+":"+getBroadHm.substring(2,4)+":00";

        //방송길이와, 방송시각이 비어있지 않을 경우. 방송시각에 방송길이를 더해 방송 종료시간 계산.
        if (getBroadRun != null && getBroadRun.trim().isEmpty() ==false &&
                getBroadHm != null && getBroadHm.trim().isEmpty() == false) {

            int chkMinute = Integer.parseInt(getBroadRun); //방송길이를 계산하기위해 int로 변환

            if (chkMinute >= 60) {

                int getHour = chkMinute / 60; //60으로 계산했을때 값을 시간으로 계산

                int minute = chkMinute % 60; //60으로 계산했을때 나머지 값을 분으로 계산

                // 시간만 있을경우
                if (ObjectUtils.isEmpty(getHour) == false&& ObjectUtils.isEmpty(minute)) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dateBroadHm = simpleDateFormat.parse(formatBroadHm);//방송시작시간 date로 변환
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateBroadHm);
                    cal.add(Calendar.HOUR, getHour);
                    String StringBroadHm = simpleDateFormat.format(cal.getTime());

                    String endTime = StringBroadHm.substring(11,15);
                    System.out.println(endTime);
                    broadRun = endTime;
                }
                // 시,분 둘다 있을경우,
                if (ObjectUtils.isEmpty(getHour) == false && ObjectUtils.isEmpty(minute) ==false) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dateBroadHm = simpleDateFormat.parse(formatBroadHm);//방송시작시간 date로 변환
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateBroadHm);
                    cal.add(Calendar.HOUR, getHour);
                    cal.add(Calendar.MINUTE, minute);
                    String StringBroadHm = simpleDateFormat.format(cal.getTime());

                    String endTime = StringBroadHm.substring(11,19);
                    System.out.println(endTime);
                    broadRun = endTime;
                }

            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date dateBroadHm = simpleDateFormat.parse(formatBroadHm);//방송시작시간 date로 변환
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateBroadHm);
                cal.add(Calendar.MINUTE, chkMinute);
                String StringBroadHm = simpleDateFormat.format(cal.getTime());

                String endTime = StringBroadHm.substring(11,19);
                System.out.println(endTime);
                broadRun = endTime;
            }
        }

        return broadRun;
    }

    //Bis에서 조회한 기본편성 정보와 ANS에 이미 등록되어 있는 기본편성 정보와 비교하여 새로들어온 data가 있으면 재등록
    public void matchBasicScheduleCreate(BisBasicScheduleDTO bisBasicScheduleDTO) throws ParseException {

        //bis에서 조회한 기본편성 정보를 불러온다.
        List<DsBasicScheduleListDTO> dsBasicScheduleListDTOList = bisBasicScheduleDTO.getDsBasicScheduleList();

        //ANS에 등록되어 있는 기본편성을 가져온다.
        Optional<List<BaseProgram>> getBaseProgramList = Optional.of(baseProgramRepository.findAll());

        if (getBaseProgramList.isPresent() ==false){
            createBaseProgram(dsBasicScheduleListDTOList);
        }

        List<BaseProgram> baseProgramList = getBaseProgramList.get();

        int bisBasicScheduleSize = dsBasicScheduleListDTOList.size();
        int ansBaseProgramSize = baseProgramList.size();


        if (bisBasicScheduleSize != ansBaseProgramSize){
            regenerateBaseProgram(baseProgramList, dsBasicScheduleListDTOList);//기본편성 삭재후 재등록
            return;
        }

        for (BaseProgram baseProgram : baseProgramList){

            String basePgmschId = baseProgram.getProgram().getBrdcPgmId(); //프로그램아이디를 가져온다.
            String basDt = baseProgram.getBasDt();
            String updtDtm = baseProgram.getUpdtDtm();

            for (DsBasicScheduleListDTO dsBasicScheduleDTO : dsBasicScheduleListDTOList){

                String pgmCd = dsBasicScheduleDTO.getPgmCd();//Bis에서 조회한 기본편성 아이디를 가져온다.
                String dayCd = dsBasicScheduleDTO.getDayCd();
                String updDt = dsBasicScheduleDTO.getUpdDt();

                // 기본편성 아이디가 같다면, 이미 등록되어 있는 정보이므로 bis에서 조회한 기본편성 리스트 에서 삭제.
                if (basePgmschId.equals(pgmCd) && basDt.equals(dayCd)){ //프로그램 아이디가 와 방송일자가 일치하면  true

                    if (updDt != null && updDt.trim().isEmpty() == false){
                        if (updDt.equals(updtDtm) == false){
                            //기본편성 삭재후 재등록
                            regenerateBaseProgram(baseProgramList, dsBasicScheduleListDTOList);
                            return;
                        }
                    }
                }
            }
        }


        /*//새로들어온 기본편성이 있으면 등록
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

        }*/
    }

    //기본편성표 생성
    public void createBaseProgram(List<DsBasicScheduleListDTO> dsBasicScheduleListDTOList){
        //새로들어온 기본편성이 있으면 등록
        if (CollectionUtils.isEmpty(dsBasicScheduleListDTOList) == false){
            //새로들어온 기본편성 등록
            for (DsBasicScheduleListDTO dsBasicScheduleDTO : dsBasicScheduleListDTOList){

                String brdcPgmId = dsBasicScheduleDTO.getPgmCd();
                ProgramSimpleDTO programSimpleDTO = ProgramSimpleDTO.builder().brdcPgmId(brdcPgmId).build();

                BaseProgramDTO baseProgramDTO = BaseProgramDTO.builder()
                        //.basePgmschId("")
                        .brdcStartDt(dsBasicScheduleDTO.getBroadBeginYmd())
                        .brdcEndDt(dsBasicScheduleDTO.getBroadEndYmd())
                        .brdcStartClk(dsBasicScheduleDTO.getBroadHm())
                        .basDt(dsBasicScheduleDTO.getDayCd())
                        .inputDtm(dsBasicScheduleDTO.getRegDt())
                        .updtDtm(dsBasicScheduleDTO.getUpdDt())
                        .program(programSimpleDTO)
                        .build();

                BaseProgram baseProgram = baseProgramMapper.toEntity(baseProgramDTO);

                baseProgramRepository.save(baseProgram);
            }

        }

    }

    //ANS 기본편성 재등록
    public void regenerateBaseProgram(List<BaseProgram> baseProgramList, List<DsBasicScheduleListDTO> dsBasicScheduleListDTOList) throws ParseException {

        //등록되어있던 기본편성 삭제
        for (BaseProgram baseProgram : baseProgramList){
            Long basePgmschId = baseProgram.getBasePgmschId();
            baseProgramRepository.deleteById(basePgmschId);
        }

        //새로들어온 기본편성이 있으면 등록
        if (CollectionUtils.isEmpty(dsBasicScheduleListDTOList) == false){
            //새로들어온 기본편성 등록
            for (DsBasicScheduleListDTO dsBasicScheduleDTO : dsBasicScheduleListDTOList){

                String broadHm = dsBasicScheduleDTO.getBroadHm(); //방송시작시간
                String broadRun = dsBasicScheduleDTO.getBroadRun(); //방송길이
                String endTime = getEndTime(broadRun, broadHm); //방송종료시간 가져오기[방송길이 + 방송시작시간]

                //방송프로그램 아이디 방송프로그램DTO로 빌드후  set
                String brdcPgmId = dsBasicScheduleDTO.getPgmCd();
                ProgramSimpleDTO programSimpleDTO = ProgramSimpleDTO.builder().brdcPgmId(brdcPgmId).build();

                BaseProgramDTO baseProgramDTO = BaseProgramDTO.builder()
                        //.basePgmschId("")
                        .brdcStartDt(dsBasicScheduleDTO.getBroadBeginYmd())
                        .brdcEndDt(dsBasicScheduleDTO.getBroadEndYmd())
                        .brdcStartClk(dsBasicScheduleDTO.getBroadHm())
                        .brdcEndClk(endTime)
                        .basDt(dsBasicScheduleDTO.getDayCd())
                        .inputDtm(dsBasicScheduleDTO.getRegDt())
                        .updtDtm(dsBasicScheduleDTO.getUpdDt())
                        .program(programSimpleDTO)
                        .build();

                BaseProgram baseProgram = baseProgramMapper.toEntity(baseProgramDTO);

                baseProgramRepository.save(baseProgram);
            }

        }

    }


}
