package com.gemiso.zodiac.app.scrollNews;

import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsCreateDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsSimpleDTO;
import com.gemiso.zodiac.app.scrollNews.dto.ScrollNewsUpdateDTO;
import com.gemiso.zodiac.app.scrollNews.mapper.ScrollNewsCreateMapper;
import com.gemiso.zodiac.app.scrollNews.mapper.ScrollNewsMapper;
import com.gemiso.zodiac.app.scrollNews.mapper.ScrollNewsUpdateMapper;
import com.gemiso.zodiac.app.scrollNewsDetail.ScrollNewsDetail;
import com.gemiso.zodiac.app.scrollNewsDetail.ScrollNewsDetailRepository;
import com.gemiso.zodiac.app.scrollNewsDetail.dto.ScrollNewsDetailCreateDTO;
import com.gemiso.zodiac.app.scrollNewsDetail.dto.ScrollNewsDetailCttJsonDTO;
import com.gemiso.zodiac.app.scrollNewsDetail.mapper.ScrollNewsDetailCreateMapper;
import com.gemiso.zodiac.app.scrollNewsFtpInfo.ScrollNewsFtpInfo;
import com.gemiso.zodiac.app.scrollNewsFtpInfo.ScrollNewsFtpInfoService;
import com.gemiso.zodiac.core.helper.DateChangeHelper;
import com.gemiso.zodiac.core.helper.MarshallingJsonHelper;
import com.gemiso.zodiac.core.helper.SearchDate;
import com.gemiso.zodiac.core.service.FTPconnectService;
import com.gemiso.zodiac.exception.ResourceNotFoundException;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScrollNewsService {

    private final ScrollNewsRepository scrollNewsRepository;
    private final ScrollNewsDetailRepository scrollNewsDetailRepository;

    private final ScrollNewsMapper scrollNewsMapper;
    private final ScrollNewsCreateMapper scrollNewsCreateMapper;
    private final ScrollNewsUpdateMapper scrollNewsUpdateMapper;
    private final ScrollNewsDetailCreateMapper scrollNewsDetailCreateMapper;

    //private final UserAuthService userAuthService;
    private final ScrollNewsFtpInfoService scrollNewsFtpInfoService;

    private final DateChangeHelper dateChangeHelper;
    private final MarshallingJsonHelper marshallingJsonHelper;

   /* @Value("${ftp1.ip:ftp1Ip}")
    private String ftp1Ip;
    @Value("${ftp1.port:ftp1Port}")
    private Integer ftp1Port;
    @Value("${ftp1.id:ftp1Id}")
    private String ftp1Id;
    @Value("${ftp1.pw:ftp1Pw}")
    private String ftp1Pw;
    @Value("${ftp1.path:ftp1Path}")
    private String ftp1Path;

    @Value("${ftp2.ip:ftp2Ip}")
    private String ftp2Ip;
    @Value("${ftp2.port:ftp2Port}")
    private Integer ftp2Port;
    @Value("${ftp2.id:ftp2Id}")
    private String ftp2Id;
    @Value("${ftp2.pw:ftp2Pw}")
    private String ftp2Pw;
    @Value("${ftp2.path:ftp2Path}")
    private String ftp2Path;

    @Value("${ftp3.ip:ftp3Ip}")
    private String ftp3Ip;
    @Value("${ftp3.port:ftp3Port}")
    private Integer ftp3Port;
    @Value("${ftp3.id:ftp3Id}")
    private String ftp3Id;
    @Value("${ftp3.pw:ftp3Pw}")
    private String ftp3Pw;
    @Value("${ftp3.path:ftp3Path}")
    private String ftp3Path;

    @Value("${ftp4.ip:ftp4Ip}")
    private String ftp4Ip;
    @Value("${ftp4.port:ftp4Port}")
    private Integer ftp4Port;
    @Value("${ftp4.id:ftp4Id}")
    private String ftp4Id;
    @Value("${ftp4.pw:ftp4Pw}")
    private String ftp4Pw;
    @Value("${ftp4.path:ftp4Path}")
    private String ftp4Path;

    @Value("${ftp5.ip:ftp5Ip}")
    private String ftp5Ip;
    @Value("${ftp5.port:ftp5Port}")
    private Integer ftp5Port;
    @Value("${ftp5.id:ftp5Id}")
    private String ftp5Id;
    @Value("${ftp5.pw:ftp5Pw}")
    private String ftp5Pw;
    @Value("${ftp5.path:ftp5Path}")
    private String ftp5Path;*/


    //FTP코드 생성자
    FTPconnectService ftp = new FTPconnectService();
    FileInputStream fis = null;

    //스크롤 뉴스 목록조회
    @Transactional
    public List<ScrollNewsDTO> findAll(Date sdate, Date edate, String delYn) {

        //스크롤 뉴스 조회조건 빌드
        BooleanBuilder booleanBuilder = getSearch(sdate, edate, delYn);

        //빌드된 조회조건으로 스크롤 뉴스 엔티티 목록 조회
        List<ScrollNews> scrollNews =
                (List<ScrollNews>) scrollNewsRepository.findAll(booleanBuilder, Sort.by(Sort.Direction.ASC, "inputDtm"));

        //목록조회된 엔티티 리스트 DTO변환
        List<ScrollNewsDTO> scrollNewsDTOList = scrollNewsMapper.toDtoList(scrollNews);

        //articleDTO 리스트 리턴
        return scrollNewsDTOList;
    }

    //스크롤 뉴스 상세조회
    @Transactional
    public ScrollNewsDTO find(Long scrlNewsId) {

        ScrollNews scrollNews = findScrollNews(scrlNewsId); //스크롤뉴스 아이디로 조회 및 존재유무 확인

        ScrollNewsDTO scrollNewsDTO = scrollNewsMapper.toDto(scrollNews);

        return scrollNewsDTO;

    }

    public void send(ScrollNewsDTO scrollNewsDTO) {

        String fileNm = scrollNewsDTO.getFileNm();
        String scrlFrmCd = scrollNewsDTO.getScrlFrmCd();
        Long scrlNewsId = scrollNewsDTO.getScrlNewsId();
        List<ScrollNewsDetail> scrollNewsDetailList = scrollNewsDetailRepository.findDetailsList(scrlNewsId);

        String makeFileNm = fileNm + ".txt";

        try {

            File makeForder = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text");
            //File makeForder = new File(System.getProperty("user.dir")+File.separator+"storage"+File.separator+"send_text");

            if (makeForder.exists() == false) {

                try {

                    Path directoryPath = Paths.get(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text");
                    Files.createDirectories(directoryPath);//폴더 생성합니다.
                    //System.out.println("폴더가 생성되었습니다.");
                } catch (Exception e) {
                    String message = e.getMessage();
                    log.error("스크롤 뉴스 send 디렉토리 생성 오류 : "+message);
                    //e.getStackTrace();
                }
            }

            // 1. 파일 객체 생성
            File file = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text" + File.separator + makeFileNm);
            //File file = new File(System.getProperty("user.dir")+File.separator+"storage"+File.separator+"send_text"+File.separator+fileNm+".txt");

            // 2. 파일 존재여부 체크 및 생성
            if (file.exists() == false) {
                file.createNewFile();
            }

            //Writer 생성
            FileWriter fw = new FileWriter(file);
            PrintWriter writer = new PrintWriter(fw);

            try {


                if ("scroll_typ_scroll".equals(scrlFrmCd)) {

                    //파일에 쓰기
                    writer.write("{#SC}\n");
                    writer.println();

                    for (ScrollNewsDetail entity : scrollNewsDetailList) {

                        String cttJson = entity.getCttJson();
                        String title = entity.getTitl();

                        writer.println("*sc " + title);

                        JSONParser parser = new JSONParser();
                        Object obj = parser.parse(cttJson);
                        JSONArray jsonObj = (JSONArray) obj;

                        for (int i = 0; i < jsonObj.size(); i++) {
                            JSONObject object = (JSONObject) jsonObj.get(i);
                            String line = (String) object.get("line");
                            writer.println("=" + line);
                        }
                        writer.println();
                    }


                } else if ("scroll_typ_standup".equals(scrlFrmCd)) {

                    //파일에 쓰기
                    writer.write("{#RU}\n");
                    writer.println();

                    for (ScrollNewsDetail entity : scrollNewsDetailList) {

                        String cttJson = entity.getCttJson();
                        String title = entity.getTitl();

                        writer.println("*sc " + title);

                        JSONParser parser = new JSONParser();
                        Object obj = parser.parse(cttJson);
                        JSONArray jsonObj = (JSONArray) obj;

                        for (int i = 0; i < jsonObj.size(); i++) {
                            JSONObject object = (JSONObject) jsonObj.get(i);
                            String line = (String) object.get("line");
                            writer.println("=" + line);
                        }

                        writer.println();
                    }


                } else {
                    log.error(" 스크롤 뉴스 타입이 맞지 않습니다 . scrlFrmCd - " + scrlFrmCd);
                }

                writer.print("####");
                //writer.close();

            }catch (IndexOutOfBoundsException e){

                log.error("스크롤 뉴스 프린트 에러 : "+"IndexOutOfBoundsException");
            } finally {
                writer.close();
            }


        } catch (IOException | ParseException e) {

            String message  =  e.getMessage();
            log.error(" 스크롤 뉴스 에러 : massage - " + message);

        }

        sendTextFile(makeFileNm);

    }

    //FTP전송 [ 전송장비 총 5개 ]
    public void sendTextFile(String makeFileNm) {

        //FTP전송 [ 1 ]
        sendFile1(makeFileNm);

        //FTP전송 [ 2 ]
        sendFile2(makeFileNm);

        //FTP전송 [ 3 ]
        sendFile3(makeFileNm);

        //FTP전송 [ 4 ]
        sendFile4(makeFileNm);

        //FTP전송 [ 5 ]
        sendFile5(makeFileNm);


    }
    //FTP전송 [ 전송장비 5 ]
    public void sendFile5(String makeFileNm) {


        Long id = 5L;

        ScrollNewsFtpInfo scrollNewsFtpInfo = scrollNewsFtpInfoService.findScrollFtpInfo(id);


        String ftpIp = scrollNewsFtpInfo.getFtpIp();
        Integer ftpPort = scrollNewsFtpInfo.getFtpPort();
        String ftpId = scrollNewsFtpInfo.getFtpId();
        String ftpPw = scrollNewsFtpInfo.getFtpPw();
        String ftpPath = scrollNewsFtpInfo.getFtpPath();
        String ftpType = scrollNewsFtpInfo.getFtpType();

        try {
            //nam ftp 커넥트
            if ("active".equals(ftpType)) {
                ftp.connectActive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            }else if ("passive".equals(ftpType)){
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }

            File file = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text" + File.separator +makeFileNm);

            try {
                if(file.exists()) {
                    fis = new FileInputStream(file);
                    ftp.storeFile(makeFileNm, fis); //파일명

                    log.info("FTP5 파일명 : "+makeFileNm+" fis : "+fis.toString());
                }
            } catch (Exception e) {
                String message = e.getMessage();
                log.info("FTP5 file handle exception : "+ message);
                // TODO: handle exception
            } finally {
                if(fis != null) {
                    fis.close();
                }
            }
            ftp.disconnect();
        } catch (NumberFormatException e) {
            String message = e.getMessage();
            log.info("FTP5 file NumberFormatException : "+ message);
        } catch (Exception e) {
            String message = e.getMessage();
            log.info("FTP5 file Exception : "+ message);
        }
    }

    //FTP전송 [ 전송장비 4 ]
    public void sendFile4(String makeFileNm) {


        Long id = 4L;

        ScrollNewsFtpInfo scrollNewsFtpInfo = scrollNewsFtpInfoService.findScrollFtpInfo(id);


        String ftpIp = scrollNewsFtpInfo.getFtpIp();
        Integer ftpPort = scrollNewsFtpInfo.getFtpPort();
        String ftpId = scrollNewsFtpInfo.getFtpId();
        String ftpPw = scrollNewsFtpInfo.getFtpPw();
        String ftpPath = scrollNewsFtpInfo.getFtpPath();
        String ftpType = scrollNewsFtpInfo.getFtpType();

        try {
            //nam ftp 커넥트
            if ("active".equals(ftpType)) {
                ftp.connectActive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            }else if ("passive".equals(ftpType)){
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }

            File file = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text" + File.separator +makeFileNm);

            try {
                if(file.exists()) {
                    fis = new FileInputStream(file);
                    ftp.storeFile(makeFileNm, fis); //파일명

                    log.info("FTP4 파일명 : "+makeFileNm+" fis : "+fis.toString());
                }
            } catch (Exception e) {
                String message = e.getMessage();
                log.info("FTP4 file handle exception : "+ message);
                // TODO: handle exception
            } finally {

                if(fis != null) {
                    fis.close();
                }
            }
            ftp.disconnect();
        } catch (NumberFormatException e) {
            String message = e.getMessage();
            log.info("FTP4 file NumberFormatException : "+ message);
        } catch (Exception e) {
            String message = e.getMessage();
            log.info("FTP4 file Exception : "+ message);
        }
    }

    //FTP전송 [ 전송장비 3 ]
    public void sendFile3(String makeFileNm) {


        Long id = 3L;

        ScrollNewsFtpInfo scrollNewsFtpInfo = scrollNewsFtpInfoService.findScrollFtpInfo(id);


        String ftpIp = scrollNewsFtpInfo.getFtpIp();
        Integer ftpPort = scrollNewsFtpInfo.getFtpPort();
        String ftpId = scrollNewsFtpInfo.getFtpId();
        String ftpPw = scrollNewsFtpInfo.getFtpPw();
        String ftpPath = scrollNewsFtpInfo.getFtpPath();
        String ftpType = scrollNewsFtpInfo.getFtpType();

        try {
            //nam ftp 커넥트
            if ("active".equals(ftpType)) {
                ftp.connectActive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            }else if ("passive".equals(ftpType)){
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }

            File file = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text" + File.separator +makeFileNm);

            try {

                if(file.exists()) {
                    fis = new FileInputStream(file);
                    ftp.storeFile(makeFileNm, fis); //파일명

                    log.info("FTP3 파일명 : "+makeFileNm+" fis : "+fis.toString());
                }
            } catch (Exception e) {
                String message = e.getMessage();
                log.info("FTP3 file handle exception : "+ message);
                // TODO: handle exception
            } finally {
                if(fis != null) {
                    fis.close();
                }
            }
            ftp.disconnect();
        } catch (NumberFormatException e) {
            String message = e.getMessage();
            log.info("FTP3 file NumberFormatException : "+ message);
        } catch (Exception e) {
            String message = e.getMessage();
            log.info("FTP3 file Exception : "+ message);
        }
    }

    //FTP전송 [ 전송장비 2 ]
    public void sendFile2(String makeFileNm) {

        Long id = 2L;

        ScrollNewsFtpInfo scrollNewsFtpInfo = scrollNewsFtpInfoService.findScrollFtpInfo(id);


        String ftpIp = scrollNewsFtpInfo.getFtpIp();
        Integer ftpPort = scrollNewsFtpInfo.getFtpPort();
        String ftpId = scrollNewsFtpInfo.getFtpId();
        String ftpPw = scrollNewsFtpInfo.getFtpPw();
        String ftpPath = scrollNewsFtpInfo.getFtpPath();
        String ftpType = scrollNewsFtpInfo.getFtpType();

        try {
            //nam ftp 커넥트
            if ("active".equals(ftpType)) {
                ftp.connectActive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            }else if ("passive".equals(ftpType)){
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }
            File file = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text" + File.separator +makeFileNm);

            try {

                if(file.exists()) {
                    fis = new FileInputStream(file);
                    ftp.storeFile(makeFileNm, fis); //파일명

                    log.info("FTP2 파일명 : "+makeFileNm+" fis : "+fis.toString());
                }
            } catch (Exception e) {
                String message = e.getMessage();
                log.info("FTP2 file handle exception : "+ message);
                // TODO: handle exception
            } finally {
                if(fis != null) {
                    fis.close();
                }
            }
            ftp.disconnect();
        } catch (NumberFormatException e) {
            String message = e.getMessage();
            log.info("FTP2 file NumberFormatException : "+ message);
        } catch (Exception e) {
            String message = e.getMessage();
            log.info("FTP2 file Exception : "+ message);
        }
    }

    //FTP전송 [ 전송장비 1 ]
    public void sendFile1(String makeFileNm) {

        Long id = 1L;

        ScrollNewsFtpInfo scrollNewsFtpInfo = scrollNewsFtpInfoService.findScrollFtpInfo(id);


        String ftpIp = scrollNewsFtpInfo.getFtpIp();
        Integer ftpPort = scrollNewsFtpInfo.getFtpPort();
        String ftpId = scrollNewsFtpInfo.getFtpId();
        String ftpPw = scrollNewsFtpInfo.getFtpPw();
        String ftpPath = scrollNewsFtpInfo.getFtpPath();
        String ftpType = scrollNewsFtpInfo.getFtpType();

        try {
            //nam ftp 커넥트
            if ("active".equals(ftpType)) {
                ftp.connectActive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);

            }else if ("passive".equals(ftpType)){
                ftp.connectPassive(ftpIp, ftpPort, ftpId, ftpPw, ftpPath);
            }

            File file = new File(System.getProperty("user.dir") + File.separator + "storage" + File.separator + "send_text" + File.separator +makeFileNm);

            try {
                if(file.exists()) {
                    fis = new FileInputStream(file);
                    ftp.storeFile(makeFileNm, fis); //파일명

                    log.info("FTP1 파일명 : "+makeFileNm+" fis : "+fis.toString());
                }
            } catch (Exception e) {
                String message = e.getMessage();
                log.info("FTP1 file handle exception : "+ message);
                // TODO: handle exception
            } finally {
                if(fis != null) {
                    fis.close();
                }
            }
            ftp.disconnect();
        } catch (NumberFormatException e) {
            String message = e.getMessage();
            log.info("FTP1 file NumberFormatException : "+ message);
        } catch (Exception e) {
            String message = e.getMessage();
            log.info("FTP1 file Exception : "+ message);
        }
    }

    //스크롤 뉴스 등록
    @Transactional
    public Long create(ScrollNewsCreateDTO scrollNewsCreateDTO, String userId) throws Exception {

        //String userId = userAuthService.authUser.getUserId(); //토큰 사용자 아이디 get
        scrollNewsCreateDTO.setInputrId(userId); //입력자 set

        String fileNm = scrollNewsCreateDTO.getFileNm();
        String disasterFileNm = null;
        //재난으로 들어왔을 경우 파일명 + 00 넘버 시퀀스 추가
        String scrlDivCd = scrollNewsCreateDTO.getScrlDivCd();
        if ("scroll_disaster".equals(scrlDivCd)){

            Integer count = 0;

            Date nowDate = scrollNewsCreateDTO.getBrdcDtm();

            String stringDate = dateChangeHelper.dateToStringNoTime(nowDate);
            Date dateDate = dateChangeHelper.StringToDateNoTime(stringDate);

            SearchDate searchDate = new SearchDate(dateDate, dateDate);

            Date ddd = searchDate.getStartDate();
            Date eee = searchDate.getEndDate();

            Optional<Integer> scrollNewsCount = scrollNewsRepository.findScrollNewsCount(searchDate.getStartDate(), searchDate.getEndDate(), scrlDivCd);

            if (scrollNewsCount.isPresent() == false){
                count = 0;
            }else {
                count = scrollNewsCount.get();
            }

            String stringNumber = Integer.toString(count);

            if (stringNumber.length() > 1){
                disasterFileNm = fileNm+stringNumber;
            }else {
                disasterFileNm = fileNm+"0"+stringNumber;
            }

            scrollNewsCreateDTO.setFileNm(disasterFileNm);
        }

        ScrollNews scrollNews = scrollNewsCreateMapper.toEntity(scrollNewsCreateDTO);//등록DTO 엔티티 빌드.
        scrollNewsRepository.save(scrollNews);// 등록

        Long scrlNewsId = scrollNews.getScrlNewsId(); //스크롤 뉴스 상세에 set && 리턴해줄 스크롤 뉴스 아이디
        //스크롤 뉴스 상세 등록 리스트 get
        List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO = scrollNewsCreateDTO.getScrollNewsDetails();

        createDetail(scrollNewsDetailCreateDTO, scrlNewsId); //스크럴 뉴스 상세 등록.

        return scrlNewsId;
    }

    @Transactional
    public void update(ScrollNewsUpdateDTO scrollNewsUpdateDTO, Long scrlNewsId, String userId) throws Exception {

        ScrollNews scrollNews = findScrollNews(scrlNewsId); //스크롤뉴스 아이디로 조회 및 존재유무 확인

        //String userId = userAuthService.authUser.getUserId();//토큰 사용자 아이디 get
        scrollNewsUpdateDTO.setUpdtrId(userId); //수정자 등록

        scrollNewsUpdateMapper.updateFromDto(scrollNewsUpdateDTO, scrollNews);//스크롤뉴스 정보 업데이트

        scrollNewsRepository.save(scrollNews); //수정

        //스크롤 뉴스 상세 수정 리스트 get
        List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO = scrollNewsUpdateDTO.getScrollNewsDetail();

        //스크롤 뉴스 상세 수정.
        updateDetail(scrollNewsDetailCreateDTO, scrlNewsId);
    }

    //스크롤 뉴스 삭제
    @Transactional
    public void delete(Long scrlNewsId, String userId) {

        ScrollNews scrollNews = findScrollNews(scrlNewsId); //스크롤뉴스 아이디로 조회 및 존재유무 확인

        //String userId = userAuthService.authUser.getUserId();//토큰 사용자 아이디 get

        ScrollNewsDTO scrollNewsDTO = scrollNewsMapper.toDto(scrollNews);
        scrollNewsDTO.setDelDtm(new Date()); //삭제 일시 set
        scrollNewsDTO.setDelrId(userId); // 삭제자 set
        scrollNewsDTO.setDelYn("Y"); //삭제 여부값 Y

        scrollNewsMapper.updateFromDto(scrollNewsDTO, scrollNews);
        scrollNewsRepository.save(scrollNews); //스크롤 뉴스 삭제

    }

    //스크롤 뉴스 상세 업데이트
    @Transactional
    public void updateDetail(List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO, Long scrlNewsId) throws Exception {

        //스크롤뉴스 상세 List조회[기존에 등록되어 있던 스크롤뉴스 상세 삭재 후 재등록]
        List<ScrollNewsDetail> scrollNewsDetails = scrollNewsDetailRepository.findDetailsList(scrlNewsId);

        //조회된 스크롤뉴스 상세 리스트 삭제
        for (ScrollNewsDetail scrollNewsDetail : scrollNewsDetails) {
            Long id = scrollNewsDetail.getId();
            scrollNewsDetailRepository.deleteById(id);
        }

        createDetail(scrollNewsDetailCreateDTO, scrlNewsId); //스크롤뉴스 상세 재등록

    }

    //스크롤 뉴스 상세 리스트 등록
    @Transactional
    public void createDetail(List<ScrollNewsDetailCreateDTO> scrollNewsDetailCreateDTO, Long scrlNewsId) throws Exception {

        //스크롤 뉴스 상세에 등록할 스크롤 뉴스 아이디 빌드
        ScrollNewsSimpleDTO scrollNewsSimpleDTO = ScrollNewsSimpleDTO.builder().scrlNewsId(scrlNewsId).build();

        //스크를 뉴스 상세 등록DTO 리스트 등록
        for (ScrollNewsDetailCreateDTO dto : scrollNewsDetailCreateDTO) {

            //내용 Json타입으로 변환
            List<ScrollNewsDetailCttJsonDTO> ctts = dto.getCttJsons();
            String returnCtt = "";
            if (CollectionUtils.isEmpty(ctts) == false) {
                returnCtt = marshallingJsonHelper.MarshallingJson(ctts);
            }

            //dto.setCttJson(returnCtt);//내용 Json타입으로 변환
            dto.setScrollNews(scrollNewsSimpleDTO);//스크롤 뉴스 아이디 set
            ScrollNewsDetail scrollNewsDetail = scrollNewsDetailCreateMapper.toEntity(dto);//스크롤 뉴스 상세DTO 엔티티 변환
            scrollNewsDetail.setCttJson(returnCtt);
            scrollNewsDetailRepository.save(scrollNewsDetail);//스크롤 뉴스 상세 등록

        }

    }

    //내용 Json변환
    //기사 액션로그 엔티티 Json변환
 /*   public String entityToJson(String ctt) throws Exception {

        ObjectMapper mapper = new ObjectMapper();

        String jsonInString = mapper.writeValueAsString(ctt);

        return jsonInString;
    }*/


    //스크롤뉴스 아이디로 조회 및 존재유무 확인
    @Transactional
    public ScrollNews findScrollNews(Long scrlNewsId) {

        //null방지를 위해 옵셔널로 스크롤 뉴스 조회[조회 조건 스크롤 뉴스 아이디 && 삭제여부 N]
        Optional<ScrollNews> scrollNews = scrollNewsRepository.findScrollNews(scrlNewsId);

        if (scrollNews.isPresent() == false) { //조회된 스크롤 뉴스가 없으면 에러처리.
            throw new ResourceNotFoundException("스크롤 뉴스를 찾을 수 없습니다. 스크롤 뉴스 아이디 : " + scrlNewsId);
        }

        return scrollNews.get(); //조회된 스크롤 뉴스 리턴
    }

    //스크롤 뉴스 목록조회 조건 빌드
    @Transactional
    public BooleanBuilder getSearch(Date sdate, Date edate, String delYn) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QScrollNews qScrollNews = QScrollNews.scrollNews;

        //조회조건이 날짜로 들어온 경우
        if (ObjectUtils.isEmpty(sdate) == false && ObjectUtils.isEmpty(edate) == false) {
            booleanBuilder.and(qScrollNews.brdcDtm.between(sdate, edate));
        }

        //조회조건이 삭제 여부값으로 들어온 경우
        if (delYn != null && delYn.trim().isEmpty() == false) {
            booleanBuilder.and(qScrollNews.delYn.eq(delYn));
        } else { //삭제 여부값이 안들어왔을 경우 디폴트 N
            booleanBuilder.and(qScrollNews.delYn.eq("N"));
        }

        return booleanBuilder;
    }

}
